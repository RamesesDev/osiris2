package com.rameses.web.fileupload;

import javax.servlet.http.HttpServletRequest;

public class ProgressObserver {
    
    private String fieldName;
    private HttpServletRequest request;
    
    private long totalRead;
    private long totalSize;
    private long startTime = System.currentTimeMillis();
    
    
    public ProgressObserver(String fieldName, HttpServletRequest request, long length){
        this.fieldName = fieldName;
        this.request = request;
        this.totalSize = length;
        
        if( request != null ) {
            request.getSession().setAttribute(this.fieldName, new ProgressStatus());
        }
    }
    
    public void incrementBytesRead(long bytesRead) {
        totalRead += bytesRead;
        if(request != null){
            computeStatus();
        }
    }
    
    private void computeStatus() {
        ProgressStatus status = (ProgressStatus) request.getSession().getAttribute(this.fieldName);
        if( status != null ) {
            long timeInSeconds = getElapsedTimeInSeconds();
            double uploadRate = totalRead / (timeInSeconds + 0.00001);
            
            status.setPercentCompleted( (long)Math.floor(((double)totalRead / (double)totalSize) * 100.0) );
            status.setUploadRate( uploadRate );
            status.setEstimatedRuntime( totalSize / (uploadRate + 0.00001) );
        }
        
    }
    
    private long getElapsedTimeInSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
    
}