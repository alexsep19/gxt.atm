package gxt.client;

import gxt.client.domain.FactAtm;
import gxt.client.proc.PanProc;
import gxt.client.terms.PanTerms;
import gxt.client.tool.PanTool;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class NavPan extends ContentPanel{
    FactAtm fct;
    PanTerms panTerms = null;
    PanTool panTool = null;
    PanProc panProc = null;
    TextButton bTerm = null, bTool = null, bProc = null;
    FlowLayoutContainer playCont = null;
    String role = "", user_fi = "";
    
    public void setActive(String Role){
	role = Role;
//        bReps.setEnabled(true);
	bTerm.setEnabled(!role.isEmpty());
	bTool.setEnabled(role.indexOf("A") >= 0);
	bProc.setEnabled(role.indexOf("A") >= 0);
	if (panTerms == null) panTerms = new PanTerms(fct, user_fi);
	ShowPan(panTerms);
    }
    
    public void setFi(String Fi){
	user_fi = Fi;
    }

    public NavPan(final FlowLayoutContainer blc, final FactAtm Fct){
	fct = Fct;
	playCont = blc;
	setHeadingText("Навигация");
	getHeader().addStyleName("txt_center");
        VBoxLayoutContainer bc = new VBoxLayoutContainer();
        bc.setPadding(new Padding(1));
        bc.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        bTerm = new TextButton("Терминалы", new SelectHandler(){
	    @Override
	    public void onSelect(SelectEvent event) {
		if (panTerms == null) panTerms = new PanTerms(fct, user_fi);
		ShowPan(panTerms);
	    }});
        bTerm.setEnabled(false);
        bc.add( bTerm, new BoxLayoutData(new Margins(0, 0, 1, 0)));
        
        bTool = new TextButton("Справочники", new SelectHandler(){
	    @Override
	    public void onSelect(SelectEvent event) {
		if (panTool == null) panTool = new PanTool(fct);
		ShowPan(panTool);
	    }}); 
        bTool.setEnabled(false);
        bc.add( bTool, new BoxLayoutData(new Margins(0, 0, 1, 0)));
        
        bProc = new TextButton("Процедуры", new SelectHandler(){
	    @Override
	    public void onSelect(SelectEvent event) {
		if (panProc == null) panProc = new PanProc(fct);
		panProc.getFlag();
		ShowPan(panProc);
	    }}); 
        bProc.setEnabled(false);
        bc.add( bProc, new BoxLayoutData(new Margins(0)));
        
        add(bc);
    }
    
    private void ShowPan(ContentPanel pan){
	playCont.clear();
	playCont.add(pan);
    }

}
