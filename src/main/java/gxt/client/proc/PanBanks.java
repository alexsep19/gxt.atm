package gxt.client.proc;

import java.util.List;

import gxt.client.domain.F250TownPrx;
import gxt.client.domain.FactAtm;
import gxt.client.domain.PartPrx;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Hidden;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.event.SubmitEvent;
import com.sencha.gxt.widget.core.client.event.SubmitEvent.SubmitHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
//import com.google.gwt.user.client.ui.FormPanel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class PanBanks extends FramedPanel{
	FactAtm fct;
	private static final int PAN_WIDTH = 460;
	FormPanel panFile;
    ContentPanel pnImp = new ContentPanel();
    TextArea taLog = new TextArea();
    final FileUploadField file;
    Hidden hh = new Hidden();
    
    interface PartProperties extends PropertyAccess<PartPrx> {
	    @Path("id")
	    ModelKeyProvider<PartPrx> id();
	    ValueProvider<PartPrx, String> name();
	  }
    private static final PartProperties propPart = GWT.create(PartProperties.class);
    ComboBox<PartPrx> cbPart;
    final ListStore<PartPrx> stPart = new ListStore<PartPrx>(propPart.id());
    
	ToolButton btImp = new ToolButton(ToolButton.GEAR, new SelectHandler() {
        @Override public void onSelect(SelectEvent event) {
          	panFile.submit(); 
        }});
//-----------------------------------------------        	
	public PanBanks(FactAtm fc) {
	   ToolButton btHelp = new ToolButton(ToolButton.QUESTION);
	   fct = fc;
	   addStyleName("margin-10");
	   setHeadingText("Импорт партнерских атм");
//		   setPixelSize(PAN_WIDTH, PAN_HEIGHT);
	   setWidth(PAN_WIDTH);
	   setHeight(280);
	   ToolTipConfig configHelp = new ToolTipConfig();
	   configHelp.setTitleHtml("Порядок работы");
	   configHelp.setBodyHtml("<div><ul style=\"list-style: disc; margin: 0px 0px 5px 15px\"><li>В браузере открыть ссылку: </li>"+
			                  "<li>для Альфа-Банка http://json.alfabank.ru/atm/0.1/export/</li>"+
			                  "<li>для БИН-Банка https://direct.binbank.ru/api/branches.php</li>"+
			                  "<li>Сохранить страницу в файл через 'Сохранить как'</li>"+
			                  "<li>Выбрать соответствующий банк. Нажать кн.Browse и выбрать сохраненный файл.</li>"+
			                  "<li>На панеле 'Импортировать' нажать кн.Импортировать</li>"+
			                  "<li>Процесс завершиться, когда панель 'Импортировать' станет доступна. "+
			                  "Панель 'Журнал' будет содержать результат.</li>"+
	                          "</ul></div>");
	   configHelp.setCloseable(true);
	   btHelp.setToolTipConfig(configHelp);
	   addTool(btHelp);
//	   HtmlLayoutContainer conTop = new HtmlLayoutContainer(getMarkup());
	   VerticalLayoutContainer conTop = new VerticalLayoutContainer(); //pnImp pnLig
	 //------------------------ pnImp
	   pnImp.setBodyStyle("padding: 3px;");
	   pnImp.setHeaderVisible(true);
	   pnImp.setHeadingText("Импортировать");
	   btImp.setTitle("Импортировать");
	   pnImp.addTool(btImp);
	   pnImp.setHeight(60);
	   HorizontalLayoutContainer contImp = new HorizontalLayoutContainer();
	   
	   cbPart = new ComboBox<PartPrx>(stPart, new LabelProvider<PartPrx>(){
           @Override  public String getLabel(PartPrx item) {
             return item==null ? "":item.getName();
           }
       });
	   fc.creRcAtm().getAllPart().fire(new Receiver<List<PartPrx>>(){
		  	  @Override public void onSuccess(List<PartPrx> response){
		  		if (response != null)  {
		  			cbPart.getStore().replaceAll((List<PartPrx>)response);
		  			cbPart.setValue(response.get(0));
		  		}
		  		else cbPart.clear();
		  	   }});
	   cbPart.setTriggerAction(TriggerAction.ALL);
	   cbPart.setForceSelection(true);
	   cbPart.setEditable(false);
	   cbPart.setName("cbPart");
	   cbPart.setId("cbPart");

	   file = new FileUploadField();
	   file.setId("file"); 
	   file.setName("file");

	   panFile = new FormPanel();
	   panFile.setAction(GWT.getModuleBaseURL()+"srvlUpFileBanks");
//rootLogger.log(Level.INFO,  "GWT.getModuleBaseURL() = " + GWT.getModuleBaseURL());
	   panFile.setEncoding(Encoding.MULTIPART);
	   panFile.setMethod(Method.POST);
//	   panFile.setEncoding(FormPanel.ENCODING_MULTIPART);
////	   panFile.setMethod(FormPanel.METHOD_POST);
//	   panFile.addSubmitHandler(new FormPanel.SubmitHandler() {
//			@Override
//			public void onSubmit(com.google.gwt.user.client.ui.FormPanel.SubmitEvent event) {
////				console.debug("Run");
//	        if (file.getValue().length() == 0) {
//	          Window.alert("Нужно выбрать файл");
//	          event.cancel();
//	        }else{
////  	        impVisible(false);
//  	        taLog.setText("Началось");
//	        }
//			}}
//	   );
	   panFile.addSubmitHandler(new SubmitHandler(){
		   public void onSubmit(SubmitEvent event) {
//	        	panFile.setData("XXXXX", "SSSSSSSS");
//	        	panFile.getElement().setAttribute("QQQQQQ", "WWWWWWW");
//	        	panFile.getElement().setPropertyString("WWW2", "EEEEE");
//	        	file.getElement().setAttribute("QQQQQ1", "WWWWWW1");
//	        	file.getElement().setPropertyString("XXX1", "XXX1");
//	        	cbPart.getElement().setAttribute("QQQQQ1", "WWWWWW1");
//	        	cbPart.getElement().setPropertyString("XXX1", "XXX1");
                hh.setValue(String.valueOf(cbPart.getCurrentValue().getId()));
		        if (file.getValue().length() == 0) {
		          Window.alert("Нужно выбрать файл");
		          event.cancel();
		        }else{
        	     impVisible(false);
        	     taLog.setText("Началось для " + cbPart.getCurrentValue().getName());
		        }
		      }
	   });
	   panFile.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
	       	       impVisible(true);
	   	           taLog.setText(taLog.getText()+"\n"+event.getResults().replace("<pre>","").replace("</pre>", ""));
			}

		});

