package com.rameses.web.component.outputlink;

import com.rameses.web.common.FileObjectRenderer;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

public class UIOutputLink extends UIOutput {
    
    public static final String OUTPUT_LINK_HIDDEN = "output_link_hidden";
    
    private String type;
    
    public UIOutputLink() {
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        type = (String) getAttributes().get("type");
        if (type == null) type = "link";
        
        String caption = (String) getAttributes().get("caption");
        if (caption == null) caption = "";
        
        String id = getClientId(context);
        
        String hiddenId = getFormId(context) + ":_" + OUTPUT_LINK_HIDDEN;
        String target = (String) getAttributes().get("target");
        StringBuffer script = new StringBuffer();
        script.append("return (function(){" +
            "var input = document.getElementById('" + hiddenId + "');" +
            "if(input)input.value='" + id + "';" +
            "if(input && input.form){ ");
        
        if (target != null)
            script.append("input.form.target='" + target + "';");
        
        script.append("input.form.submit();}" +
            "return false;" +
            "})();");
        
        if (type.matches("link")) {
            writer.startElement("a", this);
            writer.writeAttribute("href", "#", null);
            writer.writeAttribute("id", id, null);
            writer.writeAttribute("onclick", script.toString(), null);
            writer.write(caption);
        } else if (type.matches("button")) {
            writer.startElement("input", this);
            writer.writeAttribute("type", "button", null);
            writer.writeAttribute("id", id, null);
            writer.writeAttribute("value", caption, null);
            writer.writeAttribute("onclick", script.toString(), null);
            writer.endElement("input");
        }
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        if (type.matches("link"))
            writer.endElement("a");
        
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        String hiddenId = getFormId(context) + ":_" + OUTPUT_LINK_HIDDEN;
        if ( req.getAttribute(hiddenId) == null) {
            String id = getClientId(context);
            writer.startElement("input", this);
            writer.writeAttribute("name", hiddenId, null);
            writer.writeAttribute("id", hiddenId, null);
            writer.writeAttribute("type", "hidden", null);
            writer.endElement("input");
        }
    }
    
    public void decode(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        String clientId = getClientId(context);
        String hiddenId = getFormId(context) + ":_" + OUTPUT_LINK_HIDDEN;
        String requestId = (String) requestMap.get(hiddenId);
        
        //do nothing if no clientId found
        if(requestId == null || !requestId.equals(clientId)) return;
        
        Object value = getAttributes().get("value");
        String download = (String) getAttributes().get("download");
        String filename = (String) getAttributes().get("filename");
        String linkId = clientId + "_" + hiddenId;
        
        context.getExternalContext().getSessionMap().put(linkId, value);
        
        
        StringBuffer surl = new StringBuffer();
        surl.append( context.getExternalContext().getRequestContextPath() );
        if( context.getExternalContext().getRequestServletPath() != null ) {
            surl.append( context.getExternalContext().getRequestServletPath() );
        }
        surl.append(context.getViewRoot().getViewId());
        surl.append( "?" + FileObjectRenderer.LINK_ID + "=" + URLEncoder.encode(linkId) );
        if (download != null && download.matches("true|false"))
            surl.append("&download=" + download);
        if (filename != null)
            surl.append("&filename=" + URLEncoder.encode(filename));
        
        try {
            context.getExternalContext().redirect(surl.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            context.responseComplete();
        }
    }
    
    private String getFormId(FacesContext context) {
        UIComponent form = getParent();
        while( form != null) {
            if (form instanceof UIForm) break;
            form = form.getParent();
        }
        if (form != null)
            return form.getClientId(context);
        
        throw new IllegalStateException("outputLink must be embedded in a form");
    }
    
}
