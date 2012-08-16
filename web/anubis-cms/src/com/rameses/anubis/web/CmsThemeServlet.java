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
import com.rameses.anubis.Theme;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Elmo
 */
public class CmsThemeServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest hreq, HttpServletResponse hres) throws ServletException, IOException {
        
        ServletContext app =  super.getServletContext();
        Project project = AnubisContext.getCurrentContext().getProject();
        
        String pathInfo = hreq.getPathInfo();
        String path = hreq.getServletPath() + pathInfo;
        String urlPath = project.getUrl() + path;
        
        String themeName = pathInfo.substring(1,  pathInfo.indexOf("/",1));
        String resName = pathInfo.substring( pathInfo.indexOf("/",1) );
        String mimeType = app.getMimeType( pathInfo );
        
        
        InputStream is = null;
        try {
            Theme theme = null;
            if(themeName.equals("system")) {
                theme = project.getSystemTheme();
            } else {
                theme =  project.getThemes().get(themeName);
            }
            is = theme.getResource( resName );
            
            if( is != null ) {
                ResponseWriter.write(app,hreq,hres,mimeType,is);
            }
        } catch(Exception e) {
            System.out.println("error theme resource" + e.getMessage());
        } finally {
            try {is.close();} catch(Exception ign){;}
        }
        
    }
    
    
}
