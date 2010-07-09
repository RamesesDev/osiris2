
package com.rameses.web.component.fileupload;

import com.rameses.web.common.ProgressHandler;
import javax.servlet.http.HttpServletRequest;

public class SessionUpdatingProgressObserver implements ProgressObserver {
    private String fieldName;
    private String fileName;
    private HttpServletRequest request;
    
    public SessionUpdatingProgressObserver(String fieldName, String fileName, HttpServletRequest request){
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.request = request;
    }
    
    public void setProgress(int progress) {
        if(request != null){
            ProgressHandler handler = (ProgressHandler) request.getSession().getAttribute(fieldName);
            if ( handler != null ) {
                handler.setProgress(progress);
            }
        }
    }
}
