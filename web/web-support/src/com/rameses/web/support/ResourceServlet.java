/*
 * ResourceServlet.java
 * Created on September 26, 2011, 10:33 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 */
public class ResourceServlet extends HttpServlet {
    
    protected ServletConfig config;
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }
    
    public void destroy() {
        this.config = null;
    }
    
    protected void writeResource( InputStream is, String contentType, HttpServletResponse resp ) throws Exception {
        OutputStream out = null;
        try {
            resp.setContentType(contentType);
            out = resp.getOutputStream();
            int i = 0;
            while( (i =is.read())!=-1 ) {
                out.write( i );
            }
            out.flush();
        } catch(Exception e) {
            throw e;
        } finally {
            try {is.close(); } catch(Exception e) {;}
            try {out.close(); } catch(Exception e) {;}
        }
    }
    
    protected void writeResource( String filepath, HttpServletResponse resp ) throws Exception {
        String _url = this.config.getServletContext().getInitParameter("res-url");
        if(_url==null)
            throw new Exception("ResourceServlet error. Please provide a res-url in web.xml servlet context param");
        
        if(_url.endsWith("/")){
            _url = _url + filepath.substring(1);
        } else {
            _url = _url + filepath;
        }
        String _mimeType = config.getServletContext().getMimeType(_url);
        URL u = new URL(_url);
        writeResource(u.openStream(), _mimeType, resp);
    }
    
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            writeResource( req.getPathInfo(), resp );
        } catch(Exception e) {
            e.printStackTrace();
            resp.sendError( resp.SC_INTERNAL_SERVER_ERROR, e.getMessage() );
        }
    }
    
}
