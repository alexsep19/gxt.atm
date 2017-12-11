package gxt.server.domain;


import jpa.atm.AtPart;

import com.google.web.bindery.requestfactory.shared.Locator;

public class PartLoc extends Locator<AtPart, Long>{

    @Override
    public AtPart create(Class<? extends AtPart> clazz) {
	// TODO Auto-generated method stub
	return new AtPart();
    }

    @Override
    public AtPart find(Class<? extends AtPart> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findAtPart(id);
    }

    @Override
    public Class<AtPart> getDomainType() {
	// TODO Auto-generated method stub
	return AtPart.class;
    }

    @Override
    public Long getId(AtPart domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(AtPart domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
