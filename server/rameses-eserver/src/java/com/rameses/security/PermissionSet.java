/*
 * PermissionSet.java
 *
 * Created on August 4, 2010, 2:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PermissionSet implements Serializable {
    
    private String name;
    private List<Permission> permissions = new ArrayList();
    
    public PermissionSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }
    
    public void addPermission(Permission p ) {
        if(permissions.indexOf(p)>=0) return;
        permissions.add(p);
    }
    
}
