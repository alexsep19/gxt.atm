package gxt.client.terms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import jpa.atm.AtTel;

import gxt.client.domain.FactAtm;
import gxt.client.domain.TelPrx;
import gxt.client.domain.TermStPrx;
import gxt.client.domain.FactAtm.rcAtm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.logging.client.ConsoleLogHandler;
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
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.error.TitleErrorHandler;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

public class PanTel extends FramedPanel{
    private static Logger rootLogger = Logger.getLogger("");
    private static final int TEL_WIDTH = 120;
    private static final int TEL_HEIGHT = 143;
    FactAtm f;
    TermStPrx curTerm;
    ColumnConfig<TelPrx, String> cc1;
    boolean isIns = false;
    rcAtm reqIns;
    String maskTel = "^[0-9]{3}-[0-9]{1,3}-[0-9]{2}-[0-9]{2}$";
    String maskErr = "шаблон 999-9..999-99-99";
    protected Grid<TelPrx> gTel;
    final GridEditing<TelPrx> editing;
    final ListStore<TelPrx> stTel = new ListStore<TelPrx>(propTel.id());
    interface TelProp extends PropertyAccess<TelPrx> {
	    @Path("id")
	    ModelKeyProvider<TelPrx> id();
	    ValueProvider<TelPrx, String> num();
	  }
    private static final TelProp propTel = GWT.create(TelProp.class);
    final ToolButton tbIns;
    final ToolButton tbDel;
    TextField txNum = new TextField();

