/*
 * WebResource.java
 *
 * Created on July 30, 2010, 10:09 AM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


public class WebResource implements Serializable {
    
    private String filename;
    private String contentType;
    private Object data;
    private boolean cacheable = true;
    private boolean forceDownload = false;
    private Map headers = new LinkedHashMap();
    
    public WebResource() {}

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isForceDownload() {
        return forceDownload;
    }

    public void setForceDownload(boolean forceDownload) {
        this.forceDownload = forceDownload;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }
    
    public void addHeader(String name, Object value) {
        headers.put(name, value);
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public String getContentType() {
        if ( contentType == null && filename != null ) {
            return WebContext.getServletContext().getMimeType(filename);
        }
        
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
}
