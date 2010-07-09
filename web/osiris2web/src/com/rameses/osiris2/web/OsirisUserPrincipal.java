/*
 * OsirisUserPrincipal.java
 *
 * Created on May 24, 2010, 2:24 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OsirisUserPrincipal implements Principal, Serializable {
    
    private String username;
    private List permissions = new ArrayList();
    private Map properties = new HashMap();
    
    public OsirisUserPrincipal() {
    }

    public String getName() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List getPermissions() {
        return permissions;
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }
    
}
