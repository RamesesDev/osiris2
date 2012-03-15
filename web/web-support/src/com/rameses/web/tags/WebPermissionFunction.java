/*
 * WebPermissionFunction.java
 * Created on March 15, 2012, 7:44 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.tags;


import java.util.List;

/**
 *
 * @author jzamss
 */
public class WebPermissionFunction {
    
    public static boolean checkPermission(List permissions, String key) {
        if(permissions==null) return true;
        
        for(Object o: permissions) {
            if( key.matches( (String)o) ) return true;
        }
        return false;
    }
    
}
