/*
 * ParserUtil.java
 *
 * Created on February 16, 2009, 2:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import com.rameses.common.PropertyResolver;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.xml.sax.Attributes;

/**
 *
 * @author elmo
 */
public final class ParserUtil {
    
    public static void loadAttributes( Object o , Map extended, Attributes attributes, PropertyResolver resolver ) {
        if(resolver==null)
            throw new IllegalStateException("Property resolver must be specified in ParserUtil.loadAttributes");
        
        for( int i = 0 ; i<attributes.getLength();i++ ) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            Object val = value;

            try {
                
                Class clz = resolver.getPropertyType(o,name);;
                if( clz.equals(Integer.class) ) {
                    val = Integer.valueOf(value);
                } 
                else if( clz.equals(Double.class) ) {
                    val = Double.valueOf(value) ;
                } 
                else if( clz.equals(Boolean.class) ) {
                    val = Boolean.valueOf(value);
                }
                /*
                else if( clz == Double.TYPE ) {
                    val = Double.parseDouble(value+"") ;
                }
                else if( clz == Boolean.TYPE ) {
                    val = Boolean.parseBoolean(value+"") ;
                }
                 */
                resolver.setProperty(o,name,val);
            } 
            catch (Exception ex) {
                //ex.printStackTrace();
            } 
            if(extended != null ) extended.put(name, val);
        }
    }
    
    
    public static void updateAttributes( Object o , Map extended, Map newValues, PropertyResolver resolver ) {
        
         if(resolver==null)
            throw new IllegalStateException("Property resolver must be specified in ParserUtil.updateAttributes");
         
        Iterator iter = newValues.entrySet().iterator();
        String name = null;
        String value = null;
        while(iter.hasNext()) {
            Map.Entry me = (Map.Entry)iter.next();
            name = me.getKey()+"";
            value = me.getValue()+"";
            Object val = value;
            try {
                Class clz = resolver.getPropertyType(o, name);
                if( clz.equals(Integer.class) ) {
                    val = Integer.valueOf(value);
                } else if( clz.equals(Double.class) ) {
                    val = Double.valueOf(value) ;
                } else if( clz.equals(Boolean.class) ) {
                    val = Boolean.valueOf(value);
                }
                /*
                else if( clz == Integer.TYPE ) {
                    val = Integer.parseInt(value+"") ;
                }
                else if( clz == Double.TYPE ) {
                    val = Double.parseDouble(value+"") ;
                }
                else if( clz == Boolean.TYPE ) {
                    val = Boolean.parseBoolean(value+"") ;
                }
                 */
                resolver.setProperty(o,name,val);
            } 
            catch (Exception ex) {
                //System.out.println("property NAME=" + name + " VALUE=" + value + " " + ex.getMessage() );
                //ex.printStackTrace();
            } 
            if(extended != null ) extended.put(name, val);            
        }
    }
    
    //This utility parses a string to map properties
    //example visible:hidden; editable:true
    public static Map stringToMap( String props ) {
        HashMap map = new HashMap();
        if( props != null ) {
            String[] arr = props.split(";");
            for( int i = 0; i < arr.length; i++ ) {
                String[] kv = arr[i].split(":");
                map.put( kv[0].trim(), kv[1].trim() );
            }
        }
        return map;  
    }
    
    
    
    
}
