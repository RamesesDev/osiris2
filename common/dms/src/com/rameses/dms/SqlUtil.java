/*
 * SqlUtil.java
 *
 * Created on February 5, 2009, 5:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rameses
 */
public final class SqlUtil {
    
    /** Creates a new instance of SqlUtil */
    public SqlUtil() {
    }
    
    private static Pattern pattern = Pattern.compile("\\$P\\{.+?\\}"); 
    
    public static void parseStatement( String sql, StringBuffer sb, Map values, List params ) {
        Matcher m = pattern.matcher(sql);
        int start = 0;
        while(m.find()) {
            int end = m.start();
            sb.append( sql.substring(start, end ) + "?" );
            String name = m.group().replaceAll( "\\$P\\{|\\}", "").trim();
            params.add( values.get(name) );
            start = end + m.group().length();
        }
        sb.append( sql.substring(start) );
    }
    
    public static String substituteValues( String sql, Map values ) {
        String ss = sql;
        if( values != null ) {
            for( Object oo: values.entrySet()) {
                Map.Entry e = (Map.Entry)oo;
                String key = "\\$\\{" + (e.getKey()+"").trim() + "\\}";
                String value = e.getValue()+"";
                ss = ss.replaceAll( key, value );
            }
        }
        return  ss;
    }
    
    public static void parseStatement( String sql, StringBuffer sb, List params ) {
        Matcher m = pattern.matcher(sql);
        int start = 0;
        while(m.find()) {
            int end = m.start();
            sb.append( sql.substring(start, end ) + "?" );
            String name = m.group().replaceAll( "\\$P\\{|\\}", "").trim();
            params.add( name );
            start = end + m.group().length();
        }
        sb.append( sql.substring(start) );
    }
    
}
