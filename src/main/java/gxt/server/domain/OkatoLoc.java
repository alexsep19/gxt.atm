package gxt.server.domain;

import com.google.web.bindery.requestfactory.shared.Locator;
import jpa.atm.AtOkato;

public class OkatoLoc extends Locator<AtOkato, String> {

    @Override
    public AtOkato create(Class<? extends AtOkato> clazz) {
	// TODO Auto-generated method stub
	return new AtOkato();
    }

    @Override
    public AtOkato find(Class<? extends AtOkato> clazz, String id) {
	// TODO Auto-generated method stub
	return Dao.findAtOkato(id);
    }

    @Override
    public Class<AtOkato> getDomainType() {
	// TODO Auto-generated method stub
	return AtOkato.class;
    }

    @Override
    public String getId(AtOkato domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<String> getIdType() {
	// TODO Auto-generated method stub
	return String.class;
    }

    @Override
    public Object getVersion(AtOkato domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
