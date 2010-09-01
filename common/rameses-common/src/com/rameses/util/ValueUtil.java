/*
 * ValueUtil.java
 *
 * Created on June 21, 2010, 3:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author compaq
 */
public class ValueUtil {
    
    private static final Pattern ESC_STRING = Pattern.compile("[\\\\$\"\n\t\r]");
    private static final SimpleDateFormat DT_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TS_FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    
    public static final boolean isEqual(Object obj1, Object obj2) {
        if ( obj1 == null && obj2 == null)
            return true;
        if ( obj1 == null && obj2 != null )
            return false;
        if ( obj1 != null && obj2 == null )
            return false;
        
        return obj1.equals(obj2);
    }
    
    public static final boolean isEmpty(Object obj) {
        if ( obj == null)
            return true;
        if ( obj instanceof String && ((String) obj).trim().length() == 0)
            return true;
        
        return false;
    }
    
    public static String getValueAsString(Class type, Object value) {
        if (value == null) {
            //do nothing
        } 
        else if (value instanceof String) {
            return escape(value.toString()).insert(0, "\"").append("\"").toString();
        } 
        else if(type == Double.class || type == double.class ||
                type == long.class || type == Long.class ||
                type == int.class || type == Integer.class ||
                type == float.class || type == Float.class ||
                type == BigDecimal.class || type == Boolean.class || 
                type == boolean.class ) 
        {
            return value+"";
        } 
        else if (value instanceof Timestamp) {
            return "\"" + TS_FORMATTER.format(value) + "\"";
        } 
        else if (value instanceof Date) {
            return "\"" + DT_FORMATTER.format(value) + "\"";
        }
        
        return "null";
    }
    
    public static StringBuffer escape(String str) {
        StringBuffer sb = new StringBuffer();
        Matcher m = ESC_STRING.matcher(str);
        while( m.find() ) {
            char match = m.group(0).charAt(0);
            String rep = null;
            switch(match) {
                case '\n': rep = "\\\\n"; break;
                case '\r': rep = "\\\\r"; break;
                case '\t': rep = "\\\\t"; break;
                case '\\': rep = "\\\\"; break;
                case '"' : rep = "\\\\\""; break;
                case '$' : rep = "\\\\\\$"; break;
            }
            
            m.appendReplacement(sb, rep);
        }
        m.appendTail(sb);
        
        return sb;
    }

    public static String repeat(String val, int repeat ) {
        if(val==null) val = " ";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<repeat;i++) {
            sb.append( val );
        }
        return sb.toString();
    }
    
    
}
