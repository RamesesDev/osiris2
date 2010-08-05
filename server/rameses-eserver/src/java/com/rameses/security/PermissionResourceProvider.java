/*
 * PermissionResourceProvider.java
 *
 * Created on August 4, 2010, 5:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

import com.rameses.eserver.MultiResourceHandler;
import com.rameses.eserver.ResourceProvider;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 *
 * @author elmo
 */
public class PermissionResourceProvider extends ResourceProvider {
    
    /** Creates a new instance of PermissionResourceProvider */
    public PermissionResourceProvider() {
    }
    
    public String getName() {
        return "permission";
    }
    
    public String getDescription() {
        return "Permission Resource [permission://]";
    }
    
    public int getPriority() {
        return 0;
    }
    
    public boolean accept(String nameSpace) {
        return nameSpace.equals("permission");
    }
    
    public InputStream getResource(String name) throws Exception {
        throw new Exception("getResource is not applicable for permissions");
    }
    
    public void scanResources(String name, MultiResourceHandler handler) throws Exception {
        String fileName = null;
        if(name.equals("roledomains"))  fileName = "META-INF/roledomain.conf";
        else fileName = "META-INF/permissions.xml";
        Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources(fileName);
        while(e.hasMoreElements()) {
            URL u = e.nextElement();
            System.out.println("parsing permission for " + u.getPath());
            InputStream is = null;
            try {
                is = u.openStream();
                handler.handle(is, u.getPath() );
            } catch(Exception ex) {
                System.out.println("error loading permission for [" + u.getPath() + "]->" + ex.getMessage());
            } finally {
                try {is.close();} catch(Exception ign){;}
            }
        }
    }
    
}
