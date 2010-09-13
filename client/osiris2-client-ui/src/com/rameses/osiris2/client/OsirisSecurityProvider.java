/*
 * OsirisSecurityProvider.java
 *
 * Created on October 17, 2009, 10:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.SecurityProvider;
import com.rameses.rcp.framework.ClientSecurityProvider;
import java.util.ArrayList;
import java.util.List;

public class OsirisSecurityProvider implements SecurityProvider, ClientSecurityProvider {
    
    private List<String> permissions = new ArrayList<String>();
    private List<String> roles = new ArrayList<String>();
    
    public OsirisSecurityProvider() {
    }

    public boolean checkRoles(String name) {
        if(name==null) return true;
        for(String s: roles) {
            if(name.matches(s)) return true;
        }
        return false;
    }

    public boolean checkPermission(String name) {
        if (name == null) return true;
        for (String s: permissions) {
            if (name.matches(s)) return true;
        }
        return false;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    
    
}
