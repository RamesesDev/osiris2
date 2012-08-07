/*
 * CmsResourceServlet.java
 *
 * Created on June 28, 2012, 8:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import com.rameses.anubis.AnubisContext;

import com.rameses.anubis.Project;
import java.io.IOException;
import java.io.InputStream;
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
    
    protected void doGet(HttpServletRequest hreq, HttpServletResponse hres) throws ServletException, IOException {
        ServletContext app =  super.getServletContext();
        //run the CMS file
        
        Project project = AnubisContext.getCurrentContext().getProject();
        String path = hreq.getServletPath();
        if(hreq.getPathInfo()!=null) path = path + hreq.getPathInfo();
        String urlPath = project.getUrl() + path;
        
        String mimeType = app.getMimeType( path );
        URL u = new URL( urlPath );
        InputStream is = null;
        try {
            CmsWebUtil.setCachedHeader(hres);
        
            is = u.openStream();
            CmsWebUtil.output( app,mimeType, is, hreq, hres );
        }
        catch(Exception e) {
            System.out.println("error " + e.getMessage());
        }
        finally {
            try {is.close();} catch(Exception ign){;}
        }
        
    }
    
    
}
