/*
 * ResourceService.java
 *
 * Created on July 15, 2010, 9:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.eserver;

import com.sun.jmx.remote.util.Service;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.naming.InitialContext;

/**
 * ResourceService only handles resources.
 * It does not involve caching data.
 */
public class ResourceService implements ResourceServiceMBean, Serializable {
    
    
    private List<ResourceProvider> providers;
    
    public void start() throws Exception {
        System.out.println("STARTING RESOURCE SERVICE");
        providers = new ArrayList();
        Iterator<ResourceProvider> iter = Service.providers(ResourceProvider.class, Thread.currentThread().getContextClassLoader());
        while(iter.hasNext()) {
            providers.add(iter.next());
        }
        Collections.sort(providers);
        
        //display plugged in resource providers for debugging purposes
        for(ResourceProvider rp: providers) {
            System.out.println("     Loading Provider ... " + rp.getDescription());
        }
        JndiUtil.bind(new InitialContext(),CONSTANTS.RESOURCE_SERVICE,this);
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING RESOURCE SERVICE");
        JndiUtil.unbind(new InitialContext(),CONSTANTS.RESOURCE_SERVICE);
        providers.clear();
        providers = null;
    }
    
    public InputStream getResource(String name) throws Exception {
        String[] arr = name.split("://");
        InputStream is = null;
        if( arr.length == 1 ) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        } else {
            String protocol = arr[0];
            String resourceName = arr[1];
            
            for(ResourceProvider rp: providers ) {
                if(rp.accept(protocol)) {
                    is = rp.getResource(resourceName);
                    if(is!=null) break;
                }
            }
            if(is==null) {
                try {
                    URL u = new URL(name);
                    is = u.openStream();
                } catch(Exception e) {;}
            }
        }
        if(is==null)
            throw new IllegalStateException("Resource " + name + " does not exist!");
        else
            return is;
    }
    
    public void scanResources(String name, MultiResourceHandler handler) throws Exception {
        if(handler==null)
            throw new IllegalStateException("MultiResourceHandler must not be null");
        String[] arr = name.split("://");
        InputStream is = null;
        if( arr.length == 1 ) {
            Enumeration<URL> en = Thread.currentThread().getContextClassLoader().getResources(name);
            while(en.hasMoreElements()) {
                URL u = en.nextElement();
                handler.handle( u.openStream() );
            }
        } 
        else {
            String protocol = arr[0];
            String resourceName = arr[1];
            boolean isHandled = false;
            for(ResourceProvider rp: providers ) {
                if(rp.accept(protocol)) {
                    isHandled = true;
                    rp.scanResources( resourceName, handler );
                }
            }
            if(!isHandled) {
                try {
                    URL u = new URL(name);
                    handler.handle(u.openStream());
                } catch(Exception e) {;}
            }
        }
    }
    
    
}
