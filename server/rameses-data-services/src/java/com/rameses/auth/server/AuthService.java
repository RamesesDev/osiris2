/*
 * AuthService.java
 *
 * Created on April 8, 2010, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.auth.server;

import com.rameses.interfaces.ResourceProvider;
import com.rameses.jndi.JndiUtil;
import com.sun.jmx.remote.util.Service;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/***
 * for file types :
 *    it is stored in 
 *
 * appname/modulename/role
 *
 */
public class AuthService implements Serializable, AuthServiceMBean {
    
    private Map map = new Hashtable();
    private ResourceProvider resourceProvider;
    
    public AuthService() {
    }

    public List getPermissions(String role) {
        return getPermissions(role,null,null,null);
    }
    
    public List getPermissions(String role, String exclude, String allow, String disallow) {
        List perms = null;
        if( !map.containsKey(role) ) {
            perms = findPermissions( role );
            PermissionUtil.filter(perms,exclude,allow,disallow);
            map.put(role, perms);
        }
        else {
            perms = (List)map.get(role);
        }
        return perms;
    }

    public void start() {
        System.out.println("START AUTH SERVICE");
        try {
            JndiUtil.bind(new InitialContext(), AuthService.class.getSimpleName(), this);
            Iterator iter = Service.providers(ResourceProvider.class, Thread.currentThread().getContextClassLoader());
            while(iter.hasNext()) {
                ResourceProvider rp = (ResourceProvider)iter.next();
                if(rp.getNamespace().equals("PERMISSION")) {
                    resourceProvider = rp;
                    break;
                }
            }
            
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        System.out.println("STOPPING AUTH SERVICE");
        try {
            JndiUtil.unbind(new InitialContext(), AuthService.class.getSimpleName());
            resourceProvider = null;
            map.clear();            
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    public void flush(String role) {
        map.remove(role);
    }

    public void flushAll() {
        map.clear();
    }
    
    private List findPermissions(String name) {
        PermissionResource pr = new PermissionResource(resourceProvider);
        PermissionParser parser = new PermissionParser(pr);
        return parser.parse(name);
    }
    
}
