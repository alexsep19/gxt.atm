package gxt.client.domain;

import java.util.List;

import jpa.atm.AtMetroLine;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = AtMetroLine.class, locator = gxt.server.domain.MLLoc.class)
public interface MLPrx extends EntityProxy{
	public long getId();
	public void setId(long id);
	public String getName();
	public void setName(String name);
	public F250TownPrx getF250Town();
	public void setF250Town(F250TownPrx f250Town);
	public List<MSPrx> getAtMstations();
	public void setAtMstations(List<MSPrx> atMstations);
	public String getColor();
	public void setColor(String color);
	public Integer getVersion();
}
