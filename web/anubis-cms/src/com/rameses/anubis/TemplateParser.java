/*
 * TemplateParser.java
 *
 * Created on July 1, 2012, 2:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import com.rameses.io.StreamUtil;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Elmo
 */
public class TemplateParser {
    
    
    private static TemplateParser instance = new TemplateParser();
    
    public static TemplateParser getInstance() {
        return instance;
    }
    
    private Pattern pattern = Pattern.compile("@?@.*?\\((.|\n|\r)*?\\)");
    
    public String replaceText(String text) {
        Matcher m = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        int start = 0;
        while(m.find()) {
            sb.append( text.substring(start, m.start()) );
            String stext = text.substring(m.start(),m.end()).trim();
            String _pre = stext.substring(1, stext.indexOf("("));
            String _post = stext.substring(stext.indexOf("(")+1, stext.indexOf(")") );
            
            if( _post.trim().length() == 0 ) {
                _post = "[:]";
            }
            else if( _post.indexOf(":")<0) {
                _post = "[value:"+_post+"]";
            }
            else {
                _post = "[" + _post + "]";
            }
            
            //delayed processing
            if( _pre.startsWith("@")) {
                _pre = _pre.substring(1);
                sb.append(" ${ANUBIS.queue('" + _pre + "'," + _post + ")} " );
            }
            else {
                sb.append(" ${ANUBIS.getWidget('" + _pre + "'," + _post + ")} " );
            }
            start = m.end();
        }
        if( start < text.length()  ) sb.append( text.substring(start));
        return sb.toString();
    }
    
    public Template parse(InputStream is) throws Exception {
        return parse( StreamUtil.toString( is ) );
    }
    
    public Template parse(String s ) throws Exception {
        try {
            String txt  = replaceText(s);
            SimpleTemplateEngine st = new SimpleTemplateEngine();
            return  st.createTemplate( txt );
            
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
