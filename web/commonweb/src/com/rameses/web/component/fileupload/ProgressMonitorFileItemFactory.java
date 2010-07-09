
package com.rameses.web.component.fileupload;

import java.io.File;
import java.lang.ref.WeakReference;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

public class ProgressMonitorFileItemFactory extends DiskFileItemFactory {
    
    private File temporaryDirectory;
    private WeakReference<HttpServletRequest> requestRef;
    private long requestLength;
    
    public ProgressMonitorFileItemFactory(HttpServletRequest request) {
        super();
        temporaryDirectory = (File)request.getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
        requestRef = new WeakReference<HttpServletRequest>(request);
        
        String contentLength = request.getHeader("content-length");
        
        if(contentLength != null){
            requestLength = Long.parseLong(contentLength.trim());
        }
    }
    
    public FileItem createItem(String fieldName, String contentType,
            boolean isFormField, String fileName) {
        
        SessionUpdatingProgressObserver observer = null;
        
        if(isFormField == false)
            observer = new SessionUpdatingProgressObserver(fieldName,fileName, requestRef.get());
        
        ProgressMonitorFileItem item = new ProgressMonitorFileItem(fieldName,contentType,
                isFormField,fileName,
                DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,
                temporaryDirectory,
                observer,
                requestLength);
        return item;
    }
}
