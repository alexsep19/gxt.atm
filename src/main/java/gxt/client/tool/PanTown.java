package gxt.client.tool;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jpa.atm.F250Town;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.logging.client.ConsoleLogHandler;
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

import gxt.client.domain.F250TownPrx;
import gxt.client.domain.FactAtm;
import gxt.client.domain.OkatoPrx;
import gxt.client.domain.FactAtm.rcAtm;
import gxt.tool.PanExtPage;
import gxt.tool.ShuListFilter;
import gxt.tool.ShuListMenu;

public class PanTown extends PanExtPage<F250TownPrx>{
    private static Logger rootLogger = Logger.getLogger("");

    private static final int PAN_WIDTH = 420;
    private static final int PAN_HEIGHT = 819;
    private static final int GR_PAGE_SIZE = 35;
    ColumnConfig<F250TownPrx, String> ccName;
    ColumnConfig<F250TownPrx, OkatoPrx> ccOkato;
    ColumnConfig<F250TownPrx, String> ccRusName;
    ColumnConfig<F250TownPrx, String> ccCode;
    interface TownProperties extends PropertyAccess<F250TownPrx> {
	    ModelKeyProvider<F250TownPrx> id();
	    ValueProvider<F250TownPrx, String> name();
	    ValueProvider<F250TownPrx, OkatoPrx> atOkato();
	    ValueProvider<F250TownPrx, String> rusName();
	    ValueProvider<F250TownPrx, String> telCode();
	  }
    private static final TownProperties propTown = GWT.create(TownProperties.class);
    interface OkProperties extends PropertyAccess<OkatoPrx> {
	    @Path("id")
	    ModelKeyProvider<OkatoPrx> id();
	    ValueProvider<OkatoPrx, String> name();
	  }
    private static final OkProperties propOk = GWT.create(OkProperties.class);
    private ComboBox<OkatoPrx> cbOk;
    private TextField txName = new TextField();
    private TextField txRusName = new TextField();
    private TextField txCode = new TextField();
    ListStore<OkatoPrx> stOk = new ListStore<OkatoPrx>(propOk.id());
    rcAtm reqIns;
    FactAtm f;

//============================================================    
    public PanTown(FactAtm fct) {
	super(PAN_WIDTH, PAN_HEIGHT, GR_PAGE_SIZE, "Города");
	rootLogger.addHandler(new ConsoleLogHandler());
	f = fct;
	ccName = new ColumnConfig<F250TownPrx, String>(propTown.name(), 40, "City");
	ccOkato = new ColumnConfig<F250TownPrx, OkatoPrx>(propTown.atOkato(), 100, "Регион");
	ccOkato.setCell(new AbstractCell<OkatoPrx>() {
	      @Override
	      public void render(Context context, OkatoPrx value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant(value.getName());
	      } });
	ccRusName = new ColumnConfig<F250TownPrx, String>(propTown.rusName(), 80, "Город");
	ccCode = new ColumnConfig<F250TownPrx, String>(propTown.telCode(), 30, "Тел.");
	getCcL().add(ccName);
	getCcL().add(ccRusName);
	getCcL().add(ccOkato);
	getCcL().add(ccCode);
	setStT(new ListStore<F250TownPrx>(propTown.id()));
	setRfpT(new RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<F250TownPrx>>() {
            @Override
            public void load(FilterPagingLoadConfig loadConfig, Receiver<? super PagingLoadResult<F250TownPrx>> receiver) {
              rcAtm req = f.creRcAtm();
              List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
              List<FilterConfig> filterConfig = createRequestFilterConfig(req, loadConfig.getFilters());
              req.getPageTown(loadConfig.getOffset(), loadConfig.getLimit(), sortInfo, filterConfig).with("data.atOkato").to(receiver).fire();
//              f.creRcAtm().getAllRec(Factory.SQL_ALLOK).fire(new Receiver<List>(){
              f.creRcAtm().getAllOkato().fire(new Receiver<List>(){
		  	  @Override
		  	  public void onSuccess(List response){
		  		if (response != null)  cbOk.getStore().replaceAll((List<OkatoPrx>)response);
		  		else cbOk.clear();
		  	   }});
            }});
//	<OkatoPrx>
        cbOk = new ComboBox<OkatoPrx>(stOk, new LabelProvider<OkatoPrx>(){
            @Override
            public String getLabel(OkatoPrx item) {
              return item==null ? "":item.getName();
            }
        });

	cbOk.setPropertyEditor(new PropertyEditor<OkatoPrx>() {
	          @Override
	          public OkatoPrx parse(CharSequence text) throws ParseException {
	            for(OkatoPrx it: stOk.getAll()){
	        	if (it.getName().equals(text)) return it;
	            }
	            return null;
	          }
	          @Override
	          public String render(OkatoPrx object) {
	            return object == null ? "XXX" : object.getName();
	          }});
	cbOk.setTriggerAction(TriggerAction.ALL);
	cbOk.setForceSelection(true);
        cbOk.setEditable(false); 
	
	initValues();
	ShuListFilter<F250TownPrx, OkatoPrx> OkFilter =	new ShuListFilter<F250TownPrx, OkatoPrx>(new ValueProvider<F250TownPrx, OkatoPrx>(){
	    @Override
	    public OkatoPrx getValue(F250TownPrx object) {
		return object.getAtOkato();
	    }
	    @Override
	    public void setValue(F250TownPrx object, OkatoPrx value) {
		object.setAtOkato(value);
	    }
	    @Override
	    public String getPath() {
		return "atOkato";
	    }} , stOk){
                   @Override
                   protected ShuListMenu<F250TownPrx, OkatoPrx> createListMenu(ListStore<OkatoPrx> store){
                       return new ShuListMenu<F250TownPrx, OkatoPrx>(this, store){
                	   protected void renderItem(CheckMenuItem item, OkatoPrx m){
                	       item.setText(m == null ? "" : m.getName()); 
                	   }
                       };
                   }
                   @Override
                   protected String convertValueToString() {
                       StringBuffer sb = new StringBuffer();
                       List<OkatoPrx> temp = (List<OkatoPrx>) getValue();
                       for (int i = 0; i < temp.size(); i++) {
                         sb.append((i == 0 ? "" : "::") + temp.get(i).getId());
                       }
                       return sb.toString();
                   }
	    };
	 getFilters().addFilter(OkFilter);
	 getFilters().addFilter(new StringFilter<F250TownPrx>(propTown.rusName()));
	 
	 editing.addEditor(ccName, txName);
	 editing.addEditor(ccRusName, txRusName);
	 editing.addEditor(ccOkato, cbOk);
	 editing.addEditor(ccCode, txCode);

    }
    @Override
    public void mergItem(F250TownPrx item){
       rcAtm req = null;
       if (isIns) {req = reqIns; isIns = false;}
       else req = f.creRcAtm();
       F250TownPrx editItem = req.edit(item);
       editItem.setName(txName.getValue().trim());
       editItem.setRusName(txRusName.getValue().trim());
       editItem.setAtOkato(cbOk.getCurrentValue());
       editItem.setTelCode(txCode.getValue().trim());
       editItem.setAlfaBank(txName.getValue().trim());
       req.merg(editItem).fire(mergReceiver);
    }
    @Override
    public void insItem(){
     	reqIns = f.creRcAtm();
     	F250TownPrx o = reqIns.create(F250TownPrx.class); //new ServPrx();
     	o.setAtOkato(stOk.get(0));
     	o.setName("");
     	o.setRusName("");
     	o.setTelCode("");
        stT.add(0, o);
    }
    @Override
    public String getItemName(F250TownPrx item){
	return item.getName();
    }
    @Override
    public void delItem(F250TownPrx item, Receiver<Void> R){
       f.creRcAtm().removTown(item).fire(R);
    }
    @Override
    protected void beforEdit(){
       txName.getCell().getInputElement(txName.getElement()).setMaxLength(F250Town.LEN_name);
       txRusName.getCell().getInputElement(txName.getElement()).setMaxLength(F250Town.LEN_rusName);
       txCode.getCell().getInputElement(txCode.getElement()).setMaxLength(F250Town.LEN_telCode);
    }
}
