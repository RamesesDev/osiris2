/*
 * ServiceManager.java
 *
 * Created on June 24, 2012, 11:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class ServiceManager {
    
    private Project project;
    
    private Map<String, ServiceHandler> handlers = new  Hashtable();
    
    
    ServiceManager(Project p) {
        this.project = p;
    }

    public Object create(String name) {
        String confName = name.substring(0, name.indexOf("/"));
        String svcName = name.substring( name.indexOf("/") + 1);
        
        //find the handler info
        Map conf = project.getContentManager().getHandler("service").getJsonMap( confName, null );
        String handlerName = (String)conf.get("handler");
        if(handlerName == null ) handlerName = "script";
        
        ServiceHandler svcHandler = handlers.get(handlerName); 
        return svcHandler.create( svcName, conf );
    }
    
    public void addHandler(ServiceHandler handler) {
        handlers.put( handler.getName(), handler );
    }
    
    
    
}
