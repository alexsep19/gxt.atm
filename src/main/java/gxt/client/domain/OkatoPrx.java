package gxt.client.domain;

import jpa.atm.AtOkato;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = AtOkato.class, locator = gxt.server.domain.OkatoLoc.class)
public interface OkatoPrx extends EntityProxy{
    public String getId();
    public String getName();
    public Integer getVersion();
}
