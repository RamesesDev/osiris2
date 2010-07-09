/*
 * ResStreamHandlerFactory.java
 *
 * Created on October 29, 2009, 7:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;


public class ResURLStreamHandlerFactory implements URLStreamHandlerFactory {
    
    private ClassLoader classLoader;
    
    public ResURLStreamHandlerFactory(ClassLoader loader) {
        classLoader = loader;
    }
    
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if("res".equalsIgnoreCase(protocol)){
            return new resURLStreamHandler();
        }
        return null;
    }
    
    public class resURLStreamHandler extends URLStreamHandler{
        private String m;
        
        protected URLConnection openConnection(URL u) throws IOException {
            resURLConnection url = new resURLConnection(u,m);
            url.connect();
            return url;
        }
        
        protected void parseURL(URL u, String spec, int start, int limit) {
            m = spec.substring(spec.indexOf("/")+2,limit);
            //starting from here you call the default parseURL method
            super.parseURL(u,spec,start,limit);
        }
        
    }
    
    public class resURLConnection extends URLConnection{
        private URLConnection sourceURLConn;
        private String path;
        public resURLConnection(URL url, String path){
            super(url);
            this.path = path;
            setDoOutput(true);
        }
        
        public synchronized  void connect() throws IOException {
            URL sourceURL = classLoader.getResource(path);
            if( sourceURL != null) 
                this.sourceURLConn = sourceURL.openConnection();
        }
        
        public synchronized  OutputStream getOutputStream() throws IOException {
            if(sourceURLConn != null)
                return sourceURLConn.getOutputStream();
            return super.getOutputStream();
        }
        
        public synchronized  InputStream getInputStream() throws IOException {
            if(sourceURLConn != null)
                return sourceURLConn.getInputStream();
            return super.getInputStream();
        }
        
        public Object getContent() throws IOException {
            if( sourceURLConn != null)
                return sourceURLConn.getContent();
            return super.getContent();
        }
        
        public String getContentType() {
            if(sourceURLConn != null)
                return sourceURLConn.getContentType();
            return super.getContentType();
        }
        
        public int getContentLength() {
            if(sourceURLConn != null)
                return sourceURLConn.getContentLength();
            return super.getContentLength();
        }
        
        public String getContentEncoding() {
            if(sourceURLConn != null)
                return sourceURLConn.getContentEncoding();
            return super.getContentEncoding();
        }
        
    }
    
}
