package gxt.server.domain;

import jpa.atm.AtTel;
import jpa.atm.AtTelStat;

import com.google.web.bindery.requestfactory.shared.Locator;

public class TelLoc extends Locator<AtTel, Long>{

    @Override
    public AtTel create(Class<? extends AtTel> clazz) {
	// TODO Auto-generated method stub
	return new AtTelStat();
    }

    @Override
    public AtTel find(Class<? extends AtTel> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findAtTel(id);
    }

    @Override
    public Class<AtTel> getDomainType() {
	// TODO Auto-generated method stub
	return AtTel.class;
    }

    @Override
    public Long getId(AtTel domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(AtTel domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
