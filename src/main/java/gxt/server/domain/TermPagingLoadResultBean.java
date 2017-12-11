package gxt.server.domain;

import java.util.List;

import jpa.atm.ExtTermStat;

import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class TermPagingLoadResultBean extends PagingLoadResultBean<ExtTermStat> {
    private static final long serialVersionUID = 1L;
    protected TermPagingLoadResultBean() {
       
    }
    public TermPagingLoadResultBean(List<ExtTermStat> list, int totalLength, int offset) {
      super(list, totalLength, offset);
    }
    public Integer getVersion() {
	      return 1;
  }
  }
