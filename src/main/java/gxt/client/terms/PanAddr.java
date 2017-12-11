package gxt.client.terms;

import gxt.client.domain.FactAtm;
import gxt.client.domain.FactAtm.rcAtm;
import gxt.client.domain.LabVal;
import gxt.client.domain.TermStPrx;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import jpa.atm.AtTerm;

import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.form.error.TitleErrorHandler;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

//import gxt.client.domain.FactAtm;
//import gxt.client.domain.FactAtm.rcAtm;
//import gxt.client.domain.LabVal;
//import gxt.client.domain.TermStPrx;

public class PanAddr extends FramedPanel{
    private static Logger rootLogger = Logger.getLogger("");
    private static final int ADDR_WIDTH = 525;//518
    private static final int ADDR_HEIGHT = 440;//435
    private final boolean isEditable;
    private final String lbPer = "с";
    private final String lbAll = "Круглосуточно";
    private final String lbFor = "по";
    private final String lbBrFr = "перерыв с";
    final String maskTime ="^([0-1]\\d|2[0-3]):([0-5]\\d)$";
    final String titleTime = "00:00-23:59";
    final String timeEmpty = "00:00";
    final String cooEmpty = "000.0000000000";
    final String maskCoo = "^[0-9]{2,3}\\.[0-9]{1,10}$";
    final String titleCoo = "00-000.0000000000";
    FactAtm f;
    TermStPrx curTerm;
    PanTerms papa;
    HtmlLayoutContainer contVertAddr, contVertWork;
    interface CbxProp extends PropertyAccess<LabVal> {
	    ModelKeyProvider<LabVal> value();
	    LabelProvider<LabVal> label();
	  }

    final ToolButton tbEdit = new ToolButton(ToolButton.GEAR, new SelectHandler() {
        @Override  public void onSelect(SelectEvent event) { setFieldsEnable(true);    }    });
    final ToolButton tbSave = new ToolButton(ToolButton.SAVE, new SelectHandler() {
        @Override  public void onSelect(SelectEvent event) {
           rcAtm req = f.creRcAtm();
           TermStPrx editItem = req.edit(curTerm);
           editItem.setAddr(taAddr.getText());
           if (!txDol.isValid() || !txShi.isValid() || 
               !txMoFr.isValid() || !txMoTo.isValid() || !txMoPerFr.isValid() || !txMoPerTo.isValid()) {
             ShowErrorAddr("С КРАСНЫМИ полями не сохраняется!", true);
             return;
           }
           editItem.setAddr(taAddr.getValue());
           editItem.setLocation(taLoc.getValue());
           editItem.setDop(taDop.getValue());
           editItem.setDol(txDol.getValue());
           editItem.setShi(txShi.getValue());
           editItem.setIsForall(comboForAll.getCurrentValue().getValue());
           editItem.setIsActive(comboActive.getCurrentValue().getValue());
           editItem.setDaysNotrans(txNT.getValue().trim());
           
           editItem.setDMo(cbMo.getValue()?"Y":"N"); editItem.setDMoAll(rdMoAll.getValue()?"Y":"N");editItem.setDMoFr(txMoFr.getValue());editItem.setDMoTo(txMoTo.getValue());editItem.setpMoFr(txMoPerFr.getValue()); editItem.setpMoTo(txMoPerTo.getValue());
           editItem.setDTu(cbTu.getValue()?"Y":"N"); editItem.setDTuAll(rdTuAll.getValue()?"Y":"N");editItem.setDTuFr(txTuFr.getValue());editItem.setDTuTo(txTuTo.getValue());editItem.setpTuFr(txTuPerFr.getValue()); editItem.setpTuTo(txTuPerTo.getValue());
           editItem.setDWe(cbWe.getValue()?"Y":"N"); editItem.setDWeAll(rdWeAll.getValue()?"Y":"N");editItem.setDWeFr(txWeFr.getValue());editItem.setDWeTo(txWeTo.getValue());editItem.setpWeFr(txWePerFr.getValue()); editItem.setpWeTo(txWePerTo.getValue());
           editItem.setDTh(cbTh.getValue()?"Y":"N"); editItem.setDThAll(rdThAll.getValue()?"Y":"N");editItem.setDThFr(txThFr.getValue());editItem.setDThTo(txThTo.getValue());editItem.setpThFr(txThPerFr.getValue()); editItem.setpThTo(txThPerTo.getValue());
           editItem.setDFr(cbFr.getValue()?"Y":"N"); editItem.setDFrAll(rdFrAll.getValue()?"Y":"N");editItem.setDFrFr(txFrFr.getValue());editItem.setDFrTo(txFrTo.getValue());editItem.setpFrFr(txFrPerFr.getValue()); editItem.setpFrTo(txFrPerTo.getValue());
           editItem.setDSa(cbSa.getValue()?"Y":"N"); editItem.setDSaAll(rdSaAll.getValue()?"Y":"N");editItem.setDSaFr(txSaFr.getValue());editItem.setDSaTo(txSaTo.getValue());editItem.setpSaFr(txSaPerFr.getValue()); editItem.setpSaTo(txSaPerTo.getValue());
           editItem.setDSu(cbSu.getValue()?"Y":"N"); editItem.setDSuAll(rdSuAll.getValue()?"Y":"N");editItem.setDSuFr(txSuFr.getValue());editItem.setDSuTo(txSuTo.getValue());editItem.setpSuFr(txSuPerFr.getValue()); editItem.setpSuTo(txSuPerTo.getValue());

           
  	  req.merg(editItem).fire(new Receiver<Void>() {
	      public void onSuccess(Void data) {
	          setFieldsEnable(false);  
//	       	  mainPan.refreshTab();
	          papa.gridRefresh();
	       }
              public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
	                StringBuffer b = new StringBuffer(); 
	        	for (ConstraintViolation<?> err : violations){
	        	  b.append(err.getPropertyPath() + ". " + err.getMessage());
	        	}
	        	ShowErrorAddr(b.toString(), true);
	        }
              public void onFailure(ServerFailure error) {
	          ShowErrorAddr("Охеренная ошибка сервера", true);
	          super.onFailure(error);
	        }
	      });
     }}); 
    final ToolButton tbCancel = new ToolButton(ToolButton.CLOSE, new SelectHandler() {
        @Override  public void onSelect(SelectEvent event) { 
            setNewVals(curTerm);
            setFieldsEnable(false);   }    });
    
    final Label labTown = new Label();
    final TextArea taErro = new TextArea();
    final TextArea taAddr = new TextArea();
    final TextArea taLoc = new TextArea();
    final TextArea taDop = new TextArea();
    final TextField txDol = new TextField();
    final TextField txShi = new TextField();
    ComboBox<LabVal> comboForAll;
    ComboBox<LabVal> comboActive;
    final TextField txNT = new TextField();
    FieldLabel LabNoTrans;
    final CheckBox cbMo = new CheckBox();Radio rdMoAll = new Radio(); Radio rdMoPer = new Radio();
    ToggleGroup tgMo; final TextField txMoFr = new TextField();  final TextField txMoTo = new TextField();  final TextField txMoPerFr = new TextField();  final TextField txMoPerTo = new TextField();
    final CheckBox cbTu = new CheckBox();final Radio rdTuAll = new Radio();final Radio rdTuPer = new Radio();
    ToggleGroup tgTu;final TextField txTuFr = new TextField();final TextField txTuTo = new TextField();final TextField txTuPerFr = new TextField();final TextField txTuPerTo = new TextField();
    final CheckBox cbWe = new CheckBox();final Radio rdWeAll = new Radio();final Radio rdWePer = new Radio();
    ToggleGroup tgWe;final TextField txWeFr = new TextField();final TextField txWeTo = new TextField();final TextField txWePerFr = new TextField();final TextField txWePerTo = new TextField();
    final CheckBox cbTh = new CheckBox();final Radio rdThAll = new Radio();final Radio rdThPer = new Radio();
    ToggleGroup tgTh;final TextField txThFr = new TextField();final TextField txThTo = new TextField();final TextField txThPerFr = new TextField();final TextField txThPerTo = new TextField();
    final CheckBox cbFr = new CheckBox();final Radio rdFrAll = new Radio();final Radio rdFrPer = new Radio();
    ToggleGroup tgFr;final TextField txFrFr = new TextField();final TextField txFrTo = new TextField();final TextField txFrPerFr = new TextField();final TextField txFrPerTo = new TextField();
    final CheckBox cbSa = new CheckBox();final Radio rdSaAll = new Radio();final Radio rdSaPer = new Radio();
    ToggleGroup tgSa;final TextField txSaFr = new TextField();final TextField txSaTo = new TextField();final TextField txSaPerFr = new TextField();final TextField txSaPerTo = new TextField();
    final CheckBox cbSu = new CheckBox();final Radio rdSuAll = new Radio();final Radio rdSuPer = new Radio();
    ToggleGroup tgSu;final TextField txSuFr = new TextField();final TextField txSuTo = new TextField();final TextField txSuPerFr = new TextField();final TextField txSuPerTo = new TextField();

