
package com.rameses.web.component.fileupload;

import com.rameses.web.common.ResourceUtil;
import com.rameses.web.common.ProgressHandler;
import java.io.IOException;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;

public class UIFileUpload extends HtmlInputHidden {
    
    public static final String PROGRESS_REQUEST_PARAM_NAME = "AJAX_FILEUPLOAD_STATUS_REQUEST";
    
    private String id;
    private String notifyKey;
    private Object value;
    private String progressHeight;
    private String progressWidth;
    private String styleClass;
    
    public UIFileUpload() {
        setConverter( new FileUploadConverter() );
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        //load javascript resources
        ResourceUtil.addScriptResource("js/jquery.js");
        ResourceUtil.addScriptResource("js/fileupload.js");
        
        id = getClientId( context );
        value = getAttributes().get("value");
        String displayInfo = (String) getAttributes().get("displayInfo");
        if (displayInfo == null) displayInfo = "true";
        
        ResponseWriter writer = context.getResponseWriter();
        
        
        if (value == null) {
            encodeFileInput(writer);
            ProgressHandler handler = (ProgressHandler) getAttributes().get("progress");
            if ( handler != null ) {
                handler.setProgress(0);
                HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
                req.getSession().setAttribute(id, handler);
            }
            
        } else if (Boolean.valueOf(displayInfo)) {
            encodeFileInfo(writer);
        }
    }
    
    public void decode(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        String clientId = getClientId(context);
        String value = (String) requestMap.get(clientId);
        
        //do nothing of no clientId found
        if(requestMap.get(clientId) == null) return;
        
        if(requestMap.containsKey(PROGRESS_REQUEST_PARAM_NAME)){
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            
            response.setContentType("text/plain");
            response.setHeader("Cache-Control", "no-cache");
            
            try {
                ResponseWriter writer = setupResponseWriter(context);
                ProgressHandler handler = (ProgressHandler) req.getSession().getAttribute(clientId);
                
                writer.write("{");
                writer.write("id : '" + clientId + "',");
                writer.write("progress: " + handler.getProgress());
                writer.write("}");
            } catch(Exception ign){;}
            
            req.setAttribute(FileuploadPhaseListener.ABORT_PHASE, true);
            
        } else {
            super.decode(context);
        }
    }
    
    private void encodeFileInput(ResponseWriter writer) throws IOException {
        writer.startElement("input", this);
        writer.writeAttribute( "id", id, null );
        writer.writeAttribute( "name", id, null );
        writer.writeAttribute( "type", "file", null );
        
        String ajax = (String) getAttributes().get("ajax");
        
        StringBuffer onchange = new StringBuffer();
        String onchangeAttr = (String) getAttributes().get("onchange");
        if (onchangeAttr != null)
            onchange.append(onchangeAttr + ";");
        
        if (Boolean.valueOf(ajax)) {
            String oncomplete = (String) getAttributes().get("oncomplete");
            String progressbar = (String) getAttributes().get("progressbar");
            String onstart = (String) getAttributes().get("onstart");
            if (onstart != null) {
                onchange.append(onstart + ";");
            }
            onchange.append("FileUpload.doUpload(this");
            onchange.append(",");
            if (oncomplete != null) {
                onchange.append("function() { " + oncomplete + "; }");
            } else {
                onchange.append("null");
            }
            onchange.append(",");
            if (progressbar != null) onchange.append("'" + progressbar + "'");
            else onchange.append("null");
            
            onchange.append(");");
        }
        
        writer.writeAttribute( "onchange", onchange.toString(), null );
        if (value != null)
            writer.writeAttribute("style", "display: none", null);
        
        writer.endElement("input");
        
        if (Boolean.valueOf(ajax)) {
            encodeIframe(writer);
        }
    }
    
    private void encodeFileInfo(ResponseWriter writer) throws IOException {
        writer.startElement("span", this);
        writer.writeAttribute("id", id , null);
        writer.write(((FileItem)value).getName());
        writer.endElement("span");
        
        writer.startElement("input", this);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", id, null);
        writer.writeAttribute("value", "_info", null);
        writer.endElement("input");
    }
    
    private void encodeIframe(ResponseWriter writer) throws IOException {
        //target of the form
        writer.startElement("iframe", this);
        writer.writeAttribute("style", "display: none", null);
        writer.writeAttribute("src", "", null);
        writer.writeAttribute("id", "target_" + id, null);
        writer.writeAttribute("name", "target_" + id, null);
        writer.endElement("iframe");
    }
    
    private ResponseWriter setupResponseWriter(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        if (writer == null) {
            HttpServletRequest request = (HttpServletRequest)
            context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse)
            context.getExternalContext().getResponse();
            
            RenderKitFactory renderFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = renderFactory.getRenderKit(context, context.getViewRoot().getRenderKitId());
            
            writer =  renderKit.createResponseWriter(response.getWriter(),"text/html", request.getCharacterEncoding());
            context.setResponseWriter(writer);
        }
        
        return writer;
    }
    
}
