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
        String path = hreq.getPathInfo();
        String mimeType = app.getMimeType( path );
        InputStream is = null;
        try {
            if( path.indexOf("/",1)>0) {
                String testModname = path.substring(1, path.indexOf("/",1));
                if( project.getModules().containsKey(testModname)) {
                    String res = path.substring( path.indexOf("/",1 ));
                    is = project.getModules().get(testModname).getResource(res);
                }
            }
            if(is==null) is = project.getResource(path);
            if( is!=null) {
                ResponseWriter.write( app, hreq, hres, mimeType, is );
            }
        } catch(Exception e) {
            System.out.println("error " + e.getMessage());
        } finally {
            try {is.close();} catch(Exception ign){;}
        }
        
    }
    
    
}
