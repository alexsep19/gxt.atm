package gxt.client.domain;

import jpa.atm.AtServ;
import jpa.atm.AtServStat;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = AtServStat.class, locator = gxt.server.domain.ServLoc.class)
public interface ServPrx extends EntityProxy{
    public long getId();
    public CatalogPrx getAtCatalog();
    public void setAtCatalog(CatalogPrx atCatalog);
    public TermStPrx getAtTerm();
    public void setAtTerm(TermStPrx atTerm);
    
//    public int getCount();
    Integer getVersion();
    
//    public String getRendName();
}
