package gxt.server.domain;


import jpa.atm.AtMetro;
import jpa.atm.AtMetroStat;
import jpa.atm.AtServ;

import com.google.web.bindery.requestfactory.shared.Locator;

public class MetroLoc extends Locator<AtMetro, Long>{

    @Override
    public AtMetro create(Class<? extends AtMetro> clazz) {
	// TODO Auto-generated method stub
	return new AtMetroStat();
    }

    @Override
    public AtMetro find(Class<? extends AtMetro> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findAtMetro(id);
    }

    @Override
    public Class<AtMetro> getDomainType() {
	// TODO Auto-generated method stub
	return AtMetro.class;
    }

    @Override
    public Long getId(AtMetro domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(AtMetro domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
