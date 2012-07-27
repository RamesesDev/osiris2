/*
 * CmsWebUtil.java
 *
 * Created on July 6, 2012, 8:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import com.rameses.anubis.fileupload.MultipartRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Elmo
 */
public class CmsWebUtil {
    
    private static int DEFAULT_BUFFER_SIZE = 1024;
    
    public static void output(ServletContext app, String mimeType, InputStream is, HttpServletRequest hreq, HttpServletResponse hres) throws ServletException, IOException{
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        try {
            if(mimeType==null) mimeType = "text/html";
            hres.setBufferSize(DEFAULT_BUFFER_SIZE);
            hres.setContentType(mimeType);
            //hres.setHeader("Content-Length", String.valueOf(input.available()));
            //hres.setHeader("Content-Disposition", "inline; filename=\"" + u.getFile() + "\"");
            
            // Open streams.
            input = new BufferedInputStream(is, DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(hres.getOutputStream(), DEFAULT_BUFFER_SIZE);
            
            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } finally {
            // Gently close streams.
            close(is);
            close(output);
            close(input);
        }
    }
    
    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it.
                //e.printStackTrace();
                System.out.println("closing resource " + e.getMessage());
            }
        }
    }
    
    
    public static Map buildRequestParams(HttpServletRequest hreq) {
        Map params = new HashMap();
        Enumeration e = hreq.getParameterNames();
        while(e.hasMoreElements()) {
            String name = (String)e.nextElement();
            params.put( name, hreq.getParameter(name) );
        }
        
        if( hreq instanceof MultipartRequest ) {
            MultipartRequest mreq = (MultipartRequest) hreq;
            Map fparams = mreq.getFileParameterMap();
            for(Map.Entry<String,List> item : (Set<Map.Entry>) fparams.entrySet()) {
                List l = item.getValue();
                if( l.size() == 1 ) {
                    params.put( item.getKey(), l.get(0) );
                } else {
                    params.put( item.getKey(), l );
                }
            }
        }
        
        return params;
    }
    
    public static void setCachedHeader(HttpServletResponse hres ) {
        hres.setHeader("cache-control", "public");
        hres.setHeader("cache-control", "max-age: 86400");
    }
    
}