//    ArrayList<LabVal> alForAll = new ArrayList<LabVal>();
    
    public PanAddr(FactAtm fc, boolean IsEditable, PanTerms Papa){
      rootLogger.addHandler(new ConsoleLogHandler());
      f = fc;
      papa = Papa;
      isEditable = IsEditable;
//      curTerm = t;
      tbEdit.setTitle("Редактировать");
      tbSave.setTitle("Сохранить");
      tbCancel.setTitle("Не сохранять");
      setHeadingText("Адрес");
      setWidth(ADDR_WIDTH);
      setHeight(ADDR_HEIGHT);
      setBodyStyle("background: none; padding: 2px");
      contVertAddr = new HtmlLayoutContainer(getAddrMarkup());
//      contVertAddr.addStyleName("blackRO");
      contVertWork = new HtmlLayoutContainer(getWorkMarkup());
      FieldSet fsWork = new FieldSet();
      fsWork.setHeadingText("Режим работы");
      fsWork.setWidget(contVertWork);
      fsWork.setWidth("500px");
      VerticalLayoutContainer vcMain = new VerticalLayoutContainer();
      vcMain.add(contVertAddr);
      vcMain.add(fsWork);
      vcMain.setScrollMode(ScrollMode.AUTO);
//      rootLogger.log(Level.INFO,  "PanAddr =  vcMain.add(fsWork);");
      fillCont();
      setWidget(vcMain);
    }

    private void setTxtVal(ValueBaseField F, String V, int len){
	F.setValue(V); 
	F.getCell().getInputElement(F.getElement()).setMaxLength(len);
    }
    
    public void setNewVals(TermStPrx t){
//	rootLogger.log(Level.INFO,  "setNewVals");
        setFieldsEnable(false);
//	rootLogger.log(Level.INFO,  "setFieldsEnable");
        if (t == null) return;
	curTerm = t;
//	labTown.setText(curTerm.getF250Town().getAtOkato().getName()+", " + curTerm.getF250Town().getRusName());
	labTown.setText(curTerm.getAtOkato().getName()+", " + curTerm.getF250Town().getRusName());
//	taAddr.setValue(curTerm.getAddr());
//	taLoc.setValue(curTerm.getLocation());
//	taDop.setValue(curTerm.getDop());
	setTxtVal(taAddr, curTerm.getAddr(), AtTerm.LEN_addr);
	setTxtVal(taLoc, curTerm.getLocation(), AtTerm.LEN_location);
	setTxtVal(taDop, curTerm.getDop(), AtTerm.LEN_dop);
	txDol.clearInvalid();
	txShi.clearInvalid();
//	txDol.setValue(curTerm.getDol());
//	txShi.setValue(curTerm.getShi());
	setTxtVal(txDol, curTerm.getDol(), AtTerm.LEN_dol);
	setTxtVal(txShi, curTerm.getShi(), AtTerm.LEN_shi);

		
//	rootLogger.log(Level.INFO,  "LabVal");
	LabVal lb = null;
        for(LabVal it:comboForAll.getStore().getAll()){
           if (it.getValue().equals(curTerm.getIsForall())){lb = it;break;}}
        comboForAll.setValue(lb);
        lb = null;
        for(LabVal it:comboActive.getStore().getAll()){
            if (it.getValue().equals(curTerm.getIsActive())){lb = it;break;}}
        comboActive.setValue(lb);

//	rootLogger.log(Level.INFO,  "fin LabVal");
	String s = curTerm.getDaysNotrans();
//	rootLogger.log(Level.INFO,  "curTerm.getDaysNotrans() = " + s);
        if (s==null || s.isEmpty()) {
            LabNoTrans.setVisible(false);
//            txNT.setValue("");
     	    setTxtVal(txNT, "", AtTerm.LEN_NT);
        }else {
//            txNT.setValue(curTerm.getDaysNotrans());
     	    setTxtVal(txNT, curTerm.getDaysNotrans(), AtTerm.LEN_NT);
            LabNoTrans.setVisible(true);
        }
//        rootLogger.log(Level.INFO,  ".getDaysNotrans()");
//====================================================	
	cbMo.setValue(curTerm.getDMo().equals("Y"));
        boolean isPerEna = !curTerm.getDMoAll().equals("Y");
        rdMoAll.setValue(!isPerEna, true);
        rdMoPer.setValue(isPerEna, true);

        txMoFr.clearInvalid();txMoTo.clearInvalid();
        txMoPerFr.clearInvalid();txMoPerTo.clearInvalid();
        setTxtVal(txMoFr, curTerm.getDMoFr(), 5);
//	txMoFr.setValue(curTerm.getDMoFr()); txMoFr.getCell().getInputElement(txMoFr.getElement()).setMaxLength(5);
        setTxtVal(txMoTo, curTerm.getDMoTo(), 5);
//	txMoTo.setValue(curTerm.getDMoTo());
        txMoFr.setReadOnly(!isPerEna); 
        txMoTo.setReadOnly(!isPerEna);
        setTxtVal(txMoPerFr, curTerm.getpMoFr(), 5);
//        txMoPerFr.setValue(curTerm.getpMoFr());
        setTxtVal(txMoPerTo, curTerm.getpMoTo(), 5);
//	txMoPerTo.setValue(curTerm.getpMoTo());

	cbTu.setValue(curTerm.getDTu().equals("Y"));
	isPerEna = !curTerm.getDTuAll().equals("Y");
	rdTuAll.setValue(!isPerEna, true);
	rdTuPer.setValue(isPerEna, true);
        txTuFr.clearInvalid();txTuTo.clearInvalid();
        txTuPerFr.clearInvalid();txTuPerTo.clearInvalid();
        setTxtVal(txTuFr, curTerm.getDTuFr(), 5);
        setTxtVal(txTuTo, curTerm.getDTuTo(), 5);
//	txTuFr.setValue(curTerm.getDTuFr());
//	txTuTo.setValue(curTerm.getDTuTo());
        txTuFr.setReadOnly(!isPerEna); 
        txTuTo.setReadOnly(!isPerEna);
        setTxtVal(txTuPerFr, curTerm.getpTuFr(), 5);
        setTxtVal(txTuPerTo, curTerm.getpTuTo(), 5);
//        txTuPerFr.setValue(curTerm.getpTuFr());
//	txTuPerTo.setValue(curTerm.getpTuTo());

	cbWe.setValue(curTerm.getDWe().equals("Y"));
	isPerEna = !curTerm.getDWeAll().equals("Y");
        rdWeAll.setValue(!isPerEna, true);
	rdWePer.setValue(isPerEna, true);
        txWeFr.clearInvalid();txWeTo.clearInvalid();
        txWePerFr.clearInvalid();txWePerTo.clearInvalid();
        setTxtVal(txWeFr, curTerm.getDWeFr(), 5);
        setTxtVal(txWeTo, curTerm.getDWeTo(), 5);
//	txWeFr.setValue(curTerm.getDWeFr());
//	txWeTo.setValue(curTerm.getDWeTo());
        txWeFr.setReadOnly(!isPerEna); 
        txWeTo.setReadOnly(!isPerEna);
        setTxtVal(txWePerFr, curTerm.getpWeFr(), 5);
        setTxtVal(txWePerTo, curTerm.getpWeTo(), 5);
//        txWePerFr.setValue(curTerm.getpWeFr());
//	txWePerTo.setValue(curTerm.getpWeTo());

	cbTh.setValue(curTerm.getDTh().equals("Y"));
	isPerEna = !curTerm.getDThAll().equals("Y");
	rdThAll.setValue(!isPerEna, true);
	rdThPer.setValue(isPerEna, true);
        txThFr.clearInvalid();txThTo.clearInvalid();
        txThPerFr.clearInvalid();txThPerTo.clearInvalid();
        setTxtVal(txThFr, curTerm.getDThFr(), 5);
        setTxtVal(txThTo, curTerm.getDThTo(), 5);
//	txThFr.setValue(curTerm.getDThFr());
//	txThTo.setValue(curTerm.getDThTo());
        txThFr.setReadOnly(!isPerEna); 
        txThTo.setReadOnly(!isPerEna);
        setTxtVal(txThPerFr, curTerm.getpThFr(), 5);
        setTxtVal(txThPerTo, curTerm.getpThTo(), 5);
//        txThPerFr.setValue(curTerm.getpThFr());
//	txThPerTo.setValue(curTerm.getpThTo());

	cbFr.setValue(curTerm.getDFr().equals("Y"));
	isPerEna = !curTerm.getDFrAll().equals("Y");
	rdFrAll.setValue(!isPerEna, true);
	rdFrPer.setValue(isPerEna, true);
        txFrFr.clearInvalid();txFrTo.clearInvalid();
        txFrPerFr.clearInvalid();txFrPerTo.clearInvalid();
        setTxtVal(txFrFr, curTerm.getDFrFr(), 5);
        setTxtVal(txFrTo, curTerm.getDFrTo(), 5);
//	txFrFr.setValue(curTerm.getDFrFr());
//	txFrTo.setValue(curTerm.getDFrTo());
        txFrFr.setReadOnly(!isPerEna); 
        txFrTo.setReadOnly(!isPerEna);
        setTxtVal(txFrPerFr, curTerm.getpFrFr(), 5);
        setTxtVal(txFrPerTo, curTerm.getpFrTo(), 5);
//        txFrPerFr.setValue(curTerm.getpFrFr());
//	txFrPerTo.setValue(curTerm.getpFrTo());

	cbSa.setValue(curTerm.getDSa().equals("Y"));
	isPerEna = !curTerm.getDSaAll().equals("Y");
	rdSaAll.setValue(!isPerEna, true);
	rdSaPer.setValue(isPerEna, true);
        txSaFr.clearInvalid();txSaTo.clearInvalid();
        txSaPerFr.clearInvalid();txSaPerTo.clearInvalid();
        setTxtVal(txSaFr, curTerm.getDSaFr(), 5);
        setTxtVal(txSaTo, curTerm.getDSaTo(), 5);
//	txSaFr.setValue(curTerm.getDSaFr());
//	txSaTo.setValue(curTerm.getDSaTo());
        txSaFr.setReadOnly(!isPerEna); 
        txSaTo.setReadOnly(!isPerEna);
        setTxtVal(txSaPerFr, curTerm.getpSaFr(), 5);
        setTxtVal(txSaPerTo, curTerm.getpSaTo(), 5);
//        txSaPerFr.setValue(curTerm.getpSaFr());
//	txSaPerTo.setValue(curTerm.getpSaTo());

	cbSu.setValue(curTerm.getDSu().equals("Y"));
	isPerEna = !curTerm.getDSuAll().equals("Y");
        rdSuAll.setValue(!isPerEna, true);
	rdSuPer.setValue(isPerEna, true);
        txSuFr.clearInvalid();txSuTo.clearInvalid();
        txSuPerFr.clearInvalid();txSuPerTo.clearInvalid();
        setTxtVal(txSuFr, curTerm.getDSuFr(), 5);
        setTxtVal(txSuTo, curTerm.getDSuTo(), 5);
//	txSuFr.setValue(curTerm.getDSuFr());
//	txSuTo.setValue(curTerm.getDSuTo());
        txSuFr.setReadOnly(!isPerEna); 
        txSuTo.setReadOnly(!isPerEna);
        setTxtVal(txSuPerFr, curTerm.getpSuFr(), 5);
        setTxtVal(txSuPerTo, curTerm.getpSuTo(), 5);
//        txSuPerFr.setValue(curTerm.getpSuFr());
//	txSuPerTo.setValue(curTerm.getpSuTo());
}
    
    public void fillCont(){
//	rootLogger.log(Level.INFO,  "fillCont()");
	taErro.setVisible(false);
	taErro.setReadOnly(true);
	taErro.setAllowBlank(true);
	taErro.setWidth(400);
	taErro.addStyleName("errArea");
	contVertAddr.add(taErro, new HtmlData(".erro"));

	contVertAddr.add(labTown, new HtmlData(".town"));
	
	taAddr.setAllowBlank(true);
	taAddr.setWidth(200);
	taAddr.getCell().getInputElement(taAddr.getElement()).setMaxLength(AtTerm.LEN_addr);
	FieldLabel fl1 = new FieldLabel(taAddr, "Адрес");
	fl1.setLabelAlign(LabelAlign.TOP);
	contVertAddr.add(fl1, new HtmlData(".addr"));

	taLoc.setWidth(200);
	taLoc.setAllowBlank(true);
	taLoc.getCell().getInputElement(taLoc.getElement()).setMaxLength(AtTerm.LEN_location);
	FieldLabel fl3 = new FieldLabel(taLoc, "Расположение");
	fl3.setLabelAlign(LabelAlign.TOP);
	contVertAddr.add(fl3, new HtmlData(".local"));

	taDop.setWidth(200);
	taDop.setAllowBlank(true);
	taDop.getCell().getInputElement(taDop.getElement()).setMaxLength(AtTerm.LEN_dop);
	FieldLabel fl2 = new FieldLabel(taDop, "Дополнительная информация");
	fl2.setLabelAlign(LabelAlign.TOP);
	contVertAddr.add(fl2, new HtmlData(".dop"));
	
	txDol.setWidth(100);
	txDol.getCell().getInputElement(txDol.getElement()).setMaxLength(AtTerm.LEN_dol);
	txDol.addValidator(new RegExValidator( maskCoo, titleCoo));
	txDol.setErrorSupport(new TitleErrorHandler(txDol));
	FieldLabel fl4 = new FieldLabel(txDol, "Долгота");
	fl4.setLabelAlign(LabelAlign.TOP);
	contVertAddr.add(fl4, new HtmlData(".dol"));

	txShi.setWidth(100);
	txShi.getCell().getInputElement(txShi.getElement()).setMaxLength(AtTerm.LEN_shi);
	txShi.addValidator(new RegExValidator( maskCoo, titleCoo));
	txShi.setErrorSupport(new TitleErrorHandler(txShi));
	FieldLabel fl5 = new FieldLabel(txShi, "Широта");
	fl5.setLabelAlign(LabelAlign.TOP);
	contVertAddr.add( fl5, new HtmlData(".shi"));
	
	CbxProp propForAll = GWT.create(CbxProp.class);
	ListStore<LabVal> storeForAll = new ListStore<LabVal>(propForAll.value());
	storeForAll.add(new LabVal("Y", "Доступен"));
	storeForAll.add(new LabVal("N", "Не доступен"));
//	ArrayList<LabVal> alForAll = new ArrayList<LabVal>();
//	alForAll.add(new LabVal("Y", "Доступен"));
//	alForAll.add(new LabVal("N", "Не доступен"));
//	storeForAll.addAll(alForAll);
	comboForAll = new ComboBox<LabVal>(storeForAll, propForAll.label());
	comboForAll.setForceSelection(true);
	comboForAll.setTriggerAction(TriggerAction.ALL);
	comboForAll.setEditable(false);
        comboForAll.setWidth(100);
        FieldLabel fl6 = new FieldLabel(comboForAll, "Доступность");
	fl6.setLabelAlign(LabelAlign.TOP);
	contVertAddr.add( fl6, new HtmlData(".IsAll"));

	CbxProp propActive = GWT.create(CbxProp.class);
	ListStore<LabVal> storeActive = new ListStore<LabVal>(propActive.value());
	storeActive.add(new LabVal("Y", "Работает"));
	storeActive.add(new LabVal("N", "Не работает"));
        comboActive = new ComboBox<LabVal>(storeActive, propActive.label());
        comboActive.setForceSelection(true);
        comboActive.setTriggerAction(TriggerAction.ALL);
        comboActive.setEditable(false);
        comboActive.setWidth(100);
        FieldLabel fl7 = new FieldLabel(comboActive, "Активность");
	fl7.setLabelAlign(LabelAlign.TOP);
	contVertAddr.add( fl7, new HtmlData(".Active"));

//	rootLogger.log(Level.INFO,  "comboActive");
//====================================================	
	LabNoTrans = new FieldLabel(txNT, "Не разобрано");
	txNT.setWidth(150);
	txNT.getCell().getInputElement(txNT.getElement()).setMaxLength(AtTerm.LEN_NT);
        contVertWork.add(LabNoTrans, new HtmlData(".NoTrans"));

	cbMo.setBoxLabel("Пн");
	contVertWork.add( cbMo, new HtmlData(".Mo"));
	
	rdMoAll.setBoxLabel(lbAll);
	contVertWork.add( rdMoAll, new HtmlData(".MoAll"));
		
	rdMoPer.setBoxLabel(lbPer);
	contVertWork.add( rdMoPer, new HtmlData(".MoPer"));
	tgMo = new ToggleGroup();
	tgMo.add(rdMoAll);tgMo.add(rdMoPer);

        txMoFr.setWidth(40);
        txMoFr.getCell().getInputElement(txMoFr.getElement()).setMaxLength(5);
        txMoFr.addValidator(new RegExValidator( maskTime, titleTime));
        txMoFr.setErrorSupport(new TitleErrorHandler(txMoFr));
        contVertWork.add(txMoFr, new HtmlData(".MoFr"));
        
        Label labMoTo = new Label(lbFor);
        contVertWork.add(labMoTo, new HtmlData(".MoLabTo"));
        txMoTo.setWidth(40);
        txMoTo.getCell().getInputElement(txMoTo.getElement()).setMaxLength(5);
        txMoTo.addValidator(new RegExValidator( maskTime, titleTime));
        txMoTo.setErrorSupport(new TitleErrorHandler(txMoTo));
        contVertWork.add(txMoTo, new HtmlData(".MoTo"));
        
        Label labMoPerFr = new Label(lbBrFr);
        contVertWork.add(labMoPerFr, new HtmlData(".MoLabPerFr"));
        txMoPerFr.setWidth(40);
        txMoPerFr.getCell().getInputElement(txMoPerFr.getElement()).setMaxLength(5);
        txMoPerFr.addValidator(new RegExValidator( maskTime, titleTime));
        txMoPerFr.setErrorSupport(new TitleErrorHandler(txMoPerFr));
        contVertWork.add(txMoPerFr, new HtmlData(".MoPerFr"));
        
        Label labMoPerTo = new Label(lbFor);
        contVertWork.add(labMoPerTo, new HtmlData(".MoLabPerTo"));
        txMoPerTo.setWidth(40);
        txMoPerTo.getCell().getInputElement(txMoPerTo.getElement()).setMaxLength(5);
        txMoPerTo.addValidator(new RegExValidator( maskTime, titleTime));
        txMoPerTo.setErrorSupport(new TitleErrorHandler(txMoPerTo));
        contVertWork.add(txMoPerTo, new HtmlData(".MoPerTo"));

	rdMoPer.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<Boolean> event) {
		boolean isEnable = event.getValue();
	        txMoFr.setReadOnly(!isEnable); 
	        txMoTo.setReadOnly(!isEnable);
	        if (isEnable){
		   txMoFr.getCell().getInputElement(txMoFr.getElement()).setMaxLength(5);
		   txMoTo.getCell().getInputElement(txMoTo.getElement()).setMaxLength(5);
	        }else{
	           txMoFr.clear(); txMoFr.clearInvalid();
	           txMoTo.clear(); txMoTo.clearInvalid();
	        }
	    }});
	
	cbTu.setBoxLabel("Вт");
	contVertWork.add( cbTu, new HtmlData(".Tu"));
	
	rdTuAll.setBoxLabel(lbAll);
	contVertWork.add( rdTuAll, new HtmlData(".TuAll"));
		
	rdTuPer.setBoxLabel(lbPer);
	contVertWork.add( rdTuPer, new HtmlData(".TuPer"));
	tgTu = new ToggleGroup();
	tgTu.add(rdTuAll);tgTu.add(rdTuPer);

        txTuFr.setWidth(40);
        txTuFr.getCell().getInputElement(txTuFr.getElement()).setMaxLength(5);
        txTuFr.addValidator(new RegExValidator( maskTime, titleTime));
        txTuFr.setErrorSupport(new TitleErrorHandler(txTuFr));
        contVertWork.add(txTuFr, new HtmlData(".TuFr"));
        
        Label labTuTo = new Label(lbFor);
        contVertWork.add(labTuTo, new HtmlData(".TuLabTo"));
        txTuTo.setWidth(40);
        txTuTo.getCell().getInputElement(txTuTo.getElement()).setMaxLength(5);
        txTuTo.addValidator(new RegExValidator( maskTime, titleTime));
        txTuTo.setErrorSupport(new TitleErrorHandler(txTuTo));
        contVertWork.add(txTuTo, new HtmlData(".TuTo"));
        
        Label labTuPerFr = new Label(lbBrFr);
        contVertWork.add(labTuPerFr, new HtmlData(".TuLabPerFr"));
        txTuPerFr.setWidth(40);
        txTuPerFr.getCell().getInputElement(txTuPerFr.getElement()).setMaxLength(5);
        txTuPerFr.addValidator(new RegExValidator( maskTime, titleTime));
        txTuPerFr.setErrorSupport(new TitleErrorHandler(txTuPerFr));
        contVertWork.add(txTuPerFr, new HtmlData(".TuPerFr"));
        
        Label labTuPerTo = new Label(lbFor);
        contVertWork.add(labTuPerTo, new HtmlData(".TuLabPerTo"));
        txTuPerTo.setWidth(40);
        txTuPerTo.getCell().getInputElement(txTuPerTo.getElement()).setMaxLength(5);
        txTuPerTo.addValidator(new RegExValidator( maskTime, titleTime));
        txTuPerTo.setErrorSupport(new TitleErrorHandler(txTuPerTo));
        contVertWork.add(txTuPerTo, new HtmlData(".TuPerTo"));

	rdTuPer.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<Boolean> event) {
		boolean isEnable = event.getValue();
	        txTuFr.setReadOnly(!isEnable); 
	        txTuTo.setReadOnly(!isEnable);
	        if (isEnable){
		   txTuFr.getCell().getInputElement(txTuFr.getElement()).setMaxLength(5);
		   txTuTo.getCell().getInputElement(txTuTo.getElement()).setMaxLength(5);
	        }else{
	           txTuFr.clear(); txTuFr.clearInvalid();
	           txTuTo.clear(); txTuTo.clearInvalid();
	        }
	    }});

	cbWe.setBoxLabel("Ср");
	contVertWork.add( cbWe, new HtmlData(".We"));
	
	rdWeAll.setBoxLabel(lbAll);
	contVertWork.add( rdWeAll, new HtmlData(".WeAll"));
		
	rdWePer.setBoxLabel(lbPer);
	contVertWork.add( rdWePer, new HtmlData(".WePer"));
	tgWe = new ToggleGroup();
	tgWe.add(rdWeAll);tgWe.add(rdWePer);

        txWeFr.setWidth(40);
        txWeFr.getCell().getInputElement(txWeFr.getElement()).setMaxLength(5);
        txWeFr.addValidator(new RegExValidator( maskTime, titleTime));
        txWeFr.setErrorSupport(new TitleErrorHandler(txWeFr));
        contVertWork.add(txWeFr, new HtmlData(".WeFr"));
        
        Label labWeTo = new Label(lbFor);
        contVertWork.add(labWeTo, new HtmlData(".WeLabTo"));
        txWeTo.setWidth(40);
        txWeTo.getCell().getInputElement(txWeTo.getElement()).setMaxLength(5);
        txWeTo.addValidator(new RegExValidator( maskTime, titleTime));
        txWeTo.setErrorSupport(new TitleErrorHandler(txWeTo));
        contVertWork.add(txWeTo, new HtmlData(".WeTo"));
        
        Label labWePerFr = new Label(lbBrFr);
        contVertWork.add(labWePerFr, new HtmlData(".WeLabPerFr"));
        txWePerFr.setWidth(40);
        txWePerFr.getCell().getInputElement(txWePerFr.getElement()).setMaxLength(5);
        txWePerFr.addValidator(new RegExValidator( maskTime, titleTime));
        txWePerFr.setErrorSupport(new TitleErrorHandler(txWePerFr));
        contVertWork.add(txWePerFr, new HtmlData(".WePerFr"));
        
        Label labWePerTo = new Label(lbFor);
        contVertWork.add(labWePerTo, new HtmlData(".WeLabPerTo"));
        txWePerTo.setWidth(40);
        txWePerTo.getCell().getInputElement(txWePerTo.getElement()).setMaxLength(5);
        txWePerTo.addValidator(new RegExValidator( maskTime, titleTime));
        txWePerTo.setErrorSupport(new TitleErrorHandler(txWePerTo));
        contVertWork.add(txWePerTo, new HtmlData(".WePerTo"));

	rdWePer.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<Boolean> event) {
		boolean isEnable = event.getValue();
	        txWeFr.setReadOnly(!isEnable); 
	        txWeTo.setReadOnly(!isEnable);
	        if (isEnable){
		   txWeFr.getCell().getInputElement(txWeFr.getElement()).setMaxLength(5);
		   txWeTo.getCell().getInputElement(txWeTo.getElement()).setMaxLength(5);
	        }else{
	           txWeFr.clear(); txWeFr.clearInvalid();
	           txWeTo.clear(); txWeTo.clearInvalid();
	        }
	    }});

	cbTh.setBoxLabel("Чт");
	contVertWork.add( cbTh, new HtmlData(".Th"));
	
	rdThAll.setBoxLabel(lbAll);
	contVertWork.add( rdThAll, new HtmlData(".ThAll"));
		
	rdThPer.setBoxLabel(lbPer);
	contVertWork.add( rdThPer, new HtmlData(".ThPer"));
	tgTh = new ToggleGroup();
	tgTh.add(rdThAll);tgTh.add(rdThPer);

        txThFr.setWidth(40);
        txThFr.getCell().getInputElement(txThFr.getElement()).setMaxLength(5);
        txThFr.addValidator(new RegExValidator( maskTime, titleTime));
        txThFr.setErrorSupport(new TitleErrorHandler(txThFr));
        contVertWork.add(txThFr, new HtmlData(".ThFr"));
        
        Label labThTo = new Label(lbFor);
        contVertWork.add(labThTo, new HtmlData(".ThLabTo"));
        txThTo.setWidth(40);
        txThTo.getCell().getInputElement(txThTo.getElement()).setMaxLength(5);
        txThTo.addValidator(new RegExValidator( maskTime, titleTime));
        txThTo.setErrorSupport(new TitleErrorHandler(txThTo));
        contVertWork.add(txThTo, new HtmlData(".ThTo"));
        
        Label labThPerFr = new Label(lbBrFr);
        contVertWork.add(labThPerFr, new HtmlData(".ThLabPerFr"));
        txThPerFr.setWidth(40);
        txThPerFr.getCell().getInputElement(txThPerFr.getElement()).setMaxLength(5);
        txThPerFr.addValidator(new RegExValidator( maskTime, titleTime));
        txThPerFr.setErrorSupport(new TitleErrorHandler(txThPerFr));
        contVertWork.add(txThPerFr, new HtmlData(".ThPerFr"));
        
        Label labThPerTo = new Label(lbFor);
        contVertWork.add(labThPerTo, new HtmlData(".ThLabPerTo"));
        txThPerTo.setWidth(40);
        txThPerTo.getCell().getInputElement(txThPerTo.getElement()).setMaxLength(5);
        txThPerTo.addValidator(new RegExValidator( maskTime, titleTime));
        txThPerTo.setErrorSupport(new TitleErrorHandler(txThPerTo));
        contVertWork.add(txThPerTo, new HtmlData(".ThPerTo"));

	rdThPer.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<Boolean> event) {
		boolean isEnable = event.getValue();
	        txThFr.setReadOnly(!isEnable); 
	        txThTo.setReadOnly(!isEnable);
	        if (isEnable){
		   txThFr.getCell().getInputElement(txThFr.getElement()).setMaxLength(5);
		   txThTo.getCell().getInputElement(txThTo.getElement()).setMaxLength(5);
	        }else{
	           txThFr.clear(); txThFr.clearInvalid();
	           txThTo.clear(); txThTo.clearInvalid();
	        }
	    }});


	cbFr.setBoxLabel("Пт");
	contVertWork.add( cbFr, new HtmlData(".Fr"));
	
	rdFrAll.setBoxLabel(lbAll);
	contVertWork.add( rdFrAll, new HtmlData(".FrAll"));
		
	rdFrPer.setBoxLabel(lbPer);
	contVertWork.add( rdFrPer, new HtmlData(".FrPer"));
	tgFr = new ToggleGroup();
	tgFr.add(rdFrAll);tgFr.add(rdFrPer);

        txFrFr.setWidth(40);
        txFrFr.getCell().getInputElement(txFrFr.getElement()).setMaxLength(5);
        txFrFr.addValidator(new RegExValidator( maskTime, titleTime));
        txFrFr.setErrorSupport(new TitleErrorHandler(txFrFr));
        contVertWork.add(txFrFr, new HtmlData(".FrFr"));
        
        Label labFrTo = new Label(lbFor);
        contVertWork.add(labFrTo, new HtmlData(".FrLabTo"));
        txFrTo.setWidth(40);
        txFrTo.getCell().getInputElement(txFrTo.getElement()).setMaxLength(5);
        txFrTo.addValidator(new RegExValidator( maskTime, titleTime));
        txFrTo.setErrorSupport(new TitleErrorHandler(txFrTo));
        contVertWork.add(txFrTo, new HtmlData(".FrTo"));
        
        Label labFrPerFr = new Label(lbBrFr);
        contVertWork.add(labFrPerFr, new HtmlData(".FrLabPerFr"));
        txFrPerFr.setWidth(40);
        txFrPerFr.getCell().getInputElement(txFrPerFr.getElement()).setMaxLength(5);
        txFrPerFr.addValidator(new RegExValidator( maskTime, titleTime));
        txFrPerFr.setErrorSupport(new TitleErrorHandler(txFrPerFr));
        contVertWork.add(txFrPerFr, new HtmlData(".FrPerFr"));
        
        Label labFrPerTo = new Label(lbFor);
        contVertWork.add(labFrPerTo, new HtmlData(".FrLabPerTo"));
        txFrPerTo.setWidth(40);
        txFrPerTo.getCell().getInputElement(txFrPerTo.getElement()).setMaxLength(5);
        txFrPerTo.addValidator(new RegExValidator( maskTime, titleTime));
        txFrPerTo.setErrorSupport(new TitleErrorHandler(txFrPerTo));
        contVertWork.add(txFrPerTo, new HtmlData(".FrPerTo"));

	rdFrPer.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<Boolean> event) {
		boolean isEnable = event.getValue();
	        txFrFr.setReadOnly(!isEnable); 
	        txFrTo.setReadOnly(!isEnable);
	        if (isEnable){
		   txFrFr.getCell().getInputElement(txFrFr.getElement()).setMaxLength(5);
		   txFrTo.getCell().getInputElement(txFrTo.getElement()).setMaxLength(5);
	        }else{
	           txFrFr.clear(); txFrFr.clearInvalid();
	           txFrTo.clear(); txFrTo.clearInvalid();
	        }
	    }});

	cbSa.setBoxLabel("Сб");
	contVertWork.add( cbSa, new HtmlData(".Sa"));
	
	rdSaAll.setBoxLabel(lbAll);
	contVertWork.add( rdSaAll, new HtmlData(".SaAll"));
		
	rdSaPer.setBoxLabel(lbPer);
	contVertWork.add( rdSaPer, new HtmlData(".SaPer"));
	tgSa = new ToggleGroup();
	tgSa.add(rdSaAll);tgSa.add(rdSaPer);

        txSaFr.setWidth(40);
        txSaFr.getCell().getInputElement(txSaFr.getElement()).setMaxLength(5);
        txSaFr.addValidator(new RegExValidator( maskTime, titleTime));
        txSaFr.setErrorSupport(new TitleErrorHandler(txSaFr));
        contVertWork.add(txSaFr, new HtmlData(".SaFr"));
        
        Label labSaTo = new Label(lbFor);
        contVertWork.add(labSaTo, new HtmlData(".SaLabTo"));
        txSaTo.setWidth(40);
        txSaTo.getCell().getInputElement(txSaTo.getElement()).setMaxLength(5);
        txSaTo.addValidator(new RegExValidator( maskTime, titleTime));
        txSaTo.setErrorSupport(new TitleErrorHandler(txSaTo));
        contVertWork.add(txSaTo, new HtmlData(".SaTo"));
        
        Label labSaPerFr = new Label(lbBrFr);
        contVertWork.add(labSaPerFr, new HtmlData(".SaLabPerFr"));
        txSaPerFr.setWidth(40);
        txSaPerFr.getCell().getInputElement(txSaPerFr.getElement()).setMaxLength(5);
        txSaPerFr.addValidator(new RegExValidator( maskTime, titleTime));
        txSaPerFr.setErrorSupport(new TitleErrorHandler(txSaPerFr));
        contVertWork.add(txSaPerFr, new HtmlData(".SaPerFr"));
        
        Label labSaPerTo = new Label(lbFor);
        contVertWork.add(labSaPerTo, new HtmlData(".SaLabPerTo"));
        txSaPerTo.setWidth(40);
        txSaPerTo.getCell().getInputElement(txSaPerTo.getElement()).setMaxLength(5);
        txSaPerTo.addValidator(new RegExValidator( maskTime, titleTime));
        txSaPerTo.setErrorSupport(new TitleErrorHandler(txSaPerTo));
        contVertWork.add(txSaPerTo, new HtmlData(".SaPerTo"));

	rdSaPer.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<Boolean> event) {
		boolean isEnable = event.getValue();
	        txSaFr.setReadOnly(!isEnable); 
	        txSaTo.setReadOnly(!isEnable);
	        if (isEnable){
		   txSaFr.getCell().getInputElement(txSaFr.getElement()).setMaxLength(5);
		   txSaTo.getCell().getInputElement(txSaTo.getElement()).setMaxLength(5);
	        }else{
	           txSaFr.clear(); txSaFr.clearInvalid();
	           txSaTo.clear(); txSaTo.clearInvalid();
	        }
	    }});

	cbSu.setBoxLabel("Вс");
	contVertWork.add( cbSu, new HtmlData(".Su"));
	
	rdSuAll.setBoxLabel(lbAll);
	contVertWork.add( rdSuAll, new HtmlData(".SuAll"));
		
	rdSuPer.setBoxLabel(lbPer);
	contVertWork.add( rdSuPer, new HtmlData(".SuPer"));
	tgSu = new ToggleGroup();
	tgSu.add(rdSuAll);tgSu.add(rdSuPer);

        txSuFr.setWidth(40);
        txSuFr.getCell().getInputElement(txSuFr.getElement()).setMaxLength(5);
        txSuFr.addValidator(new RegExValidator( maskTime, titleTime));
        txSuFr.setErrorSupport(new TitleErrorHandler(txSuFr));
        contVertWork.add(txSuFr, new HtmlData(".SuFr"));
        
        Label labSuTo = new Label(lbFor);
        contVertWork.add(labSuTo, new HtmlData(".SuLabTo"));
        txSuTo.setWidth(40);
        txSuTo.getCell().getInputElement(txSuTo.getElement()).setMaxLength(5);
        txSuTo.addValidator(new RegExValidator( maskTime, titleTime));
        txSuTo.setErrorSupport(new TitleErrorHandler(txSuTo));
        contVertWork.add(txSuTo, new HtmlData(".SuTo"));
        
        Label labSuPerFr = new Label(lbBrFr);
        contVertWork.add(labSuPerFr, new HtmlData(".SuLabPerFr"));
        txSuPerFr.setWidth(40);
        txSuPerFr.getCell().getInputElement(txSuPerFr.getElement()).setMaxLength(5);
        txSuPerFr.addValidator(new RegExValidator( maskTime, titleTime));
        txSuPerFr.setErrorSupport(new TitleErrorHandler(txSuPerFr));
        contVertWork.add(txSuPerFr, new HtmlData(".SuPerFr"));
        
        Label labSuPerTo = new Label(lbFor);
        contVertWork.add(labSuPerTo, new HtmlData(".SuLabPerTo"));
        txSuPerTo.setWidth(40);
        txSuPerTo.getCell().getInputElement(txSuPerTo.getElement()).setMaxLength(5);
        txSuPerTo.addValidator(new RegExValidator( maskTime, titleTime));
        txSuPerTo.setErrorSupport(new TitleErrorHandler(txSuPerTo));
        contVertWork.add(txSuPerTo, new HtmlData(".SuPerTo"));

	rdSuPer.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<Boolean> event) {
		boolean isEnable = event.getValue();
	        txSuFr.setReadOnly(!isEnable); 
	        txSuTo.setReadOnly(!isEnable);
	        if (isEnable){
		   txSuFr.getCell().getInputElement(txSuFr.getElement()).setMaxLength(5);
		   txSuTo.getCell().getInputElement(txSuTo.getElement()).setMaxLength(5);
	        }else{
	           txSuFr.clear(); txSuFr.clearInvalid();
	           txSuTo.clear(); txSuTo.clearInvalid();
	        }
	    }});

    }
    
    private void setFieldsEnable(boolean enable){
//        rootLogger.log(Level.INFO,  "3 getHeader().getToolCount() =  "+ getHeader().getToolCount());
	ShowErrorAddr("", false);
	if (enable){
	  if (isEditable){  
	     getHeader().removeTool(tbEdit);
	     getHeader().addTool(tbSave);
	     getHeader().addTool(tbCancel);
	  }
	  txDol.setEmptyText(cooEmpty);
	  txShi.setEmptyText(cooEmpty);
          txMoFr.setEmptyText(timeEmpty);txMoTo.setEmptyText(timeEmpty);txMoPerFr.setEmptyText(timeEmpty);txMoPerTo.setEmptyText(timeEmpty);
          txTuFr.setEmptyText(timeEmpty);txTuTo.setEmptyText(timeEmpty);txTuPerFr.setEmptyText(timeEmpty);txTuPerTo.setEmptyText(timeEmpty);
          txWeFr.setEmptyText(timeEmpty);txWeTo.setEmptyText(timeEmpty);txWePerFr.setEmptyText(timeEmpty);txWePerTo.setEmptyText(timeEmpty);
          txThFr.setEmptyText(timeEmpty);txThTo.setEmptyText(timeEmpty);txThPerFr.setEmptyText(timeEmpty);txThPerTo.setEmptyText(timeEmpty);
          txFrFr.setEmptyText(timeEmpty);txFrTo.setEmptyText(timeEmpty);txFrPerFr.setEmptyText(timeEmpty);txFrPerTo.setEmptyText(timeEmpty);
          txSaFr.setEmptyText(timeEmpty);txSaTo.setEmptyText(timeEmpty);txSaPerFr.setEmptyText(timeEmpty);txSaPerTo.setEmptyText(timeEmpty);
          txSuFr.setEmptyText(timeEmpty);txSuTo.setEmptyText(timeEmpty);txSuPerFr.setEmptyText(timeEmpty);txSuPerTo.setEmptyText(timeEmpty);
	}else{
	  if (isEditable){  
  	    if (getHeader().getToolCount() > 1){
  	      getHeader().removeTool(tbSave);
 	      getHeader().removeTool(tbCancel);
 	      getHeader().addTool(tbEdit);
	    }else if (getHeader().getToolCount() == 0)  getHeader().addTool(tbEdit);
	  }  
	  txDol.setEmptyText(null);
	  txShi.setEmptyText(null);
          txMoFr.setEmptyText(null);txMoTo.setEmptyText(null);txMoPerFr.setEmptyText(null);txMoPerTo.setEmptyText(null);
          txTuFr.setEmptyText(null);txTuTo.setEmptyText(null);txTuPerFr.setEmptyText(null);txTuPerTo.setEmptyText(null);
          txWeFr.setEmptyText(null);txWeTo.setEmptyText(null);txWePerFr.setEmptyText(null);txWePerTo.setEmptyText(null);
          txThFr.setEmptyText(null);txThTo.setEmptyText(null);txThPerFr.setEmptyText(null);txThPerTo.setEmptyText(null);
          txFrFr.setEmptyText(null);txFrTo.setEmptyText(null);txFrPerFr.setEmptyText(null);txFrPerTo.setEmptyText(null);
          txSaFr.setEmptyText(null);txSaTo.setEmptyText(null);txSaPerFr.setEmptyText(null);txSaPerTo.setEmptyText(null);
          txSuFr.setEmptyText(null);txSuTo.setEmptyText(null);txSuPerFr.setEmptyText(null);txSuPerTo.setEmptyText(null);
	}
	contVertAddr.setEnabled(enable);
	contVertWork.setEnabled(enable);
//	taAddr.setReadOnly(!enable);
//	txDol.setReadOnly(!enable);
//	cbMo.setReadOnly(!enable);
//	rdMoAll.setReadOnly(!enable);
//	rdMoPer.setReadOnly(!enable);
    }

    private void ShowErrorAddr(String E, boolean IsShow){
	taErro.setValue(E);
	taErro.setVisible(IsShow);	
}

    private native String getAddrMarkup() /*-{
    return [ '<table cellpadding=0 cellspacing=4 cols="3">',
        '<tr><td class=erro colspan=3></td></tr>',
        '<tr><td class=town colspan=3></td></tr>',
        '<tr><td class=addr></td><td class=local colspan=2></td></tr>',
        '<tr><td class=dop rowspan="2"></td><td class=dol valign="top"></td><td class=shi valign="top"></td></tr>',
        '<tr><td class=IsAll valign="top"></td><td class=Active valign="top"></td></tr>',
        '</table>'
    ].join("");
  }-*/;
    private native String getWorkMarkup() /*-{
    return [ '<table cellpadding=0 cellspacing=1 cols="10">',
        '<tr><td class=NoTrans colspan=10></td></tr>',
        '<tr><td class=Mo></td><td class=MoAll></td><td class=MoPer></td><td class=MoFr width="40"></td><td class=MoLabTo width="25" align="center"></td><td class=MoTo width="40"></td><td class=MoLabPerFr width="80" align="right"></td><td class=MoPerFr width="45" align="right"></td><td class=MoLabPerTo width="25" align="center"></td><td class=MoPerTo width="40"></td></tr>',
        '<tr><td class=Tu></td><td class=TuAll></td><td class=TuPer></td><td class=TuFr width="40"></td><td class=TuLabTo width="25" align="center"></td><td class=TuTo width="40"></td><td class=TuLabPerFr width="80" align="right"></td><td class=TuPerFr width="45" align="right"></td><td class=TuLabPerTo width="25" align="center"></td><td class=TuPerTo width="40"></td></tr>',
        '<tr><td class=We></td><td class=WeAll></td><td class=WePer></td><td class=WeFr width="40"></td><td class=WeLabTo width="25" align="center"></td><td class=WeTo width="40"></td><td class=WeLabPerFr width="80" align="right"></td><td class=WePerFr width="45" align="right"></td><td class=WeLabPerTo width="25" align="center"></td><td class=WePerTo width="40"></td></tr>',
        '<tr><td class=Th></td><td class=ThAll></td><td class=ThPer></td><td class=ThFr width="40"></td><td class=ThLabTo width="25" align="center"></td><td class=ThTo width="40"></td><td class=ThLabPerFr width="80" align="right"></td><td class=ThPerFr width="45" align="right"></td><td class=ThLabPerTo width="25" align="center"></td><td class=ThPerTo width="40"></td></tr>',
        '<tr><td class=Fr></td><td class=FrAll></td><td class=FrPer></td><td class=FrFr width="40"></td><td class=FrLabTo width="25" align="center"></td><td class=FrTo width="40"></td><td class=FrLabPerFr width="80" align="right"></td><td class=FrPerFr width="45" align="right"></td><td class=FrLabPerTo width="25" align="center"></td><td class=FrPerTo width="40"></td></tr>',
        '<tr><td class=Sa></td><td class=SaAll></td><td class=SaPer></td><td class=SaFr width="40"></td><td class=SaLabTo width="25" align="center"></td><td class=SaTo width="40"></td><td class=SaLabPerFr width="80" align="right"></td><td class=SaPerFr width="45" align="right"></td><td class=SaLabPerTo width="25" align="center"></td><td class=SaPerTo width="40"></td></tr>',
        '<tr><td class=Su></td><td class=SuAll></td><td class=SuPer></td><td class=SuFr width="40"></td><td class=SuLabTo width="25" align="center"></td><td class=SuTo width="40"></td><td class=SuLabPerFr width="80" align="right"></td><td class=SuPerFr width="45" align="right"></td><td class=SuLabPerTo width="25" align="center"></td><td class=SuPerTo width="40"></td></tr>',
        '</table>'
    ].join("");
  }-*/;

    
//    private native String getAddrMarkup() /*-{
//    return [ '<table cellpadding=0 cellspacing=0 cols="3">',
//        '<tr><td class=town colspan=3></td></tr>',
//        '<tr><td class=addr></td><td class=dop colspan=2></td></tr>',
//        '<tr><td class=local></td><td class=dol></td><td class=shi></td></tr>',
//        '</table>'
//    ].join("");
//  }-*/;
//    private native String getWorkMarkup() /*-{
//    return [ '<table cellpadding=0 cellspacing=0 cols="7">',
//        '<tr><td class=Mo></td><td class=MoAll></td><td class=MoPer></td>',
//        '<td class=Fr width="40"></td><td class=To width="40"></td><td class=PerFr width="80"></td><td class=PerTo width="40"></td></tr>',
//        '</table>'
//    ].join("");
//  }-*/;
    
//    '<tr><td class=town width=100%></td><td></td></tr>',
//    '<tr><td class=local></td><td class=dol></td><td class=shi></td></tr>',
//    <td class=shi></td>
}
