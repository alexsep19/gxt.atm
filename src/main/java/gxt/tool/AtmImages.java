package gxt.tool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;

public interface AtmImages extends ClientBundle{
    public static final AtmImages INSTANCE = GWT.create(AtmImages.class);
    @Source("refresh.gif") public ImageResource butRefr();
    @Source("xls.gif") public ImageResource butXls();
    @Source("loading.gif") public ImageResource butLoad();

}
