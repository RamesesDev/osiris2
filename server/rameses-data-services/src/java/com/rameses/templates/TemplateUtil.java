/*
 * TemplateUtil.java
 *
 * Created on June 5, 2009, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.templates;

import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import java.util.HashMap;
import java.util.Map;

public final class TemplateUtil {
    
    public static String render(String src, Map data) {
        Template t = null;
        try {
            SimpleTemplateEngine st = new SimpleTemplateEngine();
            t = st.createTemplate(src);
            if( data == null ) data = new HashMap();
            Writable w =  t.make( data );
            return w.toString();  
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}
