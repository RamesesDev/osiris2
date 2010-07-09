package com.rameses.templates;

import com.rameses.jndi.JndiUtil;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import java.io.Serializable;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TemplateMgmt implements TemplateMgmtMBean, Serializable {
    
    private Map<String,Template> templates = new Hashtable<String,Template>();
    
    public void start() {
        System.out.println("START TEMPLATE SERVICE");
        try {
            JndiUtil.bind(new InitialContext(), TemplateMgmt.class.getSimpleName(), this);
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        System.out.println("STOPPING TEMPLATE SERVICE");
        try {
            JndiUtil.unbind(new InitialContext(), TemplateMgmt.class.getSimpleName());
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    public void flushAll() {
        templates.clear();
    }

    public void flush(String name) {
        templates.remove(name);
    }

    public Object getTemplate(String name) {
        Template t = null;
        if(! templates.containsKey(name)) {
            try {
                SimpleTemplateEngine st = new SimpleTemplateEngine();
                URL u  = Thread.currentThread().getContextClassLoader().getResource("META-INF/templates/" + name );
                if(u==null)
                    throw new Exception("Template " + name + " not found ");
                t = st.createTemplate(u);
                templates.put( name, t );
            }
            catch(Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
        return templates.get(name);
    }
    
}
