/*
 * CmsResourceServlet.java
 *
 * Created on June 28, 2012, 8:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Elmo
 */
public class CmsResourceServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process( req, resp );
    }
    
    protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpServletRequest hreq = (HttpServletRequest)req;
        HttpServletResponse hres = (HttpServletResponse)resp;
        
        String urlPath = null;
        ServletContext app = super.getServletContext();
        String mimeType = app.getMimeType( hreq.getServletPath() );
        InputStream is = null;
        OutputStream os = null;
        try {
            hres.setContentType( mimeType );
            os = hres.getOutputStream();
            URL u = new URL( urlPath );
            is = new BufferedInputStream(u.openStream(), 8 * 1024 );
            int i = 0;
            while( (i=is.read()) != -1 ) {
                os.write( i );
            }
        } catch(Exception e ) {
            e.printStackTrace();
        } finally {
            try { os.flush();  } catch(Exception ign) {;}
            try { os.close();  } catch(Exception ign) {;}
            try { is.close(); } catch(Exception ign) {;}
        }
    }
    
    
}
