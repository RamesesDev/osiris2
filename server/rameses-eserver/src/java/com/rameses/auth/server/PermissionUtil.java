/*
 * PermissionUtil.java
 *
 * Created on April 9, 2010, 9:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.auth.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmo
 */
public final class PermissionUtil {
    
    public static void filter(List permissions, String exclude, String allow, String disallow) {
        filterExcludes(permissions, exclude);
        filterAllow(permissions, allow);
        filterDisallow(permissions, disallow);
        
    }
    
    public static void filterExcludes(List permissions, String exclude) {
        if(exclude!=null && exclude.trim().length()>0) {
            List excludeList = new ArrayList();
            for(Object o : permissions) {
                Permission p = (Permission)o;
                if(p.getName()!=null && p.getName().matches(exclude)) {
                    excludeList.add(p);
                }
            }
            permissions.removeAll(excludeList);
        }
    }
    
    public static void filterAllow(List permissions, String allow) {
        if(allow!=null && allow.trim().length()>0) {
            for(Object o : permissions) {
                Permission p = (Permission)o;
                if(p.getName()!=null && p.getName().matches(allow)) {
                    p.setAllowed(true);
                }
            }
        }
    }
    
    public static void filterDisallow(List permissions, String disallow) {
        if(disallow!=null && disallow.trim().length()>0) {
            for(Object o : permissions) {
                Permission p = (Permission)o;
                if(p.getName()!=null && p.getName().matches(disallow)) {
                    p.setAllowed(false);
                }
            }
        }
    }
    
}
