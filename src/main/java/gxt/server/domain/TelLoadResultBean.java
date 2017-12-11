package gxt.server.domain;

import java.util.List;

import jpa.atm.AtTel;
import jpa.atm.AtTelStat;

import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class TelLoadResultBean extends ListLoadResultBean<AtTelStat>{
    private static final long serialVersionUID = 4414005133201276718L;
    public TelLoadResultBean() {
    }
    public TelLoadResultBean(List<AtTelStat> list) {
	      super(list);
	    }
    public Integer getVersion() { return 1;}
}
