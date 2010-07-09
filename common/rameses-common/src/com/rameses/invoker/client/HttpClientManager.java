package com.rameses.invoker.client;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * provides HttpInvokerClient.
 * To use, call the getService method. 
 * the host key should contain the hosts for example:
 *    app.host = ~192.168.3.101;192.168.2.111
 *
 */
public final class HttpClientManager {
    
    private Map services = new Hashtable();
    
    public HttpClientManager() {
    }
    
    private static HttpClientManager instance;
    
    public static HttpClientManager getInstance() {
        if(instance==null) {
            instance = new HttpClientManager();
        }
        return instance;
    }
    
    
    public HttpInvokerClient getService(String hostKey, Map env) {
        if( env == null ) env = new SysMap();
        
        
        //determine the host key
        if( hostKey == null || hostKey.trim().length() == 0 ) {
            if( env.get("default.host") != null ) {
                hostKey = "default.host";
            } else if( env.get("app.host") !=null ) {
                hostKey = "app.host";
            } else {
                hostKey = "default";
            }
        }
        
        if( !services.containsKey(hostKey) ) {
            HttpInvokerClient client = new HttpInvokerClient();
            
            //find the hostkey settings in system properties
            if( hostKey !=null ) {
                String s = (String) env.get( hostKey);
                if( s != null ) {
                    if(s.startsWith("~")) {
                        client.setSecured(true);
                        s = s.substring(1);
                    }
                    String hosts[] = s.split(";");
                    client.setHost(hosts[0]);
                    client.setHosts(hosts);
                }
            }
            services.put(hostKey, client);
        }
        return (HttpInvokerClient)services.get(hostKey);
    }
    
    private static class SysMap extends HashMap {
        public Object get(Object o) {
            return System.getProperty(o+"");
        }
        
    }
    
    
}
