package gxt.server.domain;

import java.util.List;
import jpa.atm.AtMetroLine;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class MLLoadResultBean extends ListLoadResultBean<AtMetroLine>{
    private static final long serialVersionUID = 3614762613134692203L;
    public MLLoadResultBean() {
    }
    public MLLoadResultBean(List<AtMetroLine> list) {
	      super(list);
	    }
    public Integer getVersion() { return 1;}

}
