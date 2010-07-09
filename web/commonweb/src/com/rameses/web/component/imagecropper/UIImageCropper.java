
package com.rameses.web.component.imagecropper;

import com.rameses.web.common.ByteArrayImageCropper;
import com.rameses.web.common.ResourceUtil;
import java.io.IOException;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;


public class UIImageCropper extends HtmlInputHidden {
    
    public UIImageCropper() {
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        //load javascripts and css
        ResourceUtil.addScriptResource("js/jquery.js");
        ResourceUtil.addScriptResource("js/jcrop.js");
        ResourceUtil.addCSSResource("css/jcrop.css");
        
        ResponseWriter writer = context.getResponseWriter();
        String parentId = getParent().getClientId(context);
        String hiddenId = getClientId(context);
        StringBuilder script = new StringBuilder();
        script.append("$(function() {$('*[id*=" + parentId + "]').Jcrop({");
        script.append("onSelect : function(c) { $('input[id*=" + hiddenId + "]').val(c.x + ',' + c.y + ',' + c.w + ',' + c.h); }");
        script.append("});});");
        
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write(script.toString());
        writer.endElement("script");
        
        writer.startElement("input", this);
        writer.writeAttribute("name", hiddenId, null);
        writer.writeAttribute("id", hiddenId, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", "", null);
        writer.endElement("input");
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        
    }
    
    public void decode(FacesContext context) {
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        String hiddenValue = req.getParameter(getClientId(context));
        
        if (hiddenValue == null) return;
        String[] coord = hiddenValue.split(",");
        
        String imageDir = (String) getParent().getAttributes().get("value");
        int fileIdIdx = imageDir.indexOf("file_id");
        if (fileIdIdx == -1) return;
        
        String expr = imageDir.substring(fileIdIdx + "file_id=".length());
        expr = "#{" + expr + "}";
        ValueBinding vb = context.getApplication().createValueBinding(expr);
        FileItem fileItem = (FileItem) vb.getValue(context);
        
        ByteArrayImageCropper cropper = (ByteArrayImageCropper) getValueBinding("handler").getValue(context);
        if (cropper == null) {
            cropper = new ByteArrayImageCropper();
            getValueBinding("handler").setValue(context, cropper);
        }
        
        String contentType = fileItem.getContentType();
        String fileType = contentType.substring(contentType.indexOf("/") + 1);
        
        try {
            int x = Integer.parseInt(coord[0]);
            int y = Integer.parseInt(coord[1]);
            int w = Integer.parseInt(coord[2]);
            int h = Integer.parseInt(coord[3]);
            cropper.setClip(x, y, w, h);
        } catch(NumberFormatException ex) {;}
        
        cropper.setFileType(fileType);
        cropper.setContentType(contentType);
        cropper.setSourceImage(fileItem.get());
    }
    
}
