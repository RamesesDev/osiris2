/*
 * HttpInvoker.java
 *
 * Created on January 21, 2011, 8:20 AM
 *
 
 */

package com.rameses.etracs1.invoker;

import com.rameses.invoker.client.*;
import java.util.List;
import java.util.Map;

/**
 * Utility class for transiently accessing remote servers and destroying it after use.
 */
public final class HttpServiceInvoker {
    
    private HttpScriptService1 service;

    public HttpServiceInvoker(String host, String appContext, boolean secured ) {
        HttpInvokerClient client = new HttpInvokerClient();   
        if(host==null) 
            throw new RuntimeException("Host is required");
        client.setHost( host );
        client.setHosts(new String[]{host});
        if(appContext!=null) client.setAppContext( appContext );
        client.setSecured( secured );
        service = new HttpScriptService1(client);
    }

    public HttpServiceInvoker(String host, String appContext) {
        this(host,appContext,false);
    }
    
    public HttpServiceInvoker(Map m) {
        this((String) m.get("host"),(String)m.get("app.context"), true);
    }
    
    public Action create(String serviceName) {
        return new Action(serviceName, service);
    }
    
    public final static class Action {
        
        private HttpScriptService1 svc;
        private String serviceName;
        private Map env;
                
        Action(String serviceName, HttpScriptService1 c) {
            this.svc = c ;
            this.serviceName = serviceName;
        }

        public Object invoke(String action, Object[] args) throws Exception {
            return svc.invoke(serviceName,action,args);
        }
        
        public Object invoke(String action, List list ) throws Exception {
            return svc.invoke(serviceName,action, list.toArray(new Object[]{}));
        }
    }
    
    
    
    
}
