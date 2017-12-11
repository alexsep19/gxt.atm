package gxt.client.domain;


import jpa.atm.AtCatalogStat;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = AtCatalogStat.class, locator = gxt.server.domain.CatalogLoc.class)
public interface CatalogPrx extends EntityProxy{
    public long getId();
    public String getCatType();
    public void setCatType(String catType);
    public String getName();
    public void setName(String name);

//    public int getCount();
    Integer getVersion();
}
