package gxt.client.tool;

import java.util.List;

import jpa.atm.AtCatalog;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import gxt.client.domain.CatalogPrx;
import gxt.client.domain.FactAtm;
import gxt.client.domain.FactAtm.rcAtm;
import gxt.tool.PanExt;

public class PanCat extends PanExt<CatalogPrx>{
    private static final int PAN_WIDTH = 350;
    private static final int PAN_HEIGHT = 410;
    rcAtm reqIns;
    FactAtm f;
    interface CatProperties extends PropertyAccess<CatalogPrx> {
	    ModelKeyProvider<CatalogPrx> id();
	    ValueProvider<CatalogPrx, String> catType();
	    ValueProvider<CatalogPrx, String> name();
	  }
    private static final CatProperties propCat = GWT.create(CatProperties.class);
    ColumnConfig<CatalogPrx, String> ccType;
    ColumnConfig<CatalogPrx, String> ccName;
    TextField txType = new TextField();
    TextField txName = new TextField();

//----------------------------------------------------------- 
    public PanCat(FactAtm fct) {
	super(PAN_WIDTH, PAN_HEIGHT, "Сервисы");
	f = fct;
	ccType = new ColumnConfig<CatalogPrx, String>(propCat.catType(), 40, "Тип");
	ccName = new ColumnConfig<CatalogPrx, String>(propCat.name(), 200, "Наименование");
        getCcL().add(ccType);
        getCcL().add(ccName);
        setStT(new ListStore<CatalogPrx>(propCat.id()));
        setRfpT(new RequestFactoryProxy<ListLoadConfig, ListLoadResult<CatalogPrx>>() {
 		@Override
 		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<CatalogPrx>> receiver) {
 		  rcAtm req = f.creRcAtm();
 		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
 		  req.getAllCats(sortInfo).to(receiver).fire();
 		  }});

        initValues();
        getEditing().addEditor(ccType, txType);
        getEditing().addEditor(ccName, txName);
    }

    @Override
    public void mergItem(CatalogPrx item){
       rcAtm req = null;
       if (isIns) {req = reqIns; isIns = false;}
       else req = f.creRcAtm();
       CatalogPrx editItem = req.edit(item);
       editItem.setCatType(txType.getText());
       editItem.setName(txName.getText());
//       editItem.setF250Town(cbTown.getCurrentValue());
       req.merg(editItem).fire(mergReceiver);
    }
    @Override
    public void insItem(){
     	reqIns = f.creRcAtm();
     	CatalogPrx o = reqIns.create(CatalogPrx.class); //new ServPrx();
     	o.setName("");
     	o.setCatType("");
        stT.add(0, o);
    }
    @Override
    public String getItemName(CatalogPrx item){
	return item.getName();
    }
    @Override
    public void delItem(CatalogPrx item, Receiver<Void> R){
       f.creRcAtm().removCat(item).fire(R);
    }
    @Override
    protected void beforEdit(){
        txType.getCell().getInputElement(txType.getElement()).setMaxLength(AtCatalog.LEN_catType);
        txName.getCell().getInputElement(txName.getElement()).setMaxLength(AtCatalog.LEN_name);
    }
}
