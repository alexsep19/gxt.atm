package gxt.client.terms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import gxt.client.domain.FactAtm;
import gxt.client.domain.MSPrx;
import gxt.client.domain.MetroPrx;
import gxt.client.domain.TermStPrx;
import gxt.client.domain.FactAtm.rcAtm;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
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
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
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
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent.BeforeStartEditHandler;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

public class PanMetro extends FramedPanel{
    private static Logger rootLogger = Logger.getLogger("");
    FactAtm f;
    private static final int METRO_WIDTH = 320;
    private static final int METRO_HEIGHT = 150;
    rcAtm reqIns;
    TermStPrx curTerm;
    boolean isIns = false;
    interface MetroProperties extends PropertyAccess<MetroPrx> {
	    @Path("id")
	    ModelKeyProvider<MetroPrx> id();
	    ValueProvider<MetroPrx, MSPrx> atMstation();
	  }
    private static final MetroProperties propMetro = GWT.create(MetroProperties.class);
    interface MSProperties extends PropertyAccess<MSPrx> {
	    @Path("id")
	    ModelKeyProvider<MSPrx> id();
	    ValueProvider<MSPrx, String> name();
	  }
    private static final MSProperties propMS = GWT.create(MSProperties.class);
    ColumnConfig<MetroPrx, MSPrx> cc1;
    protected Grid<MetroPrx> gMetro;
    final ListStore<MetroPrx> stMetro = new ListStore<MetroPrx>(propMetro.id());
    final ListStore<MSPrx> stMS = new ListStore<MSPrx>(propMS.id());
    ComboBox<MSPrx> cbMS;
    final GridEditing<MetroPrx> editing;
    final ToolButton tbIns, tbDel;

//===================================================
    public PanMetro(FactAtm fc, boolean IsEditable) {
       rootLogger.addHandler(new ConsoleLogHandler());
       f = fc;
       cc1 = new ColumnConfig<MetroPrx, MSPrx>(propMetro.atMstation(), 200, "Наименование");
       cc1.setCell(new AbstractCell<MSPrx>() {
	      @Override
	      public void render(Context context, MSPrx value, SafeHtmlBuilder sb) {
		if (value.getAtMetroLine().getColor()!=null && !value.getAtMetroLine().getColor().isEmpty()){  
	          String style = "style='color: " + value.getAtMetroLine().getColor() + ";font-weight:bold'";
	          sb.appendHtmlConstant("<span " + style + " >" + 
	               value.getName()+ "("+value.getAtMetroLine().getName()+")" + "</span>");
		}else
 		  sb.appendHtmlConstant(value.getName()+ "("+value.getAtMetroLine().getName()+")");
//	        String v = number.format(value);
//	        sb.appendHtmlConstant("<span " + style + " qtitle='Change' qtip='" + v + "'>" + v + "</span>");
//		sb.appendHtmlConstant("<span >" + value.getName() + "</span>");
//		sb.appendHtmlConstant(value.getName());
	      }
	    });
       List<ColumnConfig<MetroPrx, ?>> l = new ArrayList<ColumnConfig<MetroPrx, ?>>();
       l.add(cc1);
       ColumnModel<MetroPrx> cmMetro = new ColumnModel<MetroPrx>(l);
       final RequestFactoryProxy<ListLoadConfig, ListLoadResult<MetroPrx>> rfpMetro = 
	       new RequestFactoryProxy<ListLoadConfig, ListLoadResult<MetroPrx>>() {
		@Override
		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<MetroPrx>> receiver) {
		  rcAtm req = f.creRcAtm();
		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
		  if (curTerm != null){
		      req.getMetros(sortInfo, curTerm).with("data.atMstation","data.atMstation.atMetroLine").to(receiver).fire();
		      f.creRcAtm().pageMSByTerm(curTerm).with("atMetroLine").fire(new Receiver<List<MSPrx>>(){
		  	  @Override
		  	  public void onSuccess(List<MSPrx> response){
		  		if (response != null)  {
		  		    cbMS.getStore().replaceAll(response);
		  		}else {
		  		    cbMS.clear();
		  		}
		  	   }});
		  }}};
       final ListLoader<ListLoadConfig, ListLoadResult<MetroPrx>> loaderMetro = new ListLoader<ListLoadConfig, ListLoadResult<MetroPrx>>(rfpMetro);
       loaderMetro.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, MetroPrx, ListLoadResult<MetroPrx>>(stMetro));
       loaderMetro.setRemoteSort(true);
       gMetro = new Grid<MetroPrx>(stMetro, cmMetro) {
           @Override
           protected void onAfterFirstAttach() {
             super.onAfterFirstAttach();
             Scheduler.get().scheduleDeferred(new ScheduledCommand() {
               @Override
               public void execute() {
        	   loaderMetro.load();
               } }); }};
       gMetro.getView().setAutoExpandColumn(cc1);
       gMetro.getView().setForceFit(true);
       gMetro.setLoadMask(true);
       gMetro.setLoader(loaderMetro);
