package gxt.client.tool;

import java.text.ParseException;
import java.util.List;

import jpa.atm.AtMstation;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;

import gxt.client.domain.FactAtm;
import gxt.client.domain.MLPrx;
import gxt.client.domain.MSPrx;
import gxt.client.domain.FactAtm.rcAtm;
import gxt.tool.PanExtPage;
import gxt.tool.ShuListFilter;
import gxt.tool.ShuListMenu;

public class PanMS extends PanExtPage<MSPrx>{
    private static final int PAN_WIDTH = 350;
    private static final int PAN_HEIGHT = 819;
    private static final int GR_PAGE_SIZE = 35;
    interface MSProperties extends PropertyAccess<MSPrx> {
	    ModelKeyProvider<MSPrx> id();
	    ValueProvider<MSPrx, MLPrx> atMetroLine();
	    ValueProvider<MSPrx, String> name();
	  }
    private static final MSProperties propMS = GWT.create(MSProperties.class);
    interface MLProperties extends PropertyAccess<MLPrx> {
	    @Path("id")
	    ModelKeyProvider<MLPrx> id();
	    ValueProvider<MLPrx, String> name();
	  }
    private static final MLProperties propML = GWT.create(MLProperties.class);
    ComboBox<MLPrx> cbLine;
    final ListStore<MLPrx> stLine = new ListStore<MLPrx>(propML.id());
    final ListStore<MSPrx> stMS = new ListStore<MSPrx>(propMS.id());
    ColumnConfig<MSPrx, MLPrx> ccLine;
    ColumnConfig<MSPrx, String> ccName;
    TextField txName = new TextField();

    rcAtm reqIns;
    FactAtm f;
//=================================================
    public PanMS(FactAtm fct) {
	super(PAN_WIDTH, PAN_HEIGHT, GR_PAGE_SIZE, "Станции метро");
	f = fct;
        ccLine = new ColumnConfig<MSPrx, MLPrx>(propMS.atMetroLine(), 150, "Линия");
	ccLine.setCell(new AbstractCell<MLPrx>() {
	      @Override
	      public void render(Context context, MLPrx value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant(value.getName());
	      } });
        ccName = new ColumnConfig<MSPrx, String>(propMS.name(), 100, "Станция");   
        getCcL().add(ccLine);
        getCcL().add(ccName);
        setStT(new ListStore<MSPrx>(propMS.id()));
        setRfpT(new RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<MSPrx>>() {
            @Override
            public void load(FilterPagingLoadConfig loadConfig, Receiver<? super PagingLoadResult<MSPrx>> receiver) {
              rcAtm req = f.creRcAtm();
              List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
              List<FilterConfig> filterConfig = createRequestFilterConfig(req, loadConfig.getFilters());//,"data.atPart"
              req.getPageMS(loadConfig.getOffset(), loadConfig.getLimit(), sortInfo, filterConfig).with("data.atMetroLine").to(receiver).fire();
//              f.creRcAtm().getAllRec(Factory.SQL_ALLLINE).fire(new Receiver<List>(){
                f.creRcAtm().getAllLine().fire(new Receiver<List>(){
		  	  @Override
		  	  public void onSuccess(List response){
		  		if (response != null)  cbLine.getStore().replaceAll((List<MLPrx>)response);
		  		else cbLine.clear();
		  	   }});
            }});
        //<MLPrx>
        cbLine = new ComboBox<MLPrx>(stLine, new LabelProvider<MLPrx>(){
            @Override
            public String getLabel(MLPrx item) {
              return item==null ? "":item.getName();
            }
        });
	cbLine.setPropertyEditor(new PropertyEditor<MLPrx>() {
	          @Override
	          public MLPrx parse(CharSequence text) throws ParseException {
	            for(MLPrx it: stLine.getAll()){
	        	if (it.getName().equals(text)) return it;
	            }
	            return null;
	          }
	          @Override
	          public String render(MLPrx object) {
	            return object == null ? "XXX" : object.getName();
	          }});
	cbLine.setTriggerAction(TriggerAction.ALL);
	cbLine.setForceSelection(true);

	initValues();
        final ShuListFilter<MSPrx, MLPrx> lineFilter = new ShuListFilter<MSPrx, MLPrx>(new ValueProvider<MSPrx, MLPrx>(){
	    @Override
	    public MLPrx getValue(MSPrx object) {
		return object.getAtMetroLine();
	    }
	    @Override
	    public void setValue(MSPrx object, MLPrx value) {
		object.setAtMetroLine(value);
	    }
	    
	    @Override
	    public String getPath() {
		return "atMetroLine";
	    }} , stLine){
                   @Override
                   protected ShuListMenu<MSPrx, MLPrx> createListMenu(ListStore<MLPrx> store){
                       return new ShuListMenu<MSPrx, MLPrx>(this, store){
                	   protected void renderItem(CheckMenuItem item, MLPrx m){
                	       item.setText(m == null ? "" : m.getName()); 
                	   }
                       };
                   }
                   @Override
                   protected String convertValueToString() {
                       StringBuffer sb = new StringBuffer();
                       List<MLPrx> temp = (List<MLPrx>) getValue();
                       for (int i = 0; i < temp.size(); i++) {
                         sb.append((i == 0 ? "" : "::") + temp.get(i).getId());
                       }
                       return sb.toString();
                   }
	    };  
	    getFilters().addFilter(lineFilter);
	    getFilters().addFilter(new StringFilter<MSPrx>(propMS.name()));
	    editing.addEditor(ccLine, cbLine);
	    editing.addEditor(ccName, txName);
    }
//---------------------------------------------    
    @Override
    public void mergItem(MSPrx item){
       rcAtm req = null;
       if (isIns) {req = reqIns; isIns = false;}
       else req = f.creRcAtm();
       MSPrx editItem = req.edit(item);
       editItem.setName(txName.getValue().trim());
       editItem.setAtMetroLine(cbLine.getCurrentValue());
       req.merg(editItem).fire(mergReceiver);
    }
    @Override
    public void insItem(){
     	reqIns = f.creRcAtm();
     	MSPrx o = reqIns.create(MSPrx.class); 
     	o.setAtMetroLine(stLine.get(0));
     	o.setName("");
        stT.add(0, o);
    }
    @Override
    public String getItemName(MSPrx item){
	return item.getName();
    }
    @Override
    public void delItem(MSPrx item, Receiver<Void> R){
       f.creRcAtm().removMS(item).fire(R);
    }
    @Override
    protected void beforEdit(){
       txName.getCell().getInputElement(txName.getElement()).setMaxLength(AtMstation.LEN_name);
    }

}
