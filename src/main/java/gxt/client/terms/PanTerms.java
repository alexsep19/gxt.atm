package gxt.client.terms;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import gxt.client.domain.F250TownPrx;
import gxt.client.domain.FactAtm;
import gxt.client.domain.OkatoPrx;
import gxt.client.domain.PartPrx;
import gxt.client.domain.TermStPrx;
import gxt.client.domain.FactAtm.rcAtm;
import gxt.tool.ShuListFilter;
import gxt.tool.ShuListMenu;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar.PagingToolBarMessages;

public class PanTerms extends ContentPanel{
    private static Logger rootLogger = Logger.getLogger("");
    private static final int WIN_WIDTH = 1150;
    private static final int WIN_HEIGHT = 840;
    private static final int TERM_PAGE_SIZE = 35;
    private static final int TERM_WIDTH = 550; //413;
    private static final int TERM_HEIGHT = 808;

    interface TermProxyProperties extends PropertyAccess<TermStPrx> {
	    ModelKeyProvider<TermStPrx> id();
	    ValueProvider<TermStPrx, String> term();
//	    ValueProvider<TermStPrx, String> partName();
	    ValueProvider<TermStPrx, PartPrx> atPart();
	    ValueProvider<TermStPrx, String> finName();
	    ValueProvider<TermStPrx, String> townString();
	    ValueProvider<TermStPrx, OkatoPrx> atOkato();
//	    ValueProvider<TermStPrx, F250TownPrx.getAtOkato()> atOkato();
	  }
    TermProxyProperties props = GWT.create(TermProxyProperties.class);
    interface PartProxyProperties extends PropertyAccess<PartPrx> {
	    ModelKeyProvider<PartPrx> id();
	    ValueProvider<PartPrx, String> name();
    }
    PartProxyProperties propPart = GWT.create(PartProxyProperties.class);
    
    interface OkProxyProperties extends PropertyAccess<OkatoPrx> {
	    ModelKeyProvider<OkatoPrx> id();
	    ValueProvider<OkatoPrx, String> name();
    }
    OkProxyProperties propOk = GWT.create(OkProxyProperties.class);
    
