/*
 * SessionContext.java
 *
 * Created on July 6, 2012, 3:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class SessionContext {
    
    //this is used to store any kind of information that will be retained
    //until the request is completed
    private Map info = new HashMap();

    public Map getInfo() {
        return info;
    }
    
    public abstract String getSessionid();
    public abstract Map createSession(Map info);
    public abstract Map destroySession();
    public abstract Map getSession();
    public abstract Map getUserPrincipal();
    public abstract boolean hasPermission(String key);
    public abstract boolean hasRole(String role);

    public boolean isLoggedIn() {
        return ( getSession()!=null );
    }
    
    
}

