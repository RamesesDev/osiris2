/*
 * ValueUtil.java
 *
 * Created on June 21, 2010, 3:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

/**
 *
 * @author compaq
 */
public class ValueUtil {
    
    public static final boolean isEqual(Object obj1, Object obj2) {
        if ( obj1 == null && obj2 == null)
            return true;
        if ( obj1 == null && obj2 != null )
            return false;
        if ( obj1 != null && obj2 == null )
            return false;
        
        return obj1.equals(obj2);
    }
    
    public static final boolean isEmpty(Object obj) {
        if ( obj == null) 
            return true;
        if ( obj instanceof String && ((String) obj).trim().length() == 0)
            return true;
        
        return false;
    }
    
}
