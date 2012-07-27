/*
 * CmsStartup.java
 *
 * Created on June 27, 2012, 6:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author Elmo
 */
public class CmsStartup extends HttpServlet {
    
    public void init(ServletConfig config) throws ServletException {
        System.out.println("************* STARTING ANUBIS CMS WEB SERVER *************** ");
        FileInputStream fis = null;
        try {
            ServletContext appContext = config.getServletContext();
            
            //load the url patterns here.
            File file = new File("anubis/hosts");
            fis = new FileInputStream(file);
            Properties conf = new Properties();
            conf.load( fis );
            

            String scached = System.getProperty("cached", "false");
            boolean cached = Boolean.valueOf(scached);
            
            ProjectResolver resolver = new ProjectResolver(conf);
            resolver.setCached( cached );
            
            URL u = appContext.getResource("/anubis.lib");
            resolver.setDefaultModule( new AnubisDefaultModule( u.toString()) );
            resolver.setDefaultTheme( new AnubisDefaultTheme( u.toString()) );
            
            appContext.setAttribute( ProjectResolver.class.getName(), resolver );
            
        } catch(Exception ex) {
            throw new ServletException(ex);
        } finally {
            try { fis.close(); } catch(Exception e) {;}
        }
    }
    
}