    TermStPrx curTerm;
    String user_fi;
    PanTerms panel = this;
    PanAddr panAddr = null;
    PanServ panServ;
    PanTel panTel;
    PanMetro panMetro;
    Grid<TermStPrx> grTerm;
    ListStore<TermStPrx> store = new ListStore<TermStPrx>(props.id());
    ListStore<PartPrx> stPart = new ListStore<PartPrx>(propPart.id());
    ListStore<OkatoPrx> stOk = new ListStore<OkatoPrx>(propOk.id());
    boolean isRefresh = false;
    ToolButton tbRep;
    
//---------------------------------------------------    
    public PanTerms(final FactAtm fct, String fi){
	rootLogger.addHandler(new ConsoleLogHandler());
//	setCollapsible(false);
	user_fi = fi;
	getHeader().addStyleName("txt_center");
	addStyleName("margin-10");
//	setHeadingText("Терминалы");
	setPixelSize(WIN_WIDTH, WIN_HEIGHT);
	
        fct.creRcAtm().getAllOkato().fire(new Receiver<List>(){
      	  @Override public void onSuccess(List response){
      	  if (response != null) stOk.replaceAll((List<OkatoPrx>)response);
      	  else stPart.clear();
        }});
	    fct.creRcAtm().getAllPart().fire(new Receiver<List>(){
  	    @Override public void onSuccess(List response){
  		if (response != null) stPart.replaceAll((List<PartPrx>)response);
  		else stPart.clear();
       }});
//        <PartPrx>
        RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<TermStPrx>> proxy = 
        	new RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<TermStPrx>>() {
            @Override
            public void load(FilterPagingLoadConfig loadConfig, Receiver<? super PagingLoadResult<TermStPrx>> receiver) {
              rcAtm req = fct.creRcAtm();
              List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
              List<FilterConfig> filterConfig = createRequestFilterConfig(req, loadConfig.getFilters());//,"data.atPart" ,"data.f250Town.atOkato"
              req.getTerms(loadConfig.getOffset(), loadConfig.getLimit(), sortInfo, filterConfig).with("data.f250Town","data.atOkato","data.atPart").to(receiver).fire();
//              rootLogger.log(Level.INFO,  "curTerm.getAtOkato "+ ((TermStPrx) receiver).getAtOkato());
            }};
            
        final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<TermStPrx>> loader = 
        	new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<TermStPrx>>(proxy) {
        	      @Override
        	      protected FilterPagingLoadConfig newLoadConfig() {
        	        return new FilterPagingLoadConfigBean();
        	      }
        	    };
    
     	loader.setRemoteSort(true);
//        store = new ListStore<TermStPrx>(props.id()); 
        loader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, TermStPrx, PagingLoadResult<TermStPrx>>(store));
        final PagingToolBar toolBar = new PagingToolBar(TERM_PAGE_SIZE);
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");
//        toolBar.setToolTip("XXX");
//        toolBar.setEnableOverflow(false);
//        PagingToolBarMessages toolBarMessages = toolBar.getMessages();
//        PagingToolBarMessages toolBarMessages = new PagingToolBarMessages(){
//            @Override
//                 public String  displayMessage(int start, int end, int total) {
//                   return " zz "+start+ "ff "+ end+" ggg "+ total;
//                 } 
//        };
//        toolBar.setMessages(toolBarMessages);
        toolBar.bind(loader);
        ColumnConfig<TermStPrx, String> colTerm = new ColumnConfig<TermStPrx, String>(props.term(), 40, "Терминал");
        ColumnConfig<TermStPrx, PartPrx> colPart = new ColumnConfig<TermStPrx, PartPrx>(props.atPart(), 40, "Партнер");
//        ColumnConfig<TermPrx, PartPrx> colPart = new ColumnConfig<TermPrx, PartPrx>(props.atPart(), 70, "Партнер");
        colPart.setCell(new AbstractCell<PartPrx>() {
	    @Override
	    public void render(com.google.gwt.cell.client.Cell.Context context, PartPrx value, SafeHtmlBuilder sb) {
	              sb.appendHtmlConstant(value.getName());
	    }});
        ColumnConfig<TermStPrx, String> colFil = new ColumnConfig<TermStPrx, String>(props.finName(), 80, "Филиал");
        ColumnConfig<TermStPrx, String> colTown = new ColumnConfig<TermStPrx, String>(props.townString(), 80, "Город");
//        ColumnConfig<TermStPrx, F250TownPrx> colTown = new ColumnConfig<TermStPrx, F250TownPrx>(props.f250Town(), 80, "Город");
//        colTown.setCell(new AbstractCell<F250TownPrx>() {
//    	    @Override
//    	    public void render(com.google.gwt.cell.client.Cell.Context context, F250TownPrx value, SafeHtmlBuilder sb) {
//    	              sb.appendHtmlConstant(value.getRusName());
//        }});
        ColumnConfig<TermStPrx, OkatoPrx> colOk = new ColumnConfig<TermStPrx, OkatoPrx>(props.atOkato(), 80, "Регион");
        colOk.setCell(new AbstractCell<OkatoPrx>() {
    	    @Override
    	    public void render(com.google.gwt.cell.client.Cell.Context context,OkatoPrx value, SafeHtmlBuilder sb) {
    	           sb.appendHtmlConstant(value.getName());//getAtOkato().
        }});
        List<ColumnConfig<TermStPrx, ?>> l = new ArrayList<ColumnConfig<TermStPrx, ?>>();
        l.add(colTerm);
        l.add(colPart);
        l.add(colFil);
        l.add(colOk);
        l.add(colTown);
        ColumnModel<TermStPrx> cm = new ColumnModel<TermStPrx>(l);
        panAddr = new PanAddr(fct, isAtmEditable(), this);
        panAddr.setVisible(false);
        panServ = new PanServ(fct, isAtmEditable());
        panServ.setVisible(false);
        panTel = new PanTel(fct, isAtmEditable());
        panTel.setVisible(false);
        panMetro = new PanMetro(fct, isAtmEditable());
        panMetro.setVisible(false);
        grTerm = new Grid<TermStPrx>(store, cm) {
            @Override
            public void onLoaderBeforeLoad() {
//        	rootLogger.log(Level.INFO,  "onLoad ");
        	if (!isRefresh){
        	  panel.setHeadingText("АТМ не выбран");
        	  panAddr.setVisible(false);
        	  panServ.setVisible(false);
        	  panTel.setVisible(false);
        	  panMetro.setVisible(false);
        	}else isRefresh = false;
            }
            @Override
            protected void onAfterFirstAttach() {
              super.onAfterFirstAttach();
              Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                  loader.load();
                }
              }); }};
        grTerm.getView().setForceFit(true);
        grTerm.setLoadMask(true);
        grTerm.setLoader(loader);

        GridFilters<TermStPrx> filters = new GridFilters<TermStPrx>(loader);
        final ShuListFilter<TermStPrx, PartPrx> filterPart = new ShuListFilter<TermStPrx, PartPrx>(new ValueProvider<TermStPrx, PartPrx>(){
	    @Override
	    public PartPrx getValue(TermStPrx object) {
		return object.getAtPart();
	    }
	    @Override
	    public void setValue(TermStPrx object, PartPrx value) {
		object.setAtPart(value);
	    }
	    @Override
	    public String getPath() {
		return "atPart";
	    }} , stPart){
                   @Override
                   protected ShuListMenu<TermStPrx, PartPrx> createListMenu(ListStore<PartPrx> store){
                       return new ShuListMenu<TermStPrx, PartPrx>(this, store){
                	   protected void renderItem(CheckMenuItem item, PartPrx m){
                	       item.setText(m == null ? "" : m.getName()); 
                	   }
                       };
                   }
                   @Override
                   protected String convertValueToString() {
                       StringBuffer sb = new StringBuffer();
                       List<PartPrx> temp = (List<PartPrx>) getValue();
                       for (int i = 0; i < temp.size(); i++) {
                         sb.append((i == 0 ? "" : "::") + temp.get(i).getId());
                       }
                       return sb.toString();
                   }  };  
         final ShuListFilter<TermStPrx, OkatoPrx> filterOk = new ShuListFilter<TermStPrx, OkatoPrx>(new ValueProvider<TermStPrx, OkatoPrx>(){
               	    @Override
               	    public OkatoPrx getValue(TermStPrx object) {
               		return object.getAtOkato();
               	    }
               	    @Override
               	    public void setValue(TermStPrx object, OkatoPrx value) {
               		object.setAtOkato(value);
               	    }
               	    @Override
               	    public String getPath() {
               		return "atOkato";
               	    }} , stOk){
                        @Override
                        protected ShuListMenu<TermStPrx, OkatoPrx> createListMenu(ListStore<OkatoPrx> store){
                            return new ShuListMenu<TermStPrx, OkatoPrx>(this, store){
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
                        } };
         
        filters.initPlugin(grTerm);
        filters.setLocal(false);//be sure to be remote, or it will affect the local cached data only
        filters.addFilter(new StringFilter<TermStPrx>(props.term()));
        filters.addFilter(filterPart);
        filters.addFilter(filterOk);
        filters.addFilter(new StringFilter<TermStPrx>(props.townString()));

        FramedPanel panGrid = new FramedPanel();
        panGrid.setHeadingText("Банкоматы");
        panGrid.setPixelSize(TERM_WIDTH, TERM_HEIGHT);
	tbRep = new ToolButton(ToolButton.PRINT, new SelectHandler() {
            @Override  public void onSelect(SelectEvent event) {
//        	tbRep.setVisible(false);
  	        Window.open(GWT.getModuleBaseURL() + "jasperin", "_self", "disabled"); //"enabled"
//  	        tbRep.setVisible(true);
               }    });
	tbRep.setTitle("Список Атм");
	panGrid.getHeader().addTool(tbRep);

//        cp.addStyleName("margin-10");
        
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);
        con.add(grTerm, new VerticalLayoutData(1, 1));
        con.add(toolBar, new VerticalLayoutData(1, -1));
        panGrid.setWidget(con);
        HtmlLayoutContainer contMain = new HtmlLayoutContainer(getMainMarkup());
        contMain.add(panGrid, new HtmlData(".terms"));
        contMain.add(panAddr, new HtmlData(".addr"));
        contMain.add(panServ, new HtmlData(".serv"));
        contMain.add(panTel, new HtmlData(".tel"));
        contMain.add(panMetro, new HtmlData(".metro"));
