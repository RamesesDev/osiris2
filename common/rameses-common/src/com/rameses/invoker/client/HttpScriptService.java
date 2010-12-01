/*
 * ScriptDelegate.java
 *
 * Created on October 24, 2010, 1:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.invoker.client;

import java.util.Map;

/**
 *
 * @author ms
 */
public class HttpScriptService {
    
    
    private String serviceName = "ScriptService";
    private HttpInvokerClient client;
    
    public HttpScriptService(HttpInvokerClient c) {
        this.client = c;
    }
    
    public byte[] getScriptInfo(String name) throws Exception {
        return (byte[])client.invoke( serviceName + ".getScriptInfo", new Object[]{name}  );
    }
    
    public Object invoke(String name, String method, Object[] params, Map env) throws Exception {
        return client.invoke( serviceName + ".invoke", new Object[]{ name,method,params,env} );
    }
    
    public Object invokeAsync(String name, String method, Object[] params, Map env, Map asyncInfo ) throws Exception {
        return client.invoke( serviceName + ".invokeAsync", new Object[]{ name,method,params,env,asyncInfo} );
    }
    
    public void pushResponse(String requestId, Object data) throws Exception {
        client.invoke( serviceName + ".pushResponse", new Object[]{ requestId, data } );
    }
    
    public Object getPollData(String requestId) throws Exception {
        return client.invoke( serviceName + ".getPollData", new Object[]{ requestId } );
    }
    
    
}
