/*
 * ServiceManager.java
 *
 * Created on July 1, 2012, 9:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class ServiceManager {
    
    protected Project project;
    protected Module module;
    
    private Map<String, ServiceAdapter> handlers = new  Hashtable();
    private Map<String, Map> cache = new Hashtable();
    private Map<String, Map> infoCache = new Hashtable();
    
    //this is overridable
    public abstract ServiceAdapter[] getServiceAdapters();
    public abstract Map getServiceAdapterInfo(String name);
    
    protected ServiceManager(Project p, Module module) {
        this.project = p;
        this.module = module;
        for(ServiceAdapter h: getServiceAdapters()) {
            addHandler(h);
        }
    }
    
    
    public void addHandler(ServiceAdapter handler) {
        handlers.put( handler.getName(), handler );
    }
    
    private Map findAdapterInfo(String confName ) {
        Map conf = null;
        if( !cache.containsKey(confName)) {
            Map m = getServiceAdapterInfo(confName);
            cache.put(confName, m);
        }
        return cache.get(confName);
    }
    
    private ServiceAdapter findServiceAdapter(Map conf) {
        //find the handler info
        String handlerName = (String)conf.get("handler");
        if(handlerName == null ) handlerName = "script";
        return handlers.get(handlerName);
    }
    
    public Object create(String name) {
        String confName = name.substring(0, name.indexOf("/"));
        String svcName = name.substring( name.indexOf("/") + 1);
        Map conf = findAdapterInfo(confName);
        
        ServiceAdapter svcHandler = findServiceAdapter(conf);
        return svcHandler.create( svcName, conf );
    }
    
    public Map getClassInfo(String name) {
        if(infoCache.containsKey(name) ) return infoCache.get(name);
        
        String confName = name.substring(0, name.indexOf("/"));
        String svcName = name.substring( name.indexOf("/") + 1);
        Map conf = findAdapterInfo(confName);
        ServiceAdapter svcHandler = findServiceAdapter(conf);
        Map info = svcHandler.getClassInfo( svcName, conf );
        infoCache.put(name, info);
        return info;
    }
}
