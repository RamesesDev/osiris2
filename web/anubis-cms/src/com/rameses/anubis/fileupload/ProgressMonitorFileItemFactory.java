package com.rameses.anubis.fileupload;

import java.io.File;
import java.lang.ref.WeakReference;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

public class ProgressMonitorFileItemFactory extends DiskFileItemFactory {
    
    private File temporaryDirectory;
    private WeakReference<HttpServletRequest> requestRef;
    private long requestLength;
    private ServletContext servletCtx;
    
    public ProgressMonitorFileItemFactory(HttpServletRequest request, ServletContext servletCtx) 
    {
        super();
        this.servletCtx = servletCtx;
        temporaryDirectory = (File)servletCtx.getAttribute("javax.servlet.context.tempdir");
        requestRef = new WeakReference<HttpServletRequest>(request);
        
        String contentLength = request.getHeader("content-length");
        
        if(contentLength != null){
            requestLength = Long.parseLong(contentLength.trim());
        }
    }
    
    public FileItem createItem(
        String fieldName, 
        String contentType,
        boolean isFormField, 
        String fileName
    ) 
    {
        
        ProgressObserver observer = null;
        
        if(isFormField == false) {
            observer = new ProgressObserver(fieldName, requestRef.get(), servletCtx, requestLength);
        }
        
        ProgressMonitorFileItem item = new ProgressMonitorFileItem(
                fieldName,
                contentType,
                isFormField,
                fileName,
                DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,
                temporaryDirectory,
                observer
        );
        
        return item;
    }
}
