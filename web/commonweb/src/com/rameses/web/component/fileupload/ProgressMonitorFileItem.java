
package com.rameses.web.component.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;

public class ProgressMonitorFileItem extends DiskFileItem {
    
    private ProgressObserver observer;
    private long passedInFileSize;
    private long bytesRead;
    
    private boolean isFormField;
    
    public ProgressMonitorFileItem(String fieldName, String contentType,
            boolean isFormField, String fileName,
            int sizeThreshold, File repository,
            ProgressObserver observer,
            long passedInFileSize) {
        super(fieldName, contentType, isFormField, fileName, sizeThreshold, repository);
        this.observer = observer;
        this.passedInFileSize = passedInFileSize;
        this.isFormField = isFormField;
    }
    
    public OutputStream getOutputStream() throws IOException {
        OutputStream baseOutputStream = super.getOutputStream();
        if(isFormField == false){
            return new BytesCountingOutputStream(baseOutputStream);
        }else{
            return baseOutputStream;
        }
    }
    
    private class BytesCountingOutputStream extends OutputStream{
        
        private long previousProgressUpdate;
        private OutputStream base;
        
        public BytesCountingOutputStream(OutputStream ous){
            base = ous;
        }
        
        public void close() throws IOException {
            base.close();
        }
        
        public boolean equals(Object arg0) {
            return base.equals(arg0);
        }
        
        public void flush() throws IOException {
            base.flush();
        }
        
        public int hashCode() {
            return base.hashCode();
        }
        
        public String toString() {
            return base.toString();
        }
        
        public void write(byte[] bytes, int offset, int len) throws IOException {
            base.write(bytes, offset, len);
            fireProgressEvent(len);
        }
        
        public void write(byte[] bytes) throws IOException {
            base.write(bytes);
            fireProgressEvent(bytes.length);
        }
        
        public void write(int b) throws IOException {
            base.write(b);
            fireProgressEvent(1);
        }
        
        private void fireProgressEvent(int b){
            bytesRead += b;
            int progress = (int) Math.round((bytesRead / (double)passedInFileSize) * 100.0);
            observer.setProgress(progress);
            previousProgressUpdate = bytesRead;
        }
    }
}
