/*
 * SqlUtil.java
 *
 * Created on February 5, 2009, 5:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

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
    
    /***
     * This method parses a statement and stores parameters in the
     * paramNames list. 
     * <br>
     * For example:
     * "select * from table where fieldname = $P{fieldname}"
     * will return "select * from table where fieldname=?"
     *
     * @param sql original select statement
     * @param paramNames empty list to contain the param names extracted. 
     * must not be null. 
     *
     * @return the corrected sql statement 
     */
    public static String parseStatement( String sql, List paramNames ) {
        Matcher m = pattern.matcher(sql);
        int start = 0;
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            int end = m.start();
            sb.append( sql.substring(start, end ) + "?" );
            String name = m.group().replaceAll( "\\$P\\{|\\}", "").trim();
            paramNames.add( name );
            start = end + m.group().length();
        }
        sb.append( sql.substring(start) );
        return sb.toString();
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
    
    
}
