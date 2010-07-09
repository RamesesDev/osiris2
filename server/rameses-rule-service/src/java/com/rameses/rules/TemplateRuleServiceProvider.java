/*
 * BasicRuleService.java
 *
 * Created on May 30, 2009, 9:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.Context;

/**
 *
 * @author elmo
 */
public class TemplateRuleServiceProvider implements RuleServiceProvider, Enumeration {
    
    private SimpleTemplateEngine templateEngine = new SimpleTemplateEngine();
    private List<RuleSource> list = new ArrayList<RuleSource>();
    private Iterator<RuleSource> iterator;
    private Context ctx;
    
    public TemplateRuleServiceProvider() {
    }
    
    public Enumeration<RuleSource> getRules(String filterName) {
        try {
            String ruleBase = filterName;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            String fName = "META-INF/rules/" + filterName + ".rule";
            URL u = loader.getResource(fName);
            if( u == null ) {
                System.out.println("RULE SOURCE FOR " + fName + " does not exist");
            } 
            else {
                Template t = templateEngine.createTemplate(u);
                Map info = new HashMap();
                HashMap map = new HashMap();
                ArrayList<String> rulePackages = new ArrayList<String>();
                map.put("context", ctx);
                map.put("info", info);
                map.put("rulePackage", rulePackages );
                
                Writable w = t.make(map);
                
                String output = w.toString();
                if( info.get("log")!=null ) {
                    System.out.println(output);
                }
                
                if( info.get("rulebase")!=null ) {
                    ruleBase = (String)info.get("rulebase");
                }
                
                //loop packages if any
                for( String s: rulePackages ) {
                    StringReader sr = new StringReader(s);
                    list.add( new RuleSource(ruleBase, sr) );
                }
                
                //only process output IF there is an output
                if( output != null && output.trim().length() > 0 ) {
                    for(String ss : output.split("@Break")) {
                        if( ss !=null && ss.trim().length()>0) {
                            list.add( new RuleSource(ruleBase, new StringReader(ss)) );
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        iterator = list.iterator();
        return this;
    }
    
    
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }
    
    public Object nextElement() {
        return iterator.next();
    }
    
    public void setContext(Context ctx) {
        this.ctx = ctx;
    }
    
    
    
}
