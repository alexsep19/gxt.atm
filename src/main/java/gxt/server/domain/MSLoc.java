package gxt.server.domain;

import jpa.atm.AtMstation;

import com.google.web.bindery.requestfactory.shared.Locator;

public class MSLoc extends Locator<AtMstation, Long>{

    @Override
    public AtMstation create(Class<? extends AtMstation> clazz) {
	// TODO Auto-generated method stub
	return new AtMstation(){
		@Override
		public String toString(){return this.getAtMetroLine().getName();
		}
	};
    }

    @Override
    public AtMstation find(Class<? extends AtMstation> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findAtMstation(id);
    }

    @Override
    public Class<AtMstation> getDomainType() {
	// TODO Auto-generated method stub
	return AtMstation.class;
    }

    @Override
    public Long getId(AtMstation domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(AtMstation domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

//    @Override
//    public String toString(){
//	return this.atMetroLine.getName();
//    }
}
