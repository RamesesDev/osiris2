/*
 * CmsStartup.java
 *
 * Created on June 27, 2012, 6:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.web;

import com.rameses.cms.CmsManager;
import java.io.File;
import java.io.FileInputStream;
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
         
        System.out.println("config is " + config);
        System.out.println("************* STARTING CMS WEB SERVER *************** ");
        FileInputStream fis = null;
        try {
            ServletContext appContext = config.getServletContext();
            
            //find the cms.conf in the user home. If it
            System.out.println(System.getProperty("user.home"));
            
            //store this in the path of jetty.
            File file = new File("cms.conf");
            fis = new FileInputStream(file);
            
            Properties conf = new Properties();
            conf.load( fis );
           
            CmsManager cmsManager = new CmsManager( conf, getClass().getClassLoader()  );
            appContext.setAttribute( CmsWebConstants.CONF, cmsManager );
            
        } 
        catch(Exception ex) {
            throw new ServletException(ex);
        } finally {
            try { fis.close(); } catch(Exception e) {;}
        }
    }
    
}
