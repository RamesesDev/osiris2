/*
 * GroovyTemplateProvider.java
 *
 * Created on July 17, 2010, 9:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.templates;

import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class GroovyTemplateProvider implements TemplateProvider {
    
    /** Creates a new instance of GroovyTemplateProvider */
    public GroovyTemplateProvider() {
    }

    public boolean accept(String fileName) {
        if( fileName.endsWith(".groovy")) {
            return true;
        }
        return false;
    }

    public Template createTemplate(InputStream is) throws Exception {
        SimpleTemplateEngine st = new SimpleTemplateEngine();
        InputStreamReader rd = new InputStreamReader(is);
        return new GroovyTemplate(st.createTemplate(rd));
    }

    public String toString() {
        return "Groovy Template Provider [.groovy extension]";
    }
    
    
    public class GroovyTemplate implements Template {
        private groovy.text.Template template;
        
        GroovyTemplate(groovy.text.Template t) {
            template = t;
        }
        
        public Object transform(Object data, OutputStream out ) {
            Map m = null;
            if(data instanceof Map) {
                m = (Map)data;
            }  
            Writable w = template.make(m);
            return w.toString();
        }
        
    }
    
    
}
