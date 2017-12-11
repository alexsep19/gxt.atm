package gxt.server.domain;

import jpa.atm.AtMetroLine;

import com.google.web.bindery.requestfactory.shared.Locator;

public class MLLoc extends Locator<AtMetroLine, Long>{

    @Override
    public AtMetroLine create(Class<? extends AtMetroLine> clazz) {
	// TODO Auto-generated method stub
	return new AtMetroLine();
    }

    @Override
    public AtMetroLine find(Class<? extends AtMetroLine> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findAtMetroLine(id);
    }

    @Override
    public Class<AtMetroLine> getDomainType() {
	// TODO Auto-generated method stub
	return AtMetroLine.class;
    }

    @Override
    public Long getId(AtMetroLine domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(AtMetroLine domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
