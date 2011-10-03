/*
 * MapBeanUtil.java
 *
 * Created on January 13, 2011, 6:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ms
 *
 * This utility acts like bean utils but used exclusively for Map.
 *
 */
public final class MapBeanUtils {
    
    public static void setProperty( Map target, String name, Object value ) {
        if( name.indexOf(".")>0) {
            String fldname = name.substring(0, name.indexOf("."));
            String remainder = name.substring( name.indexOf(".")+1 );
            Object m = target.get(fldname);
            if(m==null) {
                Map child = new HashMap();
                target.put(fldname,child);
                setProperty(child, remainder, value);
            } else if(m instanceof Map) {
                setProperty((Map)m, remainder, value);
            } else {
                System.out.println("cannot set property on object type " + name + " for:" + m.getClass());
            }
        } else {
            target.put(name, value);
        }
    }
    
    
    
}
