package gxt.server.domain;
import jpa.atm.AtServ;
import jpa.atm.AtServStat;

import com.google.web.bindery.requestfactory.shared.Locator;

public class ServLoc extends Locator<AtServ, Long>{

    @Override
    public AtServ create(Class<? extends AtServ> clazz) {
	// TODO Auto-generated method stub
	return new AtServStat();
    }

    @Override
    public AtServ find(Class<? extends AtServ> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findAtServ(id);
    }

    @Override
    public Class<AtServ> getDomainType() {
	// TODO Auto-generated method stub
	return AtServ.class;
    }

    @Override
    public Long getId(AtServ domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(AtServ domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
