package gxt.server.domain;


import jpa.atm.ExtTermStat;

import com.google.web.bindery.requestfactory.shared.Locator;

public class TermLoc extends Locator<ExtTermStat, Long>{

    @Override
    public ExtTermStat create(Class<? extends ExtTermStat> clazz) {
	// TODO Auto-generated method stub
	return new ExtTermStat();
    }

    @Override
    public ExtTermStat find(Class<? extends ExtTermStat> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findAtTerm(id);
    }

    @Override
    public Class<ExtTermStat> getDomainType() {
	// TODO Auto-generated method stub
	return ExtTermStat.class;
    }

    @Override
    public Long getId(ExtTermStat domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(ExtTermStat domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
