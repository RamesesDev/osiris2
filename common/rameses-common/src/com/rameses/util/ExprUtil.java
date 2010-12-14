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
    
    
    private static final Pattern p = Pattern.compile("(?:#|\\$)\\{(.*?)\\}");
    
    public static String substituteValues( String expr, Map xvalues ) {
        return substituteValues( expr, xvalues, null );
    }
    
    public static String substituteValues(String name, Map map, PropertyResolver resolver) {
        if( name == null || name.trim().length()==0 ) return null;
        if( !name.contains("$") &&  !name.contains("#") ) return name;
        
        Matcher m = p.matcher(name);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String s = m.group(1);
            String rep = (resolver==null)? map.get(s)+"" : resolver.getProperty(map, s)+"";
            m.appendReplacement(sb, rep);
        }
        m.appendTail(sb);
        
        return sb.toString();
    }
    
    
    
}
