
package com.rameses.web.chart;

import com.sun.facelets.tag.jsf.html.AbstractHtmlLibrary;

public class ChartTagLibrary extends AbstractHtmlLibrary {
    public static final String NAME_SPACE = "http://com.rameses.web.chart";
    public static final ChartTagLibrary INSTANCE = new ChartTagLibrary();

    public ChartTagLibrary() {
        super(NAME_SPACE);            
        addHtmlComponent("chart", UIChart.class.getName(), null);
    }
}
