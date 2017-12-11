package gxt.client.terms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import gxt.client.domain.CatalogPrx;
import gxt.client.domain.FactAtm;
import gxt.client.domain.ServPrx;
import gxt.client.domain.TermStPrx;
import gxt.client.domain.FactAtm.rcAtm;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.logging.client.ConsoleLogHandler;
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
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.Record;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.ListReader;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent.BeforeStartEditHandler;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

public class PanServ extends FramedPanel{
    private static Logger rootLogger = Logger.getLogger("");
    private static final int SERV_WIDTH = 200;
    private static final int SERV_HEIGHT = 319;
    FactAtm f;
    rcAtm reqIns;
    TermStPrx curTerm;
    boolean isIns = false;
//    private final boolean isEditable;
    protected Grid<ServPrx> gServ;
    final ListStore<ServPrx> stServ = new ListStore<ServPrx>(propServ.id());
    final ListStore<CatalogPrx> stCat = new ListStore<CatalogPrx>(propCat.id());
    ColumnConfig<ServPrx, CatalogPrx> cc1;
    final GridEditing<ServPrx> editing;
    final ToolButton tbIns;
    final ToolButton tbDel;
    
    interface ServProperties extends PropertyAccess<ServPrx> {
	    @Path("id")
	    ModelKeyProvider<ServPrx> id();
	    ValueProvider<ServPrx, CatalogPrx> atCatalog();
//	    ValueProvider<ServPrx, TermStPrx> atTerm();
	  }
    private static final ServProperties propServ = GWT.create(ServProperties.class);
    interface CatProperties extends PropertyAccess<CatalogPrx> {
	    @Path("id")
	    ModelKeyProvider<CatalogPrx> id();
	    ValueProvider<CatalogPrx, String> name();
	  }
private static final CatProperties propCat = GWT.create(CatProperties.class);
    ComboBox<CatalogPrx> cbCat;
//    rcAtm reqForEdit;
    
    public Receiver<Void> delReceiver = new Receiver<Void>() {
	  public void onSuccess(Void data) {
	      editing.cancelEditing();
	      gServ.getLoader().load();
		  }
		  public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
		     StringBuffer b = new StringBuffer(); 
		     for (ConstraintViolation<?> err : violations){
		        b.append(err.getPropertyPath() + " : " + err.getMessage());
		        }
		     AlertMessageBox d = new AlertMessageBox("Ошибка", b.toString());
//		        d.addHideHandler(hideHandler);
		        d.show();
	 		     setEditMode(true);

		     }
	          public void onFailure(ServerFailure error) {
//	             ShowError(ServerErr.getMess(error.getMessage()), true);
		     AlertMessageBox d = new AlertMessageBox("Ошибка", error.getMessage());
//		        d.addHideHandler(hideHandler);
		        d.show();
		     super.onFailure(error);
	 		     setEditMode(true);

		     }
		};
//-------------------------------------------------------------    
    public PanServ(FactAtm fc, boolean IsEditable) {
       rootLogger.addHandler(new ConsoleLogHandler());
       f = fc;
//       isEditable = IsEditable;
       
         cc1 = new ColumnConfig<ServPrx, CatalogPrx>(propServ.atCatalog(), 100, "Наименование");
//       cc1 = new ColumnConfig<ServPrx, CatalogPrx>(new ValueProvider<ServPrx, CatalogPrx>(){
//	    @Override
//	    public CatalogPrx getValue(ServPrx object) {
//		return object.getAtCatalog();
//	    }
//	    @Override
//	    public void setValue(ServPrx object, CatalogPrx value) {
//              object.setAtCatalog(value);
//	    }
//	    @Override
//	    public String getPath() {return "name";}}, 100, "Наименование");
       cc1.setCell(new AbstractCell<CatalogPrx>() {
	      @Override
	      public void render(Context context, CatalogPrx value, SafeHtmlBuilder sb) {
//	        String style = "style='color: " + (value < 0 ? "red" : "green") + "'";
//	        String v = number.format(value);
//	        sb.appendHtmlConstant("<span " + style + " qtitle='Change' qtip='" + v + "'>" + v + "</span>");
//		sb.appendHtmlConstant("<span >" + value.getName() + "</span>");
		sb.appendHtmlConstant(value.getName());
	      }
	    });
       List<ColumnConfig<ServPrx, ?>> l = new ArrayList<ColumnConfig<ServPrx, ?>>();
       l.add(cc1);
       ColumnModel<ServPrx> cmServ = new ColumnModel<ServPrx>(l);
       
       final RequestFactoryProxy<ListLoadConfig, ListLoadResult<ServPrx>> rfpServ = 
	       new RequestFactoryProxy<ListLoadConfig, ListLoadResult<ServPrx>>() {
		@Override
		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<ServPrx>> receiver) {
rootLogger.log(Level.INFO,  "load");
		  rcAtm req = f.creRcAtm();
		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
//		  if (sortInfo.size() > 0){
//// rootLogger.log(Level.INFO,  "sortInfo = " + sortInfo.get(0).getSortField());
//      
//		  }
		  if (curTerm != null){
		      req.getServs(sortInfo, curTerm).with("data.atCatalog").to(receiver).fire();
//rootLogger.log(Level.INFO,  "getServs");
//		      RefreshCat();
		      f.creRcAtm().pageCatByType("U", curTerm).fire(new Receiver<List<CatalogPrx>>(){
		  	  @Override
		  	  public void onSuccess(List<CatalogPrx> response){
		  		if (response != null)  cbCat.getStore().replaceAll(response);
		  		else cbCat.clear();
		  	   }});
//		      rootLogger.log(Level.INFO,  "pageCatByType");
		  }
		}};
       final ListLoader<ListLoadConfig, ListLoadResult<ServPrx>> loaderServ = new ListLoader<ListLoadConfig, ListLoadResult<ServPrx>>(rfpServ);
       loaderServ.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, ServPrx, ListLoadResult<ServPrx>>(stServ));
       loaderServ.setRemoteSort(true);
       gServ = new Grid<ServPrx>(stServ, cmServ) {
           @Override
           protected void onAfterFirstAttach() {
             super.onAfterFirstAttach();
             Scheduler.get().scheduleDeferred(new ScheduledCommand() {
               @Override
               public void execute() {
        	   loaderServ.load();
               }
             }); }};
       gServ.getView().setAutoExpandColumn(cc1);
       gServ.getView().setForceFit(true);
       gServ.setLoadMask(true);
       gServ.setLoader(loaderServ);
