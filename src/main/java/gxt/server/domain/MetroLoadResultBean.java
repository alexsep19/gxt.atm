package gxt.server.domain;

import java.util.List;

import jpa.atm.AtMetro;
import jpa.atm.AtMetroStat;

import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class MetroLoadResultBean extends ListLoadResultBean<AtMetroStat>{
    private static final long serialVersionUID = -1677506933925499719L;
    public MetroLoadResultBean() {
    }
    public MetroLoadResultBean(List<AtMetroStat> list) {
	      super(list);
	    }
    public Integer getVersion() { return 1;}
}
