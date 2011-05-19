/*
 * JsonUtil.java
 *
 * Created on February 9, 2011, 8:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.support;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONStringer;
import net.sf.json.util.PropertyFilter;

/**
 *
 * @author ms
 */
public final class JsonUtil {
    
    public static String toString(Object o) {
        if( o== null ) {
            return "null";
        }
        if( o instanceof Map ) {
            return JSONObject.fromObject(o).toString();
        }
        else if(o instanceof List) {
            return JSONArray.fromObject(o).toString();
        }
        else if( (o instanceof String) || (o instanceof Date)) {
            return "\"" + o.toString() + "\""; 
        }
        else {
            return o.toString(); 
        }
    }
    
    public static Object toObject(String s) {
        if(s.trim().startsWith("{")) {
            return toMap(s);
        }
        else if(s.trim().startsWith("[")) {
            return toList( s );
        }
        else
            throw new RuntimeException("Expression not supported. " + s);
    }
    
    public static Object[] toObjectArray(String s) {
        if(s.trim().startsWith("{")) {
            return new Object[]{toMap(s)};
        }
        else if(s.trim().startsWith("[")) {
            return toList( s ).toArray(new Object[]{});
        }
        else
            throw new RuntimeException("Expression not supported. " + s);
    }
    
    public static Map toMap(String s) {
        JSONObject jo = JSONObject.fromObject( s );
        return toMap( jo );
    }
    
    public static Map toMap(JSONObject jo) {
        JsonConfig conf = new JsonConfig();
        conf.setJavaPropertyFilter(new JsonFilter());
        return (Map)JSONObject.toBean(jo,new HashMap(),conf);
    }
    
    public static List toList( String s ) {
        JSONArray arr = JSONArray.fromObject(s);
        return toList( arr );
    }
    
    public static List toList( JSONArray arr) {
        Iterator iter = arr.iterator();
        List list = new ArrayList();
        while(iter.hasNext()) {
            Object o = iter.next();
            if( o instanceof JSONObject ) {
                JSONObject jo = (JSONObject)o;    
                list.add( toMap(jo) );
            }
            else if( o instanceof JSONArray ) {
                List ja = toList(  (JSONArray)o );
                list.add( ja );
            }
            else {
                list.add(o);
            }
        }
        return list;
    }
    
     
    public static class JsonFilter implements  PropertyFilter {
        public boolean apply(Object object, String field, Object value) {
            Map map = (Map)object;  //the holder
            if( value == null ) {
                map.put(field,null);
            } 
            else if(value instanceof JSONNull) {
                map.put(field,null);
            }
            else if(value instanceof JSONArray ) {
                List list = toList(  (JSONArray)value );
                map.put(field,list);
            } else if( value instanceof JSONObject ) {
                JSONObject o = (JSONObject)value;
                Map bean = toMap(o);
                map.put( field, bean );
            }
            else if( value instanceof Double ) {
                map.put( field, new BigDecimal(value+"") );
            }
            else {
                map.put(field, value );
            }
            return true;
        }
    }
    
}
