
package com.rameses.web.component.progressbar;

import com.rameses.web.common.ProgressHandler;
import java.io.IOException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class UIProgressbar extends UIComponentBase {
    
    public UIProgressbar() {
    }
    
    
    public void encodeEnd(FacesContext context) throws IOException {
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        Boolean rendered = (Boolean) getAttributes().get("rendered");
        if (rendered != null && !rendered.booleanValue()) return;
        
        String id = getClientId(context);
        String style = (String) getAttributes().get("style");
        String styleClass = (String) getAttributes().get("styleClass");
        String barStyle = (String) getAttributes().get("barStyle");
        String barStyleClass = (String) getAttributes().get("barStyleClass");
        
        ProgressHandler handler = (ProgressHandler) getAttributes().get("handler");
        Integer value = null;
        if (handler != null)
            value = handler.getProgress();
        
        style = getProgressBarContainerStyle(style);
        barStyle = getProgressBarStyle(barStyle );
        
        ResponseWriter writer = context.getResponseWriter();
        //write the progress bar container
        writer.startElement("div", this);
        writer.writeAttribute("id", id, null);
        if (styleClass != null)
            writer.writeAttribute("class", styleClass, null);
        
        writer.writeAttribute("style", style, null);
        
        //write the progress bar
        writer.startElement("div", this);
        if (barStyleClass != null)
            writer.writeAttribute("class", barStyleClass, null);
        
        barStyle = "width:" + ((value == null)? "0" : value.intValue() ) + "%;" +  barStyle;
        writer.writeAttribute("style", barStyle, null);
        writer.endElement("div");
        
        //end progressbar container
        writer.endElement("div");
    }
    
    private String getProgressBarStyle(String style) {
        String height = (String) getAttributes().get("height");
        if (height == null) height = "10";
        
        StringBuffer sstyle = new StringBuffer();
        sstyle.append("height: " + height + "px;");
        if (style != null)
            sstyle.append(style);
        else
            sstyle.append("background: #336699;");
        
        return sstyle.toString();
    }
    
    private String getProgressBarContainerStyle(String style) {
        String width = (String) getAttributes().get("width");
        String height = (String) getAttributes().get("height");
        
        if (width == null) width = "100";
        if (height == null) height = "10";
        
        StringBuffer sstyle = new StringBuffer();
        sstyle.append("height: " + height + "px;");
        sstyle.append("width: " + width + "px;");
        if (style != null) {
            sstyle.append(style);
        } else {
            sstyle.append("text-align: left; border: solid 1px gray; overflow: hidden;");
            sstyle.append("-moz-border-radius: 4px; -webkit-border-radius: 4px;");
        }
        
        return sstyle.toString();
    }
    
    public String getFamily() {
        return UIProgressbar.class.getName();
    }
    
    private void encodeScript(ResponseWriter writer) throws IOException {
        ProgressHandler handler = (ProgressHandler) getAttributes().get("handler");
        if (handler == null) return;
        
        String oncomplete = (String) getAttributes().get("oncomplete");
        if (oncomplete != null ) {
            writer.startElement("script", this);
            writer.write("eval('" + oncomplete + "');");
            writer.endElement("script");
        }
    }
    
}
