package gxt.client.domain;

import jpa.atm.AtPart;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = AtPart.class, locator = gxt.server.domain.PartLoc.class)
public interface PartPrx extends EntityProxy{
    public Integer getVersion();
    public long getId();
    public void setId(long id);
    public String getName();
    public void setName(String name);
}