//========// EDITING//
       editing = createGridEditing(gMetro);
       cbMS = new ComboBox<MSPrx>(stMS, new LabelProvider<MSPrx>(){
           @Override
           public String getLabel(MSPrx item) {
             return item==null ? "":item.getName()+ "("+item.getAtMetroLine().getName()+")";
           }});
       cbMS.setPropertyEditor(new PropertyEditor<MSPrx>() {
	   @Override
	   public MSPrx parse(CharSequence text) throws ParseException {
	            for(MSPrx it: stMS.getAll()){
	        	if (it.getName().equals(text)) return it;
	            }
	            return null;
	          }
	   @Override
	   public String render(MSPrx object) {
	            return object == null ? "XXX" : object.getName();
	          }});
	cbMS.setTriggerAction(TriggerAction.ALL);
	cbMS.setForceSelection(true);
	editing.addEditor(cc1, cbMS);
	editing.addBeforeStartEditHandler(new BeforeStartEditHandler<MetroPrx>(){
	    @Override
	    public void onBeforeStartEdit(BeforeStartEditEvent<MetroPrx> event) {
		setEditMode(true);
	    }});
       editing.addCompleteEditHandler(new CompleteEditHandler<MetroPrx>(){
	    @Override
	    public void onCompleteEdit(CompleteEditEvent<MetroPrx> event) {
	       Store<MetroPrx>.Record rec = stMetro.getRecord(stMetro.get(event.getEditCell().getRow()));
	       rcAtm req = null;
	       if (isIns) {req = reqIns; isIns = false;}
	       else {
		   if (!rec.isDirty()) {editing.cancelEditing(); setEditMode(false); return;}
		   req = f.creRcAtm();
	       }
	       MetroPrx editItem = req.edit(rec.getModel());
	       editItem.setAtMstation(cbMS.getCurrentValue());
	       req.merg(editItem).fire(new Receiver<Void>() {
		      public void onSuccess(Void data) {
		         editing.cancelEditing();
			 gMetro.getLoader().load();
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
	       gMetro.getLoader().load();
	    }
       });
       editing.addCancelEditHandler(new CancelEditHandler<MetroPrx>(){
	    @Override
	    public void onCancelEdit(CancelEditEvent<MetroPrx> event) {
		if (isIns){
		  stMetro.remove(0);
	          isIns = false;
		}
	       editing.cancelEditing();
	       setEditMode(false);
	    }
       });
       tbIns= new ToolButton(ToolButton.PLUS, new SelectHandler() {
           @Override  
           public void onSelect(SelectEvent event) {   
       	        isIns = true;
       	        reqIns = f.creRcAtm();
       	        MetroPrx o = reqIns.create(MetroPrx.class); //new ServPrx();
       	        o.setAtMstation(stMS.get(0));
       	        o.setAtTerm(curTerm);
                editing.cancelEditing();
                stMetro.add(0, o);
                editing.startEditing(new GridCell(0, 0));
           }    });
       tbIns.setTitle("Добавить");
       
       tbDel = new ToolButton(ToolButton.MINUS, new SelectHandler() {
           @Override  public void onSelect(SelectEvent event) {   
       	 editing.cancelEditing();
       	 if (gMetro.getSelectionModel().getSelectedItems().size() > 0 ){
       	   final MetroPrx item = gMetro.getSelectionModel().getSelectedItem();
       	   final ConfirmMessageBox messageBox = new ConfirmMessageBox("Удаление", "Удалить " + item.getAtMstation().getName() + "?");
       	messageBox.addDialogHideHandler(new DialogHideHandler() {
			public void onDialogHide(DialogHideEvent event) {
				if (event.getHideButton() == PredefinedButton.YES) {
					f.creRcAtm().removMetro(item).fire(new Receiver<Void>() {
	 	    		  public void onSuccess(Void data) {
	    		      editing.cancelEditing();
	    		      gMetro.getLoader().load();
	    			  }
	    			  public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
	    			     StringBuffer b = new StringBuffer(); 
	    			     for (ConstraintViolation<?> err : violations){
	    			        b.append(err.getPropertyPath() + " : " + err.getMessage());
	    			        }
	    			     AlertMessageBox d = new AlertMessageBox("Ошибка", b.toString());
	    			        d.show();
	    		 		     setEditMode(true);

	    			     }
	    		          public void onFailure(ServerFailure error) {
	    			     AlertMessageBox d = new AlertMessageBox("Ошибка", error.getMessage());
	    			        d.show();
	    			     super.onFailure(error);
	    		 		     setEditMode(true);

	    			     }

				});
			      }}

       	});
//       	   messageBox.addHideHandler(new HideHandler() {
//			@Override
//			public void onHide(HideEvent event) {
//			    Dialog btn = (Dialog) event.getSource();
//	        	    if (btn.getHideButton().getText().equals("Yes")) {
//	 	    	       f.creRcAtm().removMetro(item).fire(new Receiver<Void>() {
//	 	    		  public void onSuccess(Void data) {
//	 	    		      editing.cancelEditing();
//	 	    		      gMetro.getLoader().load();
//	 	    			  }
//	 	    			  public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
//	 	    			     StringBuffer b = new StringBuffer(); 
//	 	    			     for (ConstraintViolation<?> err : violations){
//	 	    			        b.append(err.getPropertyPath() + " : " + err.getMessage());
//	 	    			        }
//	 	    			     AlertMessageBox d = new AlertMessageBox("Ошибка", b.toString());
//	 	    			        d.show();
//	 	    		 		     setEditMode(true);
//
//	 	    			     }
//	 	    		          public void onFailure(ServerFailure error) {
//	 	    			     AlertMessageBox d = new AlertMessageBox("Ошибка", error.getMessage());
//	 	    			        d.show();
//	 	    			     super.onFailure(error);
//	 	    		 		     setEditMode(true);
//
//	 	    			     }
//	 	    			});
//		            }
//			}
//       	      });
       	      messageBox.show();
               }           }    });
       tbDel.setTitle("Удалить");
       
       if (IsEditable){
         getHeader().addTool(tbIns);
         getHeader().addTool(tbDel);
       }
//========
//      getHeader().addStyleName("txt_center");
      addStyleName("margin-10");
      setHeadingText("Метро");
      setPixelSize(METRO_WIDTH, METRO_HEIGHT);
      VerticalLayoutContainer vcon = new VerticalLayoutContainer();
      vcon.setBorders(true);
      vcon.add(gMetro, new VerticalLayoutData(1, 1));
      setWidget(vcon);
    }
    
    public void setNewVals(TermStPrx t, boolean isActive){
	setEnabled(isActive);
	if (t == null) return;
	curTerm = t;
	gMetro.getLoader().load();
    }
    
    private GridEditing<MetroPrx> createGridEditing(Grid<MetroPrx> gMetro) {
	GridRowEditing<MetroPrx> gre = new GridRowEditing<MetroPrx>(gMetro);
	gre.setClicksToEdit(ClicksToEdit.TWO);
	return gre;

    }
    
    private void setEditMode(boolean isEdit){
	tbIns.setVisible(!isEdit);
	tbDel.setVisible(!isEdit);
    }

}
