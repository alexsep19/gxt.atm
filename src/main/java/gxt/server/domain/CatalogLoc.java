package gxt.server.domain;

import jpa.atm.AtCatalog;
import jpa.atm.AtCatalogStat;

import com.google.web.bindery.requestfactory.shared.Locator;

public class CatalogLoc extends Locator<AtCatalog, Long>{

    @Override
    public AtCatalog create(Class<? extends AtCatalog> clazz) {
	// TODO Auto-generated method stub
	return new AtCatalogStat(); 
    }

    @Override
    public AtCatalog find(Class<? extends AtCatalog> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findAtCatalog(id);
    }

    @Override
    public Class<AtCatalog> getDomainType() {
	// TODO Auto-generated method stub
	return AtCatalog.class;
    }

    @Override
    public Long getId(AtCatalog domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(AtCatalog domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }


}
