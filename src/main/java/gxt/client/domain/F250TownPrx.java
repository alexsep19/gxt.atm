package gxt.client.domain;

import jpa.atm.F250Town;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = F250Town.class, locator = gxt.server.domain.F250TownLoc.class)
public interface F250TownPrx extends EntityProxy{
    public long getId();
    public String getName();
    public void setName(String name);
    public String getRusName();
    public void setRusName(String rusName);
    public OkatoPrx getAtOkato();
    public void setAtOkato(OkatoPrx atOkato);
    public String getTelCode();
    public void setTelCode(String telCode);
	public String getAlfaBank();
	public void setAlfaBank(String alfaBank);

    Integer getVersion();
}
