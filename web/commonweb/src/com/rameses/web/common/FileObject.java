/*
 * FileObject.java
 *
 * Created on May 12, 2009, 9:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class FileObject implements Serializable {
    
    private String contentType;
    private byte[] content;
    
    public FileObject(String contentType, byte[] content) {
        this.setContentType(contentType);
        this.content = content;
    }
    
    public FileObject() {
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    
}
