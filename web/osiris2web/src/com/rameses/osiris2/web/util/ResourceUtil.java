/*
 * ResourceUtil.java
 *
 * Created on July 30, 2010, 11:57 AM
 * @author jaycverg
 */

package com.rameses.osiris2.web.util;

import com.rameses.osiris2.Module;
import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.web.WebContext;
import com.rameses.osiris2.web.WebResource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.server.UID;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ResourceUtil {
    
    public static final int BUFFER_SIZE = 10240;
    
    
    public static final void renderResource(WebResource webRes) {
        //thread safe rendering
        ResourceRenderer renderer = new ResourceRenderer();
        renderer.renderResource(webRes);
    }
    
    public static final void renderResource(InputStream is, OutputStream os) {
        //thread safe rendering
        ResourceRenderer renderer = new ResourceRenderer();
        renderer.renderResource(is, os);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  ResourceRenderer (class)  ">
    private static class ResourceRenderer {
        
        private static final Pattern MOD_RES_FORMAT = Pattern.compile("^/?([^/]+)/(.+)$");
        
        public void renderResource(WebResource webRes) {
            InputStream is = null;
            
            Object resource = webRes.getData();
            if ( resource instanceof byte[] ) {
                is = new ByteArrayInputStream( (byte[])resource );
                
            } else if ( resource instanceof InputStream ) {
                is = (InputStream) resource;
                
            } else if ( resource instanceof File ) {
                try {
                    is = new FileInputStream( (File)resource );
                } catch (FileNotFoundException ign) {;}
                
            } else if ( resource instanceof String ) {
                Matcher m = MOD_RES_FORMAT.matcher(resource+"");
                if ( m.matches() ) {
                    try {
                        SessionContext ctx = WebContext.getInstance().getSessionContext();
                        Module mod = ctx.getModule( m.group(1) );
                        is = mod.getResourceAsStream( m.group(2) );
                    }
                    //module not found
                    catch(Exception ign) {;}
                }
                
            }
            
            HttpServletResponse resp = WebContext.getInstance().getResponse();
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            if ( is != null ) {
                String ctype = webRes.getContentType();
                if ( ctype != null ) {
                    resp.setContentType(ctype);
                }
                if ( webRes.isCacheable() ) {
                    resp.addHeader("Cache-Control", "max-age=86400");
                    resp.addHeader("Cache-Control", "public");
                }
                
                //add headers from WebResource if specified
                Set<Map.Entry> headers = webRes.getHeaders().entrySet();
                for(Map.Entry me: headers ) {
                    resp.addHeader( me.getKey()+"", me.getValue()+"" );
                }
                
                String fname = webRes.getFilename();
                if ( fname == null || fname.trim().length() == 0 ) {
                    fname = "FILE" + new UID();
                }
                
                String disposition = "inline";
                if ( webRes.isForceDownload() ) {
                    disposition = "attachment";
                }
                resp.setHeader("Content-Disposition", disposition + ";filename=" + fname );
                
                try {
                    renderResource(is, resp.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
            } else {
                try {
                    HttpServletRequest req = WebContext.getInstance().getRequest();
                    StringBuffer resPath = new StringBuffer();
                    resPath.append( req.getRequestURI() );
                    if ( req.getQueryString() != null ) {
                        resPath.append("?" + req.getQueryString());
                    }
                    
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, resPath.toString());
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        public void renderResource(InputStream is, OutputStream os) {
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            try {
                byte []buffer = new byte[BUFFER_SIZE];
                bis = new BufferedInputStream(is, BUFFER_SIZE);
                bos = new BufferedOutputStream(os, BUFFER_SIZE);
                
                int bytesRead = -1;
                while ( (bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                bos.flush();
                
            } catch(Exception ex) {
                throw new IllegalStateException(ex);
            } finally {
                try { bis.close(); } catch(Exception e){;}
                try { bos.close(); } catch(Exception e){;}
            }
        }
        
    }
    //</editor-fold>
}
