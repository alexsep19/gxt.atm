package gxt.server.domain;

import jpa.atm.F250Town;

import com.google.web.bindery.requestfactory.shared.Locator;

public class F250TownLoc extends Locator<F250Town, Long>{

    @Override
    public F250Town create(Class<? extends F250Town> clazz) {
	// TODO Auto-generated method stub
	return new F250Town();
    }

    @Override
    public F250Town find(Class<? extends F250Town> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findF250Town(id);
    }

    @Override
    public Class<F250Town> getDomainType() {
	// TODO Auto-generated method stub
	return F250Town.class;
    }

    @Override
    public Long getId(F250Town domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(F250Town domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
