package gxt.client.tool;

import java.text.ParseException;
import java.util.List;

import jpa.atm.AtMetroLine;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import gxt.client.domain.F250TownPrx;
import gxt.client.domain.FactAtm;
import gxt.client.domain.MLPrx;
import gxt.client.domain.FactAtm.rcAtm;
import gxt.server.domain.Dao;
import gxt.tool.PanExt;

public class PanML extends PanExt<MLPrx>{
    private static final int PAN_WIDTH = 350;
    private static final int PAN_HEIGHT = 410;
    ColumnConfig<MLPrx, F250TownPrx> ccTown;
    ColumnConfig<MLPrx, String> ccLine;
    ColumnConfig<MLPrx, String> ccColor;
    interface MLProperties extends PropertyAccess<MLPrx> {
	    ModelKeyProvider<MLPrx> id();
	    ValueProvider<MLPrx, F250TownPrx> f250Town();
	    ValueProvider<MLPrx, String> name();
	    ValueProvider<MLPrx, String> color();
	  }
     private static final MLProperties propML = GWT.create(MLProperties.class);
     interface TownProperties extends PropertyAccess<F250TownPrx> {
	    @Path("id")
	    ModelKeyProvider<F250TownPrx> id();
	    ValueProvider<F250TownPrx, String> rusName();
	  }
     private static final TownProperties propTown = GWT.create(TownProperties.class);
     ComboBox<F250TownPrx> cbTown;
     TextField txLine = new TextField();
     TextField txColor = new TextField();
     final ListStore<F250TownPrx> stTown = new ListStore<F250TownPrx>(propTown.id());
     rcAtm reqIns;
     FactAtm f;
     
//----------------------------------------------
    public PanML(FactAtm fct) {
	super(PAN_WIDTH, PAN_HEIGHT, "Линии метро");
	f = fct;
	ccTown = new ColumnConfig<MLPrx, F250TownPrx>(propML.f250Town(), 80, "Город");
	ccTown.setCell(new AbstractCell<F250TownPrx>() {
		      @Override
		      public void render(Context context, F250TownPrx value, SafeHtmlBuilder sb) {
	 		sb.appendHtmlConstant(value.getRusName());
		      } });
	ccLine = new ColumnConfig<MLPrx, String>(propML.name(), 100, "Наименование");
	ccColor = new ColumnConfig<MLPrx, String>(propML.color(), 50, "Цвет");
	ccColor.setCell(new AbstractCell<String>() {
		      @Override
		      public void render(Context context, String value, SafeHtmlBuilder sb) {
			String style = "style='background-color: " + value + "'";
	                sb.appendHtmlConstant("<span " + style + " >" + value + "</span>");
//			sb.appendHtmlConstant(value.getRusName());
		      }});
        getCcL().add(ccTown);
        getCcL().add(ccLine);
        getCcL().add(ccColor);
        setStT(new ListStore<MLPrx>(propML.id()));
        
        setRfpT(new RequestFactoryProxy<ListLoadConfig, ListLoadResult<MLPrx>>() {
 		@Override
 		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<MLPrx>> receiver) {
 		  rcAtm req = f.creRcAtm();
 		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
 		  req.getMLs(sortInfo).with("data.f250Town").to(receiver).fire();
// 		      f.creRcAtm().getAllRec(Factory.SQL_ALLTOWN).fire(new Receiver<List>(){
  		      f.creRcAtm().getAllTown().fire(new Receiver<List>(){
 		  	  @Override
 		  	  public void onSuccess(List response){
 		  		if (response != null)  cbTown.getStore().replaceAll((List<F250TownPrx>)response);
 		  		else cbTown.clear();
 		  	   }});
 		  }});
//        F250TownPrx
    cbTown = new ComboBox<F250TownPrx>(stTown, new LabelProvider<F250TownPrx>(){
            @Override
            public String getLabel(F250TownPrx item) {
              return item==null ? "":item.getRusName();
            }
        });
	cbTown.setPropertyEditor(new PropertyEditor<F250TownPrx>() {
	          @Override
	          public F250TownPrx parse(CharSequence text) throws ParseException {
	            for(F250TownPrx it: stTown.getAll()){
	        	if (it.getRusName().equals(text)) return it;
	            }
	            return null;
	          }
	          @Override
	          public String render(F250TownPrx object) {
	            return object == null ? "XXX" : object.getRusName();
	          }});
	cbTown.setTriggerAction(TriggerAction.ALL);
	cbTown.setForceSelection(true);

        initValues();
        getEditing().addEditor(ccTown, cbTown);
        getEditing().addEditor(ccLine, txLine);
        getEditing().addEditor(ccColor, txColor);
		  
    }
    @Override
    public void mergItem(MLPrx item){
       rcAtm req = null;
       if (isIns) {req = reqIns; isIns = false;}
       else req = f.creRcAtm();
       MLPrx editItem = req.edit(item);
       editItem.setF250Town(cbTown.getCurrentValue());
       editItem.setName(txLine.getText());
       editItem.setColor(txColor.getText());
       req.merg(editItem).fire(mergReceiver);
    }
    @Override
    public void insItem(){
     	reqIns = f.creRcAtm();
     	MLPrx o = reqIns.create(MLPrx.class); //new ServPrx();
     	o.setF250Town(stTown.get(0));
     	o.setName("");
     	o.setColor("");
        stT.add(0, o);
    }
    @Override
    public String getItemName(MLPrx item){
	return item.getName();
    }
    @Override
    public void delItem(MLPrx item, Receiver<Void> R){
       f.creRcAtm().removML(item).fire(R);
    }
    @Override
    protected void beforEdit(){
        txLine.getCell().getInputElement(txLine.getElement()).setMaxLength(AtMetroLine.LEN_name);
        txColor.getCell().getInputElement(txColor.getElement()).setMaxLength(AtMetroLine.LEN_color);
    }
    
}
