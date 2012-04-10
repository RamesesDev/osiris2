/*
 * MapBeanUtil.java
 *
 * Created on January 13, 2011, 6:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    
    /*******
     * copying the properties
     */
    private static void scanCopy( Map src, Map target) {
        for(Object o : src.entrySet() ) {
            Map.Entry me = (Map.Entry)o;
            Object value = me.getValue();
            Object newValue = null;
            if( value instanceof Map ) {
                Map _map = new HashMap();
                scanCopy( (Map)value, _map );
                newValue = _map;
            } else if( value instanceof List ) {
                List _lst = new ArrayList();
                Iterator iter = ((List)value).iterator();
                while(iter.hasNext()) {
                    Object x = iter.next();
                    if( x instanceof Map ) {
                        Map _smap = new HashMap();
                        scanCopy( (Map)x, _smap );
                        _lst.add( _smap );
                    } else {
                        _lst.add( x );
                    }
                }
                newValue = _lst;
            } else {
                newValue = value;
            }
            target.put( me.getKey(), newValue );
        }
    }
    
    public static Map copy(Map m ) {
        Map newMap = new HashMap();
        scanCopy( m, newMap );
        return newMap;
    }
    
}
