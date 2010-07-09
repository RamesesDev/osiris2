/*
 * Osiris2WebSecurityProvider.java
 *
 * Created on May 27, 2010, 11:22 AM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.SecurityProvider;
import java.util.ArrayList;
import java.util.List;


public class Osiris2WebSecurityProvider implements SecurityProvider {
    
    private List<String> permissions = new ArrayList();
    
    public Osiris2WebSecurityProvider() {
    }

    public boolean checkRoles(String name) {
        return true;
    }

    public boolean checkPermission(String name) {
        if ( name == null ) return true;
        
        for(String s: permissions) {
            if ( name.matches(s) ) return true;
        }
        return false;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
    
}
