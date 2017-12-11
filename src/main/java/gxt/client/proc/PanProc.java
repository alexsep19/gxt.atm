package gxt.client.proc;

import java.util.List;

import gxt.client.domain.FactAtm;
import gxt.tool.AtmImages;

import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class PanProc extends ContentPanel{
    FactAtm f;
    Label lbUpdVal = new Label("");
    Label lbInsVal = new Label("");
    TextButton btImp = new TextButton();
    Image imLoad;

    public PanProc(final FactAtm fct){
//	rootLogger.addHandler(new ConsoleLogHandler());
//	setCollapsible(false);
	f = fct;
	getHeader().addStyleName("txt_center");
	addStyleName("margin-10");
	setHeadingText("Процедуры");
	setPixelSize(1200, 800);
	
	FramedPanel pnImport = new FramedPanel();
	pnImport.addStyleName("margin-10");
	pnImport.setHeadingText("Импорт атм");
	pnImport.setPixelSize(180, 120);
	
	HtmlLayoutContainer panImport = new HtmlLayoutContainer(getImportMarkup());
	
	btImp.addSelectHandler(new SelectHandler() {
	    @Override public void onSelect(SelectEvent event) {
	       setIsRun(true);
	       f.creRcAtm().loadMdm().fire(new Receiver<List<String>>() {
	       @Override public void onSuccess(List<String> response) {
			setIsRun(false);
//			setResult(response.get(0), response.get(1));
			lbUpdVal.setText(response.get(0));
			lbInsVal.setText(response.get(1));
		    }});
//	      MessageBox messageBox = new MessageBox("GXT Works.");
//	      messageBox.show();
	    }
	  });
	btImp.setText("Обновить атм МДМ");
	btImp.setIcon(AtmImages.INSTANCE.butRefr());
	btImp.setIconAlign(IconAlign.LEFT);
        imLoad = new Image(AtmImages.INSTANCE.butLoad());
        setIsRun(false);
    	panImport.add( btImp, new HtmlData(".btImp"));
	panImport.add( imLoad, new HtmlData(".imLoad"));
	
	Label lbIns = new Label("Добавлено");
	panImport.add( lbIns, new HtmlData(".ins"));
	panImport.add( lbInsVal, new HtmlData(".insval"));

	Label lbUpd = new Label("Обновлено");
	panImport.add( lbUpd, new HtmlData(".upd"));
	panImport.add( lbUpdVal, new HtmlData(".updval"));

	pnImport.setWidget(panImport);
	VerticalLayoutContainer topCont = new VerticalLayoutContainer();
	topCont.add(pnImport, new VerticalLayoutData(-1, -1, new Margins(3)));
	PanCopy panCopy = new PanCopy(f); 
	topCont.add(panCopy, new VerticalLayoutData(-1, -1, new Margins(3)));
    PanBanks panBanks = new PanBanks(f);
	topCont.add(panBanks, new VerticalLayoutData(-1, -1, new Margins(3)));
//	final HorizontalLayoutContainer topCont = new HorizontalLayoutContainer();
//	topCont.add(panImport, new HorizontalLayoutData(-1, -1, new Margins(3)));
	
	setWidget(topCont);
    }
    
    public void getFlag(){
      btImp.setEnabled(false);
      f.creRcAtm().isUpdateRun().fire(new Receiver<Boolean>(){
           @Override public void onSuccess(Boolean response) {
               btImp.setEnabled(true);
               setIsRun(response);
		 }
	      });
    }
    
    public void setIsRun(boolean is){
	btImp.setVisible(!is);
	imLoad.setVisible(is);
    }
    
    private native String getImportMarkup() /*-{
    return [ '<table cellpadding=0 cellspacing=4 cols="2">',
        '<tr><td class=btImp colspan=2></td></tr>',
        '<tr><td class=imLoad colspan=2></td></tr>',
        '<tr><td class=ins></td><td class=insval></td></tr>',
        '<tr><td class=upd></td><td class=updval></td></tr>',
        '</table>'
    ].join("");
  }-*/;
}