//========// EDITING//
//rootLogger.log(Level.INFO,  "EDITING");
        editing = createGridEditing(gServ);
	cbCat = new ComboBox<CatalogPrx>(stCat, new LabelProvider<CatalogPrx>(){
            @Override
            public String getLabel(CatalogPrx item) {
              return item==null ? "":item.getName();
            }
        });
	cbCat.setPropertyEditor(new PropertyEditor<CatalogPrx>() {
	          @Override
	          public CatalogPrx parse(CharSequence text) throws ParseException {
	            for(CatalogPrx it: stCat.getAll()){
	        	if (it.getName().equals(text)) return it;
	            }
	            return null;
	          }
	          @Override
	          public String render(CatalogPrx object) {
	            return object == null ? "XXX" : object.getName();
	          }

	        });
	cbCat.setTriggerAction(TriggerAction.ALL);
	cbCat.setForceSelection(true);
	editing.addEditor(cc1, cbCat);
	editing.addBeforeStartEditHandler(new BeforeStartEditHandler<ServPrx>(){
	    @Override
	    public void onBeforeStartEdit(BeforeStartEditEvent<ServPrx> event) {
		setEditMode(true);
	    }});
        editing.addCompleteEditHandler(new CompleteEditHandler<ServPrx>(){
	    @Override
	    public void onCompleteEdit(CompleteEditEvent<ServPrx> event) {
	       rootLogger.log(Level.INFO,  "onCompleteEdit");
//	       GridCell cell = event.getEditCell();
//             int row = cell.getRow();
////             int col = cell.getCol();
//	       if (!isIns){
//		     GridCell cell = event.getEditCell();
//	             int row = cell.getRow();
////		 Store<ServPrx>.Record rec = stServ.getRecord(stServ.get(event.getEditCell().getRow()));
////                 ServPrx o = f.creRcAtm().edit(rec.getModel());
//	             ServPrx o = null;
//                 for (Record r : stServ.getModifiedRecords()) {
//rootLogger.log(Level.INFO,  "Modified = "+((ServPrx)r.getModel()).getAtCatalog().getName());
//                     o = f.creRcAtm().create(ServPrx.class); 
//                     o.setAtCatalog(cbCat.getCurrentValue());
//                     o.setAtTerm(curTerm);
////                     o = f.creRcAtm().edit((ServPrx)r.getModel());
////                     o.setAtCatalog(cbCat.getCurrentValue());
//rootLogger.log(Level.INFO,  "new Modified = "+o.getAtCatalog().getName());
////                     r.revert();
////                     stServ.update(o);
////                     stServ.commitChanges();
//                 }
//                 stServ.remove(row);
// 	         stServ.commitChanges(); 
//                 stServ.add(row, o);
//// 	         stServ.commitChanges(); 
////                 stServ.getModifiedRecords().clear();
//                 stServ.fireEvent(new StoreUpdateEvent<ServPrx>(Collections.singletonList(o)));
//
////                 stServ.update(o);
////                 stServ.fireEvent(new StoreUpdateEvent<ServPrx>(Arrays.asList(o)));
////                 rec.addChange(cc1.getValueProvider(), cbCat.getCurrentValue());
////                 rec.commit(true);
//	       }else{
////	       if (isIns) isIns = false;
//	        stServ.commitChanges(); 
//	       }
	       Store<ServPrx>.Record rec = stServ.getRecord(stServ.get(event.getEditCell().getRow()));
	       
	       rcAtm req = null;
	       if (isIns) {req = reqIns; isIns = false;}
	       else {
		   if (!rec.isDirty()) {editing.cancelEditing(); setEditMode(false); return;}
		   req = f.creRcAtm();
	       }
	       ServPrx editItem = req.edit(rec.getModel());
	       editItem.setAtCatalog(cbCat.getCurrentValue());
	       req.merg(editItem).fire(new Receiver<Void>() {
		      public void onSuccess(Void data) {
		         editing.cancelEditing();
			 gServ.getLoader().load();
			 setEditMode(false);
		       }
	              public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
		                StringBuffer b = new StringBuffer(); 
		        	for (ConstraintViolation<?> err : violations){
		        	  b.append(err.getPropertyPath() + " : " + err.getMessage());
		        	}
		           AlertMessageBox d = new AlertMessageBox("Ошибка", b.toString());
				        d.show();
		        }
	               public void onFailure(ServerFailure error) {
		           AlertMessageBox d = new AlertMessageBox("Ошибка", error.toString());
		           d.show();
		           super.onFailure(error);
		        }
		      });
	       gServ.getLoader().load();
	    }
        });
        editing.addCancelEditHandler(new CancelEditHandler<ServPrx>(){
	    @Override
	    public void onCancelEdit(CancelEditEvent<ServPrx> event) {
		if (isIns){
		  stServ.remove(0);
	          isIns = false;
		}
	       editing.cancelEditing();
	       setEditMode(false);
//	     rootLogger.log(Level.INFO,  "Cancel store.size() = " + store.size());
	    }
        });
        tbIns= new ToolButton(ToolButton.PLUS, new SelectHandler() {
            @Override  public void onSelect(SelectEvent event) {   
        	isIns = true;
        	reqIns = f.creRcAtm();
        	ServPrx o = reqIns.create(ServPrx.class); //new ServPrx();
//        	ServPrx o = reqForEdit.create(ServPrx.class);
        	o.setAtCatalog(stCat.get(0));
        	o.setAtTerm(curTerm);
                editing.cancelEditing();
//                setEditMode(true);
                stServ.add(0, o);
                editing.startEditing(new GridCell(0, 0));
            }    });
        tbIns.setTitle("Добавить");
        tbDel = new ToolButton(ToolButton.MINUS, new SelectHandler() {
            @Override  public void onSelect(SelectEvent event) {   
        	 editing.cancelEditing();
        	 if (gServ.getSelectionModel().getSelectedItems().size() > 0 ){
        	   final ServPrx item = gServ.getSelectionModel().getSelectedItem();
        	   final ConfirmMessageBox messageBox = new ConfirmMessageBox("Удаление", "Удалить " + item.getAtCatalog().getName() + "?");
         	   messageBox.addDialogHideHandler(new DialogHideHandler() {
				public void onDialogHide(DialogHideEvent event) {
					if (event.getHideButton() == PredefinedButton.YES) f.creRcAtm().removServ(item).fire(delReceiver);;
				      }
         	      });

//        	   messageBox.addHideHandler(new HideHandler() {
//			@Override
//			public void onHide(HideEvent event) {
//			    Dialog btn = (Dialog) event.getSource();
//	        	    if (btn.getHideButton().getText().equals("Yes")) {
//	 	    	       f.creRcAtm().removServ(item).fire(delReceiver);
//		            }
//			}
//        	      });
        	      messageBox.show();
                }
            }    });
        tbDel.setTitle("Удалить");
        if (IsEditable){
          getHeader().addTool(tbIns);
          getHeader().addTool(tbDel);
        }
//========
//       getHeader().addStyleName("txt_center");
       addStyleName("margin-10");
       setHeadingText("Сервисы");
       setPixelSize(SERV_WIDTH, SERV_HEIGHT);
       VerticalLayoutContainer vcon = new VerticalLayoutContainer();
       vcon.setBorders(true);
       vcon.add(gServ, new VerticalLayoutData(1, 1));
       setWidget(vcon);

     }

    private GridEditing<ServPrx> createGridEditing(Grid<ServPrx> gServ) {
	GridRowEditing<ServPrx> gre = new GridRowEditing<ServPrx>(gServ);
	gre.setClicksToEdit(ClicksToEdit.TWO);
	return gre;

    }
    
    private void setEditMode(boolean isEdit){
	tbIns.setVisible(!isEdit);
	tbDel.setVisible(!isEdit);
    }

    public void setNewVals(TermStPrx t){
//	rootLogger.log(Level.INFO,  "Serv setNewVals");
	if (t == null) return;
	curTerm = t;
        gServ.getLoader().load();
    }
}
