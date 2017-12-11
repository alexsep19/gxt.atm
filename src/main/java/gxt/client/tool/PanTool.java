package gxt.client.tool;

import java.util.ArrayList;
import java.util.List;

import gxt.client.domain.CatalogPrx;
import gxt.client.domain.FactAtm;
import gxt.client.domain.FactAtm.rcAtm;
import gxt.client.terms.PanAddr;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import java.util.logging.Logger;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class PanTool extends ContentPanel{
    private static final int WIN_WIDTH = 1150;
    private static final int WIN_HEIGHT = 850;
    
    public PanTool(final FactAtm fct){
	getHeader().addStyleName("txt_center");
	addStyleName("margin-10");
	setHeadingText("Справочники");
	setPixelSize(WIN_WIDTH, WIN_HEIGHT);
        HtmlLayoutContainer contMain = new HtmlLayoutContainer(getMainMarkup());
        contMain.add(new PanML(fct), new HtmlData(".line"));
        contMain.add(new PanMS(fct), new HtmlData(".station"));
        contMain.add(new PanCat(fct), new HtmlData(".cat"));
        contMain.add(new PanTown(fct), new HtmlData(".town"));
        setWidget(contMain);
    }

    private native String getMainMarkup() /*-{
    return [ '<table cellpadding=0 cellspacing=4 cols="3">',
        '<tr><td class=line valign="top"></td><td class=station rowspan=2 valign="top"></td><td class=town rowspan=2 valign="top"></td></tr>',
        '<tr><td class=cat valign="top"></td></tr>',
        '</table>'
    ].join("");
  }-*/;

}
