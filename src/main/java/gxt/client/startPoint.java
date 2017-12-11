package gxt.client;


import java.util.List;

import gxt.client.domain.FactAtm;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.Status;
import com.google.web.bindery.requestfactory.shared.Receiver;
//import com.sencha.gxt.widget.core.client.Status.BoxStatusAppearance;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class startPoint implements EntryPoint {
    private FactAtm fct;
    NavPan navPanel;
    protected ToolButton tbSes;
//    ToolTipConfig sesList;
    
    FlowLayoutContainer mp = new FlowLayoutContainer();
    @Override
    public void onModuleLoad() {
	fct = GWT.create(FactAtm.class);
	fct.initialize(new SimpleEventBus());

	ToolBar botBar = new ToolBar();
//	botBar.addStyleName(ThemeStyles.getStyle().borderBottom());
	final Status userState = new Status();
	userState.setWidth(450);

        navPanel = new NavPan(mp, fct);
        fct.creRcAtm().getUserInfo().fire(new Receiver<String>() {
	      public void onSuccess(String data) {
		userState.setText(data); 
	        fct.creRcAtm().getFIs().fire(new Receiver<String>() {
		      public void onSuccess(String data) {
			userState.setText(userState.getText().concat(" ").concat(data));
			navPanel.setFi(data);
		        fct.creRcAtm().getRole().fire(new Receiver<String>() {
			      public void onSuccess(String data) {
//				if (data.equals("A")){
//				  btAtm.setVisible(true);
//				  btTools.setVisible(true);
//				}else if (data.equals("U"))btAtm.setVisible(true);
//		         	if (btAtm.isVisible()) showAtm(null);
				navPanel.setActive(data);
			      }
			      });
		      } });
	      } });

	Viewport viewport = new Viewport();
	final BorderLayoutContainer blc = new BorderLayoutContainer();
 	BorderLayoutData westData = new BorderLayoutData(100);
	westData.setMargins(new Margins(0, 5, 5, 5));
	westData.setCollapsible(true);
	westData.setSplit(true);
	westData.setCollapseMini(true);
	blc.setWestWidget(navPanel, westData);
	
//	ContentPanel mainPanel = new ContentPanel();
//	mainPanel.setHeadingText("XXXX");
//	mp.setCollapsible(false);
//	mpainPanel.getHeader().addStyleName("txt_center");
    mp.getScrollSupport().setScrollMode(ScrollMode.AUTO);
	blc.setCenterWidget(mp);
	
	BorderLayoutData southData = new BorderLayoutData(25);
//	southData.setMargins(new Margins(0, 5, 5, 5));
	southData.setMargins(new Margins(0));
	southData.setCollapsible(false);
	southData.setSplit(false);
	southData.setCollapseMini(false);

//	sesList = new ToolTipConfig();
//	sesList.setTitleHtml("Юзеры");
//	sesList.setBodyHtml("<div><ul style=\"list-style: disc; margin: 0px 0px 5px 15px\"><li>Нажать кн.'Копировать' в заголовке окна(любое количество раз). Корректные атм переместятся в окно 'Обработать'. Некоррктные атм переместятся в окно 'Журнал'</li></ul></div>");
//    sesList.setCloseable(true);
	tbSes = new ToolButton(ToolButton.EXPAND, new SelectHandler() {
         @Override  
         public void onSelect(SelectEvent event) {   
        	 fct.creRcAtm().getSessionUsers().fire(new Receiver<List<String>>() {
      	       @Override public void onSuccess(List<String> response) {
      	    	 ToolTipConfig sesList = new ToolTipConfig();
      	    	 sesList.setTitleHtml("Юзеры");
      	    	 sesList.setCloseable(true);
      	    	   StringBuilder body = new StringBuilder("<div><ul style=\"list-style: disc; margin: 0px 0px 5px 15px\">");
      	    	   for(String it: response){
      	    		 body.append("<li>").append(it).append("</li>");   
      	    	   }
      	    	   body.append("</ul></div>");
      	    	   sesList.setBodyHtml(body.toString());
      	    	   tbSes.setToolTipConfig(sesList);
//      	    	 sesList.setBodyHtml("<div><ul style=\"list-style: disc; margin: 0px 0px 5px 15px\"><li> В окно 'Копировать' вбить или копипастить пары атм формата: '1111 222,333 444' Первый(1111) - откуда копировать, второй(222) - куда </li>"+
//		                  "<li>Нажать кн.'Копировать' в заголовке окна(любое количество раз). Корректные атм переместятся в окно 'Обработать'. Некоррктные атм переместятся в окно 'Журнал'</li>"+
//		                  "<li>Нажать кн.'Обработать' в заголовке окна 'Обработать'. В окне 'Журнал' появятся сообщения о результате копирования</li>"+
//                         "</ul></div>");
      		    }}
            );}
         });
    tbSes.setTitle("");
	
	botBar.add(tbSes);
	botBar.add(userState);
	blc.setSouthWidget(botBar,southData);

	viewport.setWidget(blc);
	RootPanel.get().add(viewport);
    }
}
