/*
 * PermissionFinder.java
 *
 * Created on August 4, 2010, 3:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

import java.util.List;

public class PermissionFinder implements PermissionFilter {
    
    private List<String> excludes;
    
    public PermissionFinder(List<String> excludes) {
        this.excludes = excludes;
    }
    
    public boolean accept(Permission perm) {
        //if the name matches in any of the excludes,
        //it is immediately considered not permitted
        if(excludes==null) return true;
        for( String s: excludes ) {
            if(perm.getName().matches(s)) return false;
        }
        return true;
    }
}
