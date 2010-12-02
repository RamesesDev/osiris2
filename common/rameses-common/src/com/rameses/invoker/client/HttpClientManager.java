package com.rameses.invoker.client;

import com.rameses.util.SysMap;
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
    
    public HttpInvokerClient getService( Map env) {
        return getService(null, env);
    }
    
    public HttpInvokerClient getService(String hostKey, Map env) {
        env = new SysMap(env);
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
        
        String nameKey = hostKey;
        String appContext = (String)env.get("app.context");
        if(appContext!=null) {
            nameKey = hostKey + "/" + appContext;
        }
        
        if( !services.containsKey(nameKey) ) {
            HttpInvokerClient client = new HttpInvokerClient();
            //find the hostkey settings in system properties
            if( hostKey !=null ) {
                String s = (String) env.get( hostKey);
                if( s != null ) {
                    if(s.startsWith("~")) {
                        client.setSecured(false);
                        s = s.substring(1);
                    }
                    String hosts[] = s.split(";");
                    client.setHost(hosts[0]);
                    client.setHosts(hosts);
                }
            }
            
            if(appContext!=null) client.setAppContext( appContext );
            
            //new addition with app context.
            services.put(nameKey, client);
        }
        
        return (HttpInvokerClient)services.get(nameKey);
    }
    
    public void clear() {
        services.clear();
    }
    
}
