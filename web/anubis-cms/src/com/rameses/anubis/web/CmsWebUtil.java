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
    
}
