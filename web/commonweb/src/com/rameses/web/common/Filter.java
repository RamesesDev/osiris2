/*
 * Filter.java
 *
 * Created on September 5, 2008, 9:13 AM
 *
 */
package com.rameses.web.common;

import com.rameses.web.component.fileupload.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaycverg
 */
public class Filter extends org.ajax4jsf.Filter {
    
    public static final int BUFFER_SIZE = 10240;
    public static final String RESOURCE_FOLDER = "META-INF";
    public static final String RESOURCE_KEY = "/a4j.res";
    
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String path = req.getServletPath();
        if ( path != null ) {
            String regex = "^" + RESOURCE_KEY + "(/.+)$";
            Matcher m = Pattern.compile(regex).matcher(path);
            if ( m.matches() ) {
                String res = RESOURCE_FOLDER + m.group(1);
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(res);
                if ( is != null ) {
                    String mimeType = req.getSession().getServletContext().getMimeType(path);
                    resp.setContentType(mimeType);
                    resp.addHeader("Cache-Control", "max-age=86400");
                    resp.addHeader("Cache-Control", "public");
                    serveResource(is, resp.getOutputStream());
                    return;
                }
            }
        }
        
        if( req.getHeader("content-type")!=null ) {
            if( req.getHeader("content-type").startsWith("multipart")) {
                servletRequest = new MultipartRequestWrapper( req );
            }
        }
        
        super.doFilter( servletRequest, servletResponse, chain );
    }
    
    public void destroy() {
        //null
    }
    
    private void serveResource(InputStream is, OutputStream os) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            bis = new BufferedInputStream(is, BUFFER_SIZE);
            bos = new BufferedOutputStream(os, BUFFER_SIZE);
            
            int bytesRead = -1;
            while( (bytesRead=bis.read(buffer)) != -1 ) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
            
        } catch (Exception ign) {
            //ignore
        } finally {
            try{bos.close();}catch(Exception ign) {;}
            try{bis.close();}catch(Exception ign) {;}
        }
    }
    
}
