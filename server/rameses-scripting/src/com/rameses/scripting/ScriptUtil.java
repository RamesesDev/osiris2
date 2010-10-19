/*
 * ScriptUtil.java
 *
 * Created on October 17, 2010, 1:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ms
 */
public final class ScriptUtil {
    
    private static final Pattern p = Pattern.compile("#\\{.*?\\}");
    
    public static String correctValue(String name, Map map) {
        if( name == null ) return null;
        if(name.trim().length()==0) return "";
        Matcher m = p.matcher(name);
        StringBuffer sb = new StringBuffer();
        int start = 0;
        while(m.find()) {
            String s = m.group().substring(2, m.group().length()-1);
            sb.append( name.substring(start, m.start()) );
            sb.append( map.get(s)+"" );
            start = m.end();
        }
        sb.append( name.substring(start));
        return sb.toString();
    }
    
    
}
