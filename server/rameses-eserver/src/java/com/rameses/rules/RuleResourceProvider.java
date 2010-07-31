/*
 * RuleResource.java
 *
 * Created on July 27, 2010, 4:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import com.rameses.eserver.MultiResourceHandler;
import com.rameses.eserver.ResourceProvider;
import com.rameses.util.URLDirectory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 *
 * @author elmo
 */
public class RuleResourceProvider extends ResourceProvider {
    
    /** Creates a new instance of RuleResource */
    public RuleResourceProvider() {
    }
    
    public String getName() {
        return "rules";
    }
    
    public String getDescription() {
        return "Rule Resource Provider [rules://]";
    }
    
    public int getPriority() {
        return 0;
    }
    
    public boolean accept(String nameSpace) {
        return (nameSpace.equals("rules"));
    }
    
    public InputStream getResource(String name) throws Exception {
        String fileName = "META-INF/rules/" + name;
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
    
    public void scanResources(String name, MultiResourceHandler handler) throws Exception {
        if(name.equals("rulebases")) {
            String fileName = "META-INF/rules";
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> en = loader.getResources(fileName);
            RuleBaseURLFilter rf = new RuleBaseURLFilter();
            while(en.hasMoreElements()) {
                URL u = en.nextElement();
                URLDirectory d = new URLDirectory( u );
                d.list(rf, loader);
            }
            String s = rf.toString();
            handler.handle( new ByteArrayInputStream(s.getBytes()) );
        }
    }
    
    private class RuleBaseURLFilter implements URLDirectory.URLFilter {
        private StringBuffer sb = new StringBuffer();
        
        public boolean accept(URL u, String filter) {
            if(filter.endsWith("/")) {
                sb.append( u.getPath() +"\n" );
            }
            return false;
        }
        
        public String toString() {
            return sb.toString();
        }
    }
    
    
}
