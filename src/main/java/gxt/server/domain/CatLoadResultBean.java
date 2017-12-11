package gxt.server.domain;

import java.util.List;
import jpa.atm.AtCatalog;
import jpa.atm.AtCatalogStat;

import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class CatLoadResultBean extends ListLoadResultBean<AtCatalogStat>{
    private static final long serialVersionUID = 3370614316551134662L;
    public CatLoadResultBean() {
	// TODO Auto-generated constructor stub
    }
    public CatLoadResultBean(List<AtCatalogStat> list) {
	      super(list);
	    }
    public Integer getVersion() { return 1;}

}
