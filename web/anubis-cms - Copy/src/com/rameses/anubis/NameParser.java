/*
 * PageMapping.java
 *
 * Created on June 14, 2012, 8:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Elmo
 * the basic match is as follows:
 *
 * if url mapping is /blogs/[year]/[month]
 * it should match exactly /blogs/2001/01 and not /blogs/2001/01/01
 * 2001 will be placed in year while 01 will be month
 */
public class NameParser {
    
    private Pattern parser = Pattern.compile("\\[.*?\\]");
    private MessageFormat formatter;
    private String pattern;
    private List tokens;
    private String path;
    
    public NameParser(String mapping, String path) {
        init(mapping);
        this.path = path;
    }
    
    private String formatStr(String text) {
        return "^"+text.replace(".", "\\.")+"$";
    }
    
    private void init(String text) {
        tokens = new ArrayList();
        text = formatStr(text);
        Matcher m = parser.matcher(text);
        StringBuilder sb = new StringBuilder();
        StringBuilder sf = new StringBuilder();
        int start = 0;
        int counter = 0;
        while(m.find()) {
            sb.append( text.substring(start, m.start()) + "([\\w|-]+)?" );
            sf.append( text.substring(start, m.start()) + "{"+  (counter++) + "}" );
            String stext = text.substring(m.start(),m.end()).trim();
            tokens.add( stext.substring(1, stext.length()-1)  );
            start = m.end();
        }
        if( start < text.length()  ) {
            sb.append( text.substring(start));
            sf.append( text.substring(start) );
        }
        pattern = sb.toString();
        formatter = new MessageFormat(sf.toString());
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public List getTokens() {
        return tokens;
    }
    
    /*
     * This routine will check if the path is matched.
     * A successful match will return the tokens, if not it returns null;
     */
    public boolean matches(String path ) {
        return path.matches(pattern);
    }
    
    private Map getTokens(String path) {
        Map m = new LinkedHashMap();
        Object[] objs = formatter.parse( formatStr(path), new ParsePosition(0));
        for( int i=0; i<tokens.size(); i++) {
            m.put( tokens.get(i), objs[i] );
        }
        return m;
    }
    
    private String getPath() {
        return path;
    }
    
    private String getResolvedTargetPath(Map tokens) {
        String s = getPath();
        for(Object o : tokens.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            s = s.replace( "["+me.getKey()+"]", me.getValue().toString() );
        }
        return s;
    }
    
    private String getResolvedTargetPath(String path) {
        return getResolvedTargetPath( getTokens(path));
    }
    
    public String toString() {
        return this.pattern + " " + this.path;
    }
    
    public static class MatchResult {
        String resolvedPath;
        Map tokens;

        public String getResolvedPath() {
            return resolvedPath;
        }

        public Map getTokens() {
            return tokens;
        }
    }

    public MatchResult buildResult(String path) {
        MatchResult mr = new MatchResult();
        mr.tokens = getTokens(path);
        mr.resolvedPath = getResolvedTargetPath(mr.tokens);
        return mr;
    }
    
}
