package com.rameses.anubis.fileupload;

import com.rameses.anubis.AnubisContext;
import com.rameses.anubis.Project;
import com.rameses.anubis.web.CmsWebConstants;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ProgressObserver {
    
    private String fieldName;
    private HttpServletRequest request;
    private ServletContext servletCtx;
    
    private long totalRead;
    private long totalSize;
    private long startTime = System.currentTimeMillis();
    
    
    public ProgressObserver(String fieldName, HttpServletRequest request, ServletContext servletCtx, long length){
        this.fieldName = fieldName;
        this.request = request;
        this.totalSize = length;
        this.servletCtx = servletCtx;
    }
    
    public void incrementBytesRead(long bytesRead) {
        totalRead += bytesRead;
        if(request != null){
            computeStatus();
        }
    }
    
    private void computeStatus() {
        ProgressStatus status = new ProgressStatus();
        long timeInSeconds = getElapsedTimeInSeconds();
        double uploadRate = totalRead / (timeInSeconds + 0.00001);

        status.setPercentCompleted( (long)Math.floor(((double)totalRead / (double)totalSize) * 100.0) );
        status.setUploadRate( uploadRate );
        status.setEstimatedRuntime( totalSize / (uploadRate + 0.00001) );
        updateStatus( status );
    }
    
    private long getElapsedTimeInSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
    
    private void updateStatus(ProgressStatus status) {
        try {
            Project p = AnubisContext.getCurrentContext().getProject();
            
            Map param = new HashMap();
            param.put("requestid", this.fieldName);
            param.put("status", status.toMap());
            p.getActionManager().getActionCommand(CmsWebConstants.FILE_UPLOAD_UPDATE_STATUS_CMD).execute(param, null);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}