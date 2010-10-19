/*
 * InvokerProxy.java
 *
 * Created on June 28, 2009, 6:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tester;

import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    
    private Map<String, ClientService> map = new HashMap();
    private Map env = new HashMap();
    private HttpClientManager manager = new HttpClientManager();
    
    public HttpClient() {;}
    
    public HttpClient(Map env) {
        this.env = env;
    }

    public Object create( String name ) throws Exception {
        return create(name, null);
    }
    
    public Object create( String name, String hostKey) throws Exception {
        if(!map.containsKey(name)  ) {
            HttpInvokerClient c =  manager.getService(hostKey, env);
            map.put( name,  new ClientService(name, c));
        }
        return map.get(name);    
        
    }

    public static class ClientService {
        private String name;
        private HttpInvokerClient client;
        
        public ClientService(String name, HttpInvokerClient c) {
            this.name = name;
            this.client = c;
        }
        
        public Object invoke(String method, Object[] args) throws Exception {
           return client.invoke( name+"."+method, args );
        }
    }
    
    
}
