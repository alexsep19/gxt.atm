package gxt.client.domain;

import java.util.List;

import gxt.server.domain.CatLoadResultBean;
import gxt.server.domain.Dao;
import gxt.server.domain.DaoServ;
import gxt.server.domain.MLLoadResultBean;
import gxt.server.domain.MSPagingLoadResultBean;
import gxt.server.domain.MetroLoadResultBean;
import gxt.server.domain.ServLoadResultBean;
import gxt.server.domain.TelLoadResultBean;
import gxt.server.domain.TermPagingLoadResultBean;
import gxt.server.domain.TownPagingLoadResultBean;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;

public interface FactAtm extends RequestFactory{
    rcAtm creRcAtm();
    
    @Service(value = Dao.class, locator = DaoServ.class)
    public interface rcAtm extends RequestContext{
	Request<String> getUserInfo();
	Request<String> getFIs();
	Request<String> getRole();

	Request<CopyModelPrx> doTestCopy(String s);
	Request<CopyModelPrx> doTrueCopy(List<String> atms);
	
	Request<List<String>> loadMdm();
	Request<Boolean> isUpdateRun();
	Request<List<String>> getSessionUsers();
	
	Request<List<CatalogPrx>> pageCatByType(String type, TermStPrx t);
	Request<List<MSPrx>> pageMSByTerm(TermStPrx t);
	Request<Boolean> mertoInTown(TermStPrx Term);
	Request<List<F250TownPrx>> getAllTown();
	Request<List<MLPrx>> getAllLine();
	Request<List<OkatoPrx>> getAllOkato();
	Request<List<PartPrx>> getAllPart();
//	Request<List<PartPrx>> getAllRec(int s);
		
	Request<Void> merg(TermStPrx rec);
	Request<Void> merg(ServPrx rec);
	Request<Void> merg(TelPrx rec);
	Request<Void> merg(MetroPrx rec);
	Request<Void> merg(MLPrx rec);
	Request<Void> merg(MSPrx rec);
	Request<Void> merg(CatalogPrx rec);
	Request<Void> merg(F250TownPrx rec);
	
	Request<Void> removServ(ServPrx rec);
	Request<Void> removTel(TelPrx rec);
	Request<Void> removMetro(MetroPrx rec);
	Request<Void> removML(MLPrx rec);
	Request<Void> removCat(CatalogPrx rec);
	Request<Void> removMS(MSPrx rec);
	Request<Void> removTown(F250TownPrx rec);
		
	@ProxyFor(TermPagingLoadResultBean.class)
	public interface TermPagingLoadResultProxy extends ValueProxy, PagingLoadResult<TermStPrx> {
	    @Override
	    public List<TermStPrx> getData();
	  }
	Request<TermPagingLoadResultProxy> getTerms(int offset, int limit, List<? extends SortInfo> sortInfo, List<? extends FilterConfig> filterConfig);
	   
	@ProxyFor(MSPagingLoadResultBean.class)
	public interface MSPagingLoadResultProxy extends ValueProxy, PagingLoadResult<MSPrx> {
	    @Override
	    public List<MSPrx> getData();
	  }
	Request<MSPagingLoadResultProxy> getPageMS(int offset, int limit, List<? extends SortInfo> sortInfo, List<? extends FilterConfig> filterConfig);

	@ProxyFor(TownPagingLoadResultBean.class)
	public interface TownPagingLoadResultProxy extends ValueProxy, PagingLoadResult<F250TownPrx> {
	    @Override
	    public List<F250TownPrx> getData();
	  }
	Request<TownPagingLoadResultProxy> getPageTown(int offset, int limit, List<? extends SortInfo> sortInfo, List<? extends FilterConfig> filterConfig);

	@ProxyFor(ServLoadResultBean.class)
	public interface ServLoadResultProxy extends ValueProxy, ListLoadResult<ServPrx> {
	    @Override
	    public List<ServPrx> getData();
	  }
	Request<ServLoadResultProxy> getServs(List<? extends SortInfo> sortInfo, TermStPrx t);

	@ProxyFor(TelLoadResultBean.class)
	public interface TelLoadResultProxy extends ValueProxy, ListLoadResult<TelPrx> {
	    @Override
	    public List<TelPrx> getData();
	  }
	Request<TelLoadResultProxy> getTels(List<? extends SortInfo> sortInfo, TermStPrx t);

	@ProxyFor(MetroLoadResultBean.class)
	public interface MetroLoadResultProxy extends ValueProxy, ListLoadResult<MetroPrx> {
	    @Override
	    public List<MetroPrx> getData();
	  }
	Request<MetroLoadResultProxy> getMetros(List<? extends SortInfo> sortInfo, TermStPrx t);

	@ProxyFor(MLLoadResultBean.class)
	public interface MLLoadResultProxy extends ValueProxy, ListLoadResult<MLPrx> {
	    @Override
	    public List<MLPrx> getData();
	  }
	Request<MLLoadResultProxy> getMLs(List<? extends SortInfo> sortInfo);
	 
	@ProxyFor(CatLoadResultBean.class)
	public interface CatLoadResultProxy extends ValueProxy, ListLoadResult<CatalogPrx> {
	    @Override
	    public List<CatalogPrx> getData();
	  }
	Request<CatLoadResultProxy> getAllCats(List<? extends SortInfo> sortInfo);
	 
    }
}
