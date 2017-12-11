package gxt.client.domain;

import java.util.List;

import jpa.atm.AtMstation;
import jpa.atm.AtMstationStat;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = AtMstationStat.class, locator = gxt.server.domain.MSLoc.class)
public interface MSPrx extends EntityProxy{
	public long getId();
	public void setId(long id);
	public String getName();
	public void setName(String name);
	public List<MetroPrx> getAtMetros();
	public void setAtMetros(List<MetroPrx> atMetros);
	public MLPrx getAtMetroLine();
	public void setAtMetroLine(MLPrx atMetroLine);
//	public String getLineName();
	Integer getVersion();
	
}
