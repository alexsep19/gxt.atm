package gxt.client.domain;

import jpa.atm.AtTel;
import jpa.atm.AtTelStat;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = AtTelStat.class, locator = gxt.server.domain.TelLoc.class)
public interface TelPrx extends EntityProxy{
	public long getId();
	public void setId(long id);
	public String getNum();
	public void setNum(String num);
	public TermStPrx getAtTerm();
	public void setAtTerm(TermStPrx atTerm);
	
	Integer getVersion();

}
