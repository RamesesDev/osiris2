/*
 * JsonUtil.java
 *
 * Created on February 9, 2011, 8:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.server.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
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
        
        parseDateToString(o);
        
        if( o instanceof Map ) {
            return JSONObject.fromObject(o).toString();
        } else if(o instanceof List) {
            return JSONArray.fromObject(o).toString();
        } else if( (o instanceof String) || (o instanceof Date)) {
            return "\"" + o.toString() + "\"";
        } else {
            return o.toString();
        }
        
    }
    
    private static final Format tsf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Format df  = new SimpleDateFormat("yyyy-MM-dd");
    
    private static void parseDateToString(Object obj) {
        if(obj instanceof List) {
            for(Object o : (List)obj) parseDateToString(o);
        }
        else if(obj instanceof Map) {
            Map m = (Map)obj;
            Object value = null;
            for(Map.Entry me : (Set<Map.Entry>)m.entrySet()) {
                value = me.getValue();
                if(value instanceof List) {
                    parseDateToString(value);
                }
                else if(value instanceof Map) {
                    parseDateToString(value);
                }
                else if(value instanceof Timestamp) {
                    m.put(me.getKey(), tsf.format(value));
                }
                else if(value instanceof Date) {
                    m.put(me.getKey(), df.format(value));
                }
            }
        }
    }
    
    public static Object toObject(String s) {
        if(s.trim().startsWith("{")) {
            return toMap(s);
        } else if(s.trim().startsWith("[")) {
            return toList( s );
        } else
            throw new RuntimeException("Expression not supported. " + s);
    }
    
    public static Object[] toObjectArray(String s) {
        if(s.trim().startsWith("{")) {
            return new Object[]{toMap(s)};
        } else if(s.trim().startsWith("[")) {
            return toList( s ).toArray(new Object[]{});
        } else
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
            } else if( o instanceof JSONArray ) {
                List ja = toList(  (JSONArray)o );
                list.add( ja );
            } else {
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
            } else if(value instanceof JSONNull) {
                map.put(field,null);
            } else if(value instanceof JSONArray ) {
                List list = toList(  (JSONArray)value );
                map.put(field,list);
            } else if( value instanceof JSONObject ) {
                JSONObject o = (JSONObject)value;
                Map bean = toMap(o);
                map.put( field, bean );
            } else if( value instanceof Double ) {
                map.put( field, new BigDecimal(value+"") );
            } else {
                map.put(field, value );
            }
            return true;
        }
    }
    
    
}
