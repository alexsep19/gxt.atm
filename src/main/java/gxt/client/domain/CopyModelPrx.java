package gxt.client.domain;

import gxt.server.domain.CopyModel;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(CopyModel.class /*, locator = gxt.server.domain.PartLoc.class*/)
@SuppressWarnings("requestfactory")
public interface CopyModelPrx extends EntityProxy{
	public Long getId();
	public void setId(Long id);
	public List<String> getMess();
	public List<String> getModel();
	public Integer getVersion();
}
