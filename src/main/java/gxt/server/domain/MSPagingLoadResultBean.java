package gxt.server.domain;

import java.util.List;

import jpa.atm.AtMstation;
import jpa.atm.AtMstationStat;

import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class MSPagingLoadResultBean extends PagingLoadResultBean<AtMstationStat>{
    private static final long serialVersionUID = 5547667310399557547L;
    public MSPagingLoadResultBean() {
	// TODO Auto-generated constructor stub
    }
    public MSPagingLoadResultBean(List<AtMstationStat> list, int totalLength, int offset) {
	      super(list, totalLength, offset);
	    }
	    public Integer getVersion() {
		      return 1;
	  }
}
