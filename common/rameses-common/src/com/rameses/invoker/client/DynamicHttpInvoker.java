/*
 * HttpInvoker.java
 *
 * Created on January 21, 2011, 8:20 AM
 *
 
 */

package com.rameses.invoker.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Utility class for transiently accessing remote servers and destroying it after use.
 */
public final class DynamicHttpInvoker {
    
    private HttpScriptService service;

    public DynamicHttpInvoker(String host, String appContext, boolean secured ) {
        HttpInvokerClient client = new HttpInvokerClient();   
        if(host==null) 
            throw new RuntimeException("Host is required");
        client.setHost( host );
        client.setHosts(new String[]{host});
        if(appContext!=null) client.setAppContext( appContext );
        client.setSecured( secured );
        service = new HttpScriptService(client);
    }

    public DynamicHttpInvoker(String host, String appContext) {
        this(host,appContext,true);
    }
    
    public DynamicHttpInvoker(Map m) {
        this((String) m.get("host"),(String)m.get("app.context"), true);
    }
    
    public Action create(String serviceName) {
        return new Action(serviceName, service);
    }
    
    public Action create(String serviceName, Map env) {
        return new Action(serviceName, service, env);
    }

    public final static class Action {
        
        private HttpScriptService svc;
        private String serviceName;
        private Map env;
                
        Action(String serviceName, HttpScriptService c, Map env) {
            this.svc = c ;
            this.serviceName = serviceName;
            this.env = env;
        }

        Action(String serviceName, HttpScriptService c) {
            this(serviceName,c,new HashMap());
        }

        public Object invoke(String action) throws Exception {
            return svc.invoke(serviceName,action,new Object[]{},env);
        }
        
        public Object invoke(String action, Object[] args) throws Exception {
            return svc.invoke(serviceName,action,args,env);
        }
        
        public Object invoke(String action, List list ) throws Exception {
            return svc.invoke(serviceName,action, list.toArray(new Object[]{}) ,env);
        }
    }
    
    
    
    
}