//	   panFile.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
//			@Override
//			public void onSubmitComplete(com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent event) {
////	       	       impVisible(true);
//	   	           taLog.setText(event.getResults().replace("<pre>","").replace("</pre>", ""));
//			}
//
//		});
	   hh.setName("BankId");
	   contImp.add(hh);
	   contImp.add(cbPart, new HorizontalLayoutData( -1, 1, new Margins(3)));
       contImp.add(file, new HorizontalLayoutData( -1, 1, new Margins(3)));
//       contImp.add(btImp, new HorizontalLayoutData( -1, 1, new Margins(3)));
       panFile.setWidget(contImp);
	   pnImp.add(panFile);
//------------------------------------------------
	   ContentPanel pnLog = new ContentPanel();
	   pnLog.setBodyStyle("padding: 5px;");
	   pnLog.setHeaderVisible(true);
	   pnLog.setHeadingText("Журнал");
	   taLog.setReadOnly(true);
//	   taLog.setWidth(282);
	   taLog.setHeight(140);
	   pnLog.add(taLog);
//------------------------------------------------
	   conTop.add(pnImp, new VerticalLayoutData( 1, -1, new Margins(2)));
	   conTop.add(pnLog, new VerticalLayoutData( 1, -1, new Margins(2)));
//	   add(pnImp);
//	   add(pnLog);
	   setWidget(conTop);
	}
	
	private void impVisible(boolean isVisible){
	  btImp.setVisible(isVisible);
	  pnImp.setEnabled(isVisible);
	}
//	 private native String getMarkup() /*-{
//	    return [ '<table cellpadding=0 cellspacing=5 cols="1">',
//	        '<tr><td class=pnImp></td></tr>',
//	        '<tr><td class=pnLog></td></tr>',
//	        '</table>'
//	    ].join("");
//	  }-*/;

}