//        topCont.add(panGrid, new HorizontalLayoutData(-1, 1, new Margins(3)));
//        topCont.add(panAddr, new HorizontalLayoutData(-1, -1, new Margins(3)));
//        topCont.add(panServ, new HorizontalLayoutData(1, -1, new Margins(3)));
        
        grTerm.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<TermStPrx>(){
  	    @Override
 	    public void onSelectionChanged(SelectionChangedEvent<TermStPrx> event) {
  		if (event.getSelection().size() <= 0) return;
  		curTerm = event.getSource().getSelectedItem();
  		panel.setHeadingText("АТМ "+ curTerm.getTerm()+", статус "+(curTerm.getStatus().equals("A")?"работает":"отключен"));
  		panAddr.setNewVals(curTerm);
  		panServ.setNewVals(curTerm);
  		panTel.setNewVals(curTerm);
//  		panMetro.setVisible(false);

  		fct.creRcAtm().mertoInTown(curTerm).with("f250Town").fire(new Receiver<Boolean>() {
  		    public void onSuccess(Boolean t) {
			panMetro.setNewVals(curTerm, t);
  	  		panMetro.setVisible(true);
  	  		panAddr.setVisible(true);
  	  		panServ.setVisible(true);	
  	  		panTel.setVisible(true);
                    }
  	            public void onFailure(ServerFailure error) {
  		        super.onFailure(error);
  		        } } );
    	     }}  );

        setWidget(contMain);
    }

    public void gridRefresh(){
	isRefresh = true;
	grTerm.getLoader().load();
//	store.update(curTerm);
//        grTerm.getView().refresh(false);
    }
    
    private boolean isAtmEditable(){
	return (user_fi.equals("*"))||(user_fi.indexOf(curTerm.getFin()) >= 0);
    }

    private native String getMainMarkup() /*-{
    return [ '<table cellpadding=0 cellspacing=4 cols="3">',
        '<tr><td class=terms rowspan=3 valign="top"></td><td class=addr colspan=2 valign="top"></td></tr>',
        '<tr><td class=serv rowspan=2 valign="top"></td><td class=metro valign="top" align="left"></td></tr>',
        '<tr><td class=tel valign="top" align="left"></td></tr>',
//        '<tr><td class=terms rowspan=2 valign="top"></td><td class=addr colspan=2 valign="top"></td><td class=serv rowspan=2 valign="top"></td></tr>',
//        '<tr><td class=tel valign="top"></td><td class=metro valign="top"></td></tr>',
        '</table>'
    ].join("");
  }-*/;

}
