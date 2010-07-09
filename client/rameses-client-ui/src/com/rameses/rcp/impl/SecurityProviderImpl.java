package com.rameses.rcp.impl;

import com.rameses.rcp.framework.ClientSecurityProvider;
import java.util.ArrayList;
import java.util.List;

public class SecurityProviderImpl implements ClientSecurityProvider {
    
    private List<String> permissions = new ArrayList<String>();
    private List<String> roles = new ArrayList<String>();
    
    public SecurityProviderImpl() {
    }
    
    public boolean checkRoles(String name) {
        if(name==null) return true;
        for(String s: roles) {
            if(name.matches(s)) return true;
        }
        return false;
    }
    
    public boolean checkPermission(String name) {
        if(name==null) return true;
        for(String s: permissions) {
            if(name.matches(s)) return true;
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
