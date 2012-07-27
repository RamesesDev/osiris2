/*
 * PageMapping.java
 *
 * Created on June 14, 2012, 8:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

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
 */
public class UrlMapping {
    
    private Pattern parser = Pattern.compile("\\[.*?\\]");
    private MessageFormat formatter;
    private String pattern;
    private List tokens;
    
    private String pageName;
    
    public UrlMapping(String mapping, String pageName) {
        init(mapping);
        this.pageName = pageName;
    }
    
    private void init(String text) {
        tokens = new ArrayList();
        Matcher m = parser.matcher(text);
        StringBuilder sb = new StringBuilder();
        StringBuilder sf = new StringBuilder();
        int start = 0;
        int counter = 0;
        while(m.find()) {
            sb.append( text.substring(start, m.start()) + "[\\w_-]*" );
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
    
    public Map getTokens(String path) {
        Map m = new LinkedHashMap();
        Object[] objs = formatter.parse(path, new ParsePosition(0));
        for( int i=0; i<tokens.size(); i++) {
            m.put( tokens.get(i), objs[i] );
        }
        return m;
    }

    public String getPageName() {
        return pageName;
    }
    
    
}
