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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * this class is used by sql as well as 
 */
public final class ExprUtil {
    
    
    private static final Pattern p = Pattern.compile("(#|\\$)\\{.*?\\}");
    
    public static String substituteValues( String expr, Map xvalues ) {
        return substituteValues( expr, xvalues, null );
    }

    //private static Pattern substitute = Pattern.compile("\\$\\{.+?\\}");

    /*
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
    */
    
    public static String substituteValues(String name, Map map, PropertyResolver resolver) {
        if( name == null ) return null;
        if(name.trim().length()==0) return "";
        Matcher m = p.matcher(name);
        StringBuffer sb = new StringBuffer();
        int start = 0;
        while(m.find()) {
            String s = m.group().substring(2, m.group().length()-1);
            sb.append( name.substring(start, m.start()) );
            if(resolver==null)
                sb.append( map.get(s)+"" );
            else
                sb.append( resolver.getProperty(map, s) );
            start = m.end();
        }
        sb.append( name.substring(start));
        return sb.toString();
    }
    
    
    
}
