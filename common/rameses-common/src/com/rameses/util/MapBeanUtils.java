/*
 * MapBeanUtil.java
 *
 * Created on January 13, 2011, 6:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ms
 */
public final class MapBeanUtils {
    
    public static void setProperty( Object bean, String name, Object value ) {
        if(bean instanceof Map) {
            Map mbean = (Map)bean;
            if( !name.contains(".") || name.contains("[")) {
                mbean.put( name, value );
            } 
            else if( name.startsWith("[")) {
                
            } 
            else {
                String contextName = name.substring(0, name.indexOf("."));
                String propName = name.substring( name.indexOf(".") + 1 );
                Object b = mbean.get( contextName );
                setProperty( b, propName, value );
            }
        }
        else if(bean instanceof List) {
            
        }
        
        /*
            if(name.contains(".")) {
                //split the names. get the parent
                String arr[] = name.substring(0, name.lastIndexOf(".")).split("\\.");
                name = name.substring(name.lastIndexOf(".")+1);
                //we should get the last name
                for(String s: arr ) {
                    if(s.contains("[")) {
                        String xname = s.substring(0, s.indexOf("["));
                        int idx = Integer.valueOf( s.substring(s.indexOf("[")+1,s.indexOf("]"))).intValue();
                        List list = (List)context.get(xname);
                        context = (Map)list.get(idx);
                    } else {
                        context = (Map)context.get(s);
                    }
                }
            }
         */
        
    }
    
}
