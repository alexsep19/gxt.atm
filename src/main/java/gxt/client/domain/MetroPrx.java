package gxt.client.domain;

import jpa.atm.AtMetro;
import jpa.atm.AtMetroStat;
import jpa.atm.AtMstation;
import jpa.atm.ExtTermStat;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = AtMetroStat.class, locator = gxt.server.domain.MetroLoc.class)
public interface MetroPrx extends EntityProxy{
	public long getId();
	public void setId(long id);
	public MSPrx getAtMstation();
	public void setAtMstation(MSPrx atMstation);
	public TermStPrx getAtTerm();
	public void setAtTerm(TermStPrx atTerm);

	Integer getVersion();
}
