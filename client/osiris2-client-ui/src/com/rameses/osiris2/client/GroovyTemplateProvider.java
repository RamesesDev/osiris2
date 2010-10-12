package com.rameses.osiris2.client;
/*
 * GroovyTemplateProvider.java
 *
 * Created on July 17, 2010, 9:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import com.rameses.util.TemplateProvider;
import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class GroovyTemplateProvider extends TemplateProvider {
    
    private Map<String, Template> cache = new Hashtable();
    
    public String[] getExtensions() {
        return new String[] { "groovy" };
    }
    
    public Template getTemplate(String name) {
        try {
            if( ! cache.containsKey(name)) {
                //get the inputstream
                InputStream is = null;
                try {
                    is = getResourceStream(name);
                    SimpleTemplateEngine st = new SimpleTemplateEngine();
                    InputStreamReader rd = new InputStreamReader(is);
                    Template t = st.createTemplate(rd);
                    cache.put( name, t );
                } catch(Exception ex) {
                    throw ex;
                } finally {
                    try { is.close(); } catch(Exception ign ) {;}
                }
            }
            if( !cache.containsKey(name))
                throw new Exception("Name " + name + " not found");
            return cache.get(name);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Object getResult(String templateName, Object data) {
        Map m = null;
        if(data instanceof Map) {
            m = (Map)data;
        }
        Template template = getTemplate(templateName);
        Writable w = template.make(m);
        return w.toString();
    }
    
    public void transform(String templateName, Object data, OutputStream out) {
        ObjectOutputStream oos = null;
        try {
            Object result = getResult(templateName, data);
            oos = new ObjectOutputStream(out);
            oos.writeObject( result );
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {oos.close();}catch(Exception ign){;}
        }
    }
    
    
}