/*
 * ScriptResource.java
 *
 * Created on July 15, 2010, 3:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.eserver.*;
import com.rameses.eserver.MultiResourceHandler;
import com.rameses.eserver.ResourceProvider;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 *
 * @author elmo
 */
public class ScriptResourceProvider extends ResourceProvider {
    
    public String getName() {
        return "script" ;
    }
    
    public String getDescription() {
        return "Default Script Resource Provider [script://]";
    }
    
    public int getPriority() {
        return 100;
    }
    
    public InputStream getResource(String name) throws Exception {
        String fileName = "META-INF/scripts/" + name;
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
    
    public void scanResources(String name, MultiResourceHandler handler) throws Exception {
        if(name.equals("interceptors")) {
            String fileName = "META-INF/interceptors.conf";
            Enumeration<URL> en = Thread.currentThread().getContextClassLoader().getResources(fileName);
            while(en.hasMoreElements()) {
                URL u = en.nextElement();
                handler.handle( u.openStream() );
            }
        }
        else if(name.equals("deployers")) {
            String fileName = "META-INF/deployers.conf";
            Enumeration<URL> en = Thread.currentThread().getContextClassLoader().getResources(fileName);
            while(en.hasMoreElements()) {
                URL u = en.nextElement();
                handler.handle( u.openStream() );
            }
        }
    }
    
    public boolean accept(String nameSpace) {
        return (nameSpace.equalsIgnoreCase("script"));
    }
    
}
