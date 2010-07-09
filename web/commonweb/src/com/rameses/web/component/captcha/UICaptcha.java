
package com.rameses.web.component.captcha;

import java.io.IOException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

public class UICaptcha extends UIComponentBase{
    
    public UICaptcha() {
    }
    
    public String getFamily() {
        return null;
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        ResponseWriter writer = context.getResponseWriter();
        
        String id = getClientId(context);
        String height = (String) getAttributes().get("height");
        String width = (String) getAttributes().get("width");
        String style = (String) getAttributes().get("style");
        String styleClass = (String) getAttributes().get("styleClass");
        StringBuffer url = new StringBuffer();
        url.append(req.getRequestURI());
        url.append("?" + CaptchaPhaseListener.CAPTCHA_PARAM + "=true");
        
        writer.startElement("div", this);
        writer.writeAttribute("id", id, null);
        
        writer.startElement("img", this);
        writer.writeAttribute("src", url.toString(),  null);
        writer.writeAttribute("alt", "jcaptcha image", null);
        if(height != null)
            writer.writeAttribute("height", height, null);
        if(width != null)
            writer.writeAttribute("width", width, null);
        if(style != null)
            writer.writeAttribute("style", style, null);
        if(styleClass != null)
            writer.writeAttribute("styleClass", styleClass, null);
        
        writer.endElement("img");
        
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        context.getResponseWriter().endElement("div");
    }
    
}
