
package com.rameses.web.common;

import java.io.IOException;
import java.io.Serializable;

public interface ImageCropper extends Serializable {
    
    public byte[] crop() throws IOException;
    
    public String getFileType();
    
    public String getContentType();
    
}
