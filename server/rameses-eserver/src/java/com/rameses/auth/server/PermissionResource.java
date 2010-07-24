/*
 * PermissionResource.java
 *
 * Created on April 9, 2010, 9:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.auth.server;

import com.rameses.eserver.ResourceProvider;
import java.io.InputStream;

/**
 *
 * @author elmo
 */
public class PermissionResource {
    

    private ResourceProvider resourceProvider;
    
    public PermissionResource(ResourceProvider p) {
        this.resourceProvider = p;
    }
    
    public InputStream getIputStream(String name) throws Exception {
        InputStream is = null;
        if(resourceProvider!=null) {
            is = resourceProvider.getResource(name);
        }
        if(is==null) {
            if(!name.startsWith("/")) name = "/" + name;
            String path = "META-INF/permissions" + name;
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        }
        if(is == null )
            throw new Exception("Resource " + name + " not found!");
        return is;
    }
    
    
}
