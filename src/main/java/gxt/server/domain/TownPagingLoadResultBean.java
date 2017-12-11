package gxt.server.domain;

import java.util.List;

import jpa.atm.F250Town;

import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class TownPagingLoadResultBean extends PagingLoadResultBean<F250Town>{
    private static final long serialVersionUID = 419349240926635590L;
    public TownPagingLoadResultBean() {
    }
    public TownPagingLoadResultBean(List<F250Town> list, int totalLength, int offset) {
	      super(list, totalLength, offset);
	    }
	    public Integer getVersion() {
		      return 1;
	  }
}
