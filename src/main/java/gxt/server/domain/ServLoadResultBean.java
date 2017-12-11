package gxt.server.domain;

import java.util.List;

import jpa.atm.AtServ;
import jpa.atm.AtServStat;

import com.sencha.gxt.data.shared.loader.ListLoadResultBean;
public class ServLoadResultBean extends ListLoadResultBean<AtServStat>{
    private static final long serialVersionUID = 1L;

    public ServLoadResultBean() {
    }
    public ServLoadResultBean(List<AtServStat> list) {
      super(list);
    }
    public Integer getVersion() { return 1;}
}
