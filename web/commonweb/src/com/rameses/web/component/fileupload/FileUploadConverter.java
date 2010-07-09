package com.rameses.web.component.fileupload;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.http.HttpServletRequest;

public class FileUploadConverter implements Converter {
    
    public Object getAsObject(FacesContext context, UIComponent uIComponent, String string) {
        UIFileUpload f = (UIFileUpload)uIComponent;
        String id = f.getClientId( context );
        Object req = context.getExternalContext().getRequest();
        if( req instanceof MultipartRequestWrapper ) {
            MultipartRequestWrapper mreq = (MultipartRequestWrapper)req;
            return mreq.getFileParameters().get(id);
        }
        return null;
    }
    
    public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {
        return "mystring.file";
    }
    
}
