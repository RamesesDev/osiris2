package com.rameses.web.component.richtext;

import com.rameses.web.common.Filter;
import com.rameses.web.common.ResourceUtil;
import java.io.IOException;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class UIRichText extends UIInput {
  
    public UIRichText() {
        
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        ResourceUtil.addScriptResource("js/richtext.js");
        ResourceUtil.addCSSResource("css/richtext.css");
        
        ResponseWriter writer = context.getResponseWriter();
        String width = (String) getAttributes().get("width");
        String height = (String) getAttributes().get("height");
        String value = (String) getAttributes().get("value");
        String type = (String) getAttributes().get("type");
        Boolean rendered = (Boolean) getAttributes().get("rendered");
        
        if (!rendered.booleanValue()) return;
        
        if(width == null)  width = "400";
        if(height == null) height = "100";
        if(value == null)  value = "&nbsp;";
        if(type == null)   type = "basic";

        String name = getClientId(context);
        String resPath = context.getExternalContext().getRequestContextPath() + Filter.RESOURCE_KEY;
        
        writer.startElement("div", this);
        writer.writeAttribute("class", "rteDiv", null);
        writer.writeAttribute("id", "wrapper"+ name , null);
        
        writer.startElement("script", this);
        writer.write("init();");
        writer.write(
                "writeRichText('"+name+"', '"+value.replace("\n", "").replace("\r", "")+
                "', '"+width+"', '"+height+"', '"+type+"', '" + resPath + "');"
                );
        writer.endElement("script");
        writer.write("<br>");
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        context.getResponseWriter().endElement("div");
    }    
}
