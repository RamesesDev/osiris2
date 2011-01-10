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
    
    public static String substituteValues(String name, Map map, PropertyResolver resolver) {
        if( name == null ) return null;
        if(name.trim().length()==0) return "";
        if(!name.contains("$") &&  !name.contains("#")) return name;
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