    public PanTel(FactAtm fc, boolean IsEditable) {
	rootLogger.addHandler(new ConsoleLogHandler());
      f = fc;
      cc1 = new ColumnConfig<TelPrx, String>(propTel.num(), 100, "Номер");
      List<ColumnConfig<TelPrx, ?>> l = new ArrayList<ColumnConfig<TelPrx, ?>>();
      l.add(cc1);
      ColumnModel<TelPrx> cmTel = new ColumnModel<TelPrx>(l);
      final RequestFactoryProxy<ListLoadConfig, ListLoadResult<TelPrx>> rfpTel = 
	       new RequestFactoryProxy<ListLoadConfig, ListLoadResult<TelPrx>>() {
		@Override
		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<TelPrx>> receiver) {
//rootLogger.log(Level.INFO,  "load");
		  rcAtm req = f.creRcAtm();
		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
//		  if (sortInfo.size() > 0){
////rootLogger.log(Level.INFO,  "sortInfo = " + sortInfo.get(0).getSortField());
//     
//		  }
		  if (curTerm != null){
		      req.getTels(sortInfo, curTerm).to(receiver).fire();
		  }
		}};
      final ListLoader<ListLoadConfig, ListLoadResult<TelPrx>> loaderTel = new ListLoader<ListLoadConfig, ListLoadResult<TelPrx>>(rfpTel);
      loaderTel.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, TelPrx, ListLoadResult<TelPrx>>(stTel));
      gTel = new Grid<TelPrx>(stTel, cmTel) {
		 @Override
		 protected void onAfterFirstAttach() {
		      super.onAfterFirstAttach();
		      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		      @Override
		      public void execute() {
		      	   loaderTel.load();
		           }     }); }};
      gTel.getView().setAutoExpandColumn(cc1);
      gTel.getView().setForceFit(true);
      gTel.setLoadMask(true);
      gTel.setLoader(loaderTel);
    //========// EDITING//
      editing = createGridEditing(gTel);
      txNum.addValidator(new RegExValidator( maskTel, maskErr));
      txNum.setErrorSupport(new TitleErrorHandler(txNum));
      txNum.setTitle(maskErr);
      txNum.getCell().getInputElement(txNum.getElement()).setMaxLength(AtTel.LEN_num);
      editing.addEditor(cc1, txNum);
      editing.addBeforeStartEditHandler(new BeforeStartEditHandler<TelPrx>(){
    	    @Override
    	    public void onBeforeStartEdit(BeforeStartEditEvent<TelPrx> event) {
    		txNum.setValue(stTel.getRecord(stTel.get(event.getEditCell().getRow())).getModel().getNum());
    		rootLogger.log(Level.INFO,  "Before txNum.getValue() = "+txNum.getValue());
    		setEditMode(true);
    	    }});
      
      editing.addCompleteEditHandler(new CompleteEditHandler<TelPrx>(){
    	    @Override
    	    public void onCompleteEdit(CompleteEditEvent<TelPrx> event) {
    	       Store<TelPrx>.Record rec = stTel.getRecord(stTel.get(event.getEditCell().getRow()));
    	       rcAtm req = null;
    	       if (isIns) {req = reqIns; isIns = false;}
    	       else {
    		   if (!rec.isDirty()) {editing.cancelEditing(); setEditMode(false); return;}
    		   req = f.creRcAtm();
    	       }
    	       TelPrx editItem = req.edit(rec.getModel());
//rootLogger.log(Level.INFO,  "txNum.getValue() = "+txNum.getValue());
    	       editItem.setNum(txNum.getValue().trim());
    	       req.merg(editItem).fire(new Receiver<Void>() {
    		      public void onSuccess(Void data) {
    		         editing.cancelEditing();
    			 gTel.getLoader().load();
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
    	       gTel.getLoader().load();
    	    }
            });
            editing.addCancelEditHandler(new CancelEditHandler<TelPrx>(){
    	    @Override
    	    public void onCancelEdit(CancelEditEvent<TelPrx> event) {
    		if (isIns){
    		  stTel.remove(0);
    	          isIns = false;
    		}
    	       editing.cancelEditing();
    	       setEditMode(false);
//    	     rootLogger.log(Level.INFO,  "Cancel store.size() = " + store.size());
    	    }
            });
            tbIns= new ToolButton(ToolButton.PLUS, new SelectHandler() {
                @Override  public void onSelect(SelectEvent event) {   
            	isIns = true;
            	reqIns = f.creRcAtm();
            	TelPrx o = reqIns.create(TelPrx.class); //new ServPrx();
rootLogger.log(Level.INFO,  "getTelCode() = "+curTerm.getF250Town().getTelCode());
            	o.setNum(curTerm.getF250Town().getTelCode()+"-");
            	o.setAtTerm(curTerm);
                editing.cancelEditing();
                stTel.add(0, o);
                editing.startEditing(new GridCell(0, 0));
                }    });
            tbIns.setTitle("Добавить");
            tbDel = new ToolButton(ToolButton.MINUS, new SelectHandler() {
                @Override  public void onSelect(SelectEvent event) {   
            	 editing.cancelEditing();
            	 if (gTel.getSelectionModel().getSelectedItems().size() > 0 ){
            	   final TelPrx item = gTel.getSelectionModel().getSelectedItem();
            	   final ConfirmMessageBox messageBox = new ConfirmMessageBox("Удаление", "Удалить " + item.getNum() + "?");
             	   messageBox.addDialogHideHandler(new DialogHideHandler() {
					public void onDialogHide(DialogHideEvent event) {
						if (event.getHideButton() == PredefinedButton.YES) 
	    	 	    	   f.creRcAtm().removTel(item).fire(new Receiver<Void>() {
		    	 	    	  public void onSuccess(Void data) {
		    	 		      editing.cancelEditing();
		    	 		      gTel.getLoader().load();
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

					      }
             	      });

//            	   messageBox.addHideHandler(new HideHandler() {
//    			@Override
//    			public void onHide(HideEvent event) {
//    			    Dialog btn = (Dialog) event.getSource();
//    	        	    if (btn.getHideButton().getText().equals("Yes")) {
//    	 	    	       f.creRcAtm().removTel(item).fire(new Receiver<Void>() {
//    	 	    	  public void onSuccess(Void data) {
//    	 		      editing.cancelEditing();
//    	 		      gTel.getLoader().load();
//    	 			  }
//    	 		  public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
//    	 		     StringBuffer b = new StringBuffer(); 
//    	 		     for (ConstraintViolation<?> err : violations){
//    	 		        b.append(err.getPropertyPath() + " : " + err.getMessage());
//    	 		        }
//    	 		     AlertMessageBox d = new AlertMessageBox("Ошибка", b.toString());
//    	 		     d.show();
//    	 		     setEditMode(true);
//    	 	           }
//    	 		   public void onFailure(ServerFailure error) {
//    	 		     AlertMessageBox d = new AlertMessageBox("Ошибка", error.getMessage());
//    	 		     d.show();
//    	 		     super.onFailure(error);
//    	 		     setEditMode(true);
//    	 		     }
//    	 			});
//    		            }
//    			}
//            	      });
            	      messageBox.show();
                    }
                }    });
            tbDel.setTitle("Удалить");
            if (IsEditable){
              getHeader().addTool(tbIns);
              getHeader().addTool(tbDel);
            }
    //========
//           getHeader().addStyleName("txt_center");
           addStyleName("margin-10");
           setHeadingText("Телефоны");
           setPixelSize(TEL_WIDTH, TEL_HEIGHT);
           VerticalLayoutContainer vcon = new VerticalLayoutContainer();
           vcon.setBorders(true);
           vcon.add(gTel, new VerticalLayoutData(1, 1));
           setWidget(vcon);
    }

    public void setNewVals(TermStPrx t){
//	rootLogger.log(Level.INFO,  "Serv setNewVals");
	if (t == null) return;
	curTerm = t;
        gTel.getLoader().load();
    }
    private void setEditMode(boolean isEdit){
	tbIns.setVisible(!isEdit);
	tbDel.setVisible(!isEdit);
    }
    private GridEditing<TelPrx> createGridEditing(Grid<TelPrx> gTel) {
	GridRowEditing<TelPrx> gre = new GridRowEditing<TelPrx>(gTel);
	gre.setClicksToEdit(ClicksToEdit.TWO);
	return gre;

    }
}
