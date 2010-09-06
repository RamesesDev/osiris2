/*
 * ExprUtil.java
 *
 * Created on September 2, 2010, 12:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import com.rameses.common.PropertyResolver;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * this class is used by sql as well as 
 */
public final class ExprUtil {
    
    
    private static Pattern substitute = Pattern.compile("\\$\\{.+?\\}");
    
    public static String substituteValues( String expr, Map xvalues ) {
        return substituteValues( expr, xvalues, null );
    }
    
    public static String substituteValues( String expr, Map xvalues, PropertyResolver resolver ) {
        if(! expr.contains("{") ) return expr;

        Matcher m = substitute.matcher(expr);
        int start = 0;
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            int end = m.start();
            String name = m.group().replaceAll( "\\$\\{|\\}", "").trim();
            Object val = null;
            if( resolver == null )
                val = xvalues.get(name);
            else {
                val = resolver.getProperty(xvalues, name);
            }

            //search system properties 
            if(val==null) val = System.getProperty(name);
            
            if( val == null ) val = "${"+name+"}";
            sb.append( expr.substring(start, end ) + val );
            start = end + m.group().length();
        }
        sb.append( expr.substring(start) );
        return sb.toString();
    }
    
    
    private static class SystemMap extends HashMap {
        private Map map;
        
        public SystemMap(Map map) {
            this.map = map;
        }

        public Object get(Object key) {
            if(map.containsKey(key)) {
                return map.get(key);
            }
            else {
                return System.getProperty(key+"");
            }
        }
    }
    
}
