/*
 * ScriptDelegate.java
 *
 * Created on October 17, 2010, 11:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.async;

import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ms
 */
public class ScriptDelegate {
    
    public static ScriptDelegate instance;
    
    public static ScriptDelegate getInstance() {
        if(instance == null ) {
            instance = new ScriptDelegate();
        }
        return instance;
    }
    
    private HttpInvokerClient getClient(Map env) throws Exception {
        Map map = new HashMap();
        map.put("_host_key", "localhost:8080");
        map.putAll( env );
        return HttpClientManager.getInstance().getService("_host_key", map );
    }
    
    
    public Object invoke(String name, String method, Object[] params, Map env) {
        try {
            return getClient(env).invoke( "ScriptService.invoke", new Object[]{name, method, params, env });
        } catch(Exception e ) {
            throw new RuntimeException(e);
        }
    }
    
    public void pushResponse( String requestId, Object data, Map env ) {
        try {
            getClient(env).invoke( "ScriptService.pushResponse", new Object[]{ requestId, data });
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
