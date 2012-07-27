/*
 * CmsCache.java
 *
 * Created on May 25, 2012, 3:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import com.rameses.io.StreamUtil;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Elmo
 * CMS Cache is segregated by client
 *
 */
public final class TemplateParser  {
    
    private static TemplateParser instance = new TemplateParser();
    
    public static TemplateParser getInstance() {
        return instance;
    }
    
    private Pattern pattern = Pattern.compile("@.*?\\((.|\n|\r)*?\\)");
    
    public String replaceText(String text) {
        Matcher m = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        int start = 0;
        while(m.find()) {
            sb.append( text.substring(start, m.start()) );
            String stext = text.substring(m.start(),m.end()).trim();
            String _pre = stext.substring(1, stext.indexOf("("));
            String _post = stext.substring(stext.indexOf("(")+1, stext.indexOf(")") );
            if( _post.trim().length() == 0 ) _post = ":";
            sb.append(" ${WIDGET.load('" + _pre + "',[" + _post + "])} " );
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
