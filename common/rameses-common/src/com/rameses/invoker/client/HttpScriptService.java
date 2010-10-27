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
    
    public byte[] getScriptInfo(String name) {
        try {
            return (byte[])client.invoke( serviceName + ".getScriptInfo", new Object[]{name}  );
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Object invoke(String name, String method, Object[] params, Map env) {
        try {
            return client.invoke( serviceName + ".invoke", new Object[]{ name,method,params,env} );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Object invokeAsync(String name, String method, Object[] params, Map env, Map asyncInfo ) {
        try {
            return client.invoke( serviceName + ".invokeAsync", new Object[]{ name,method,params,env,asyncInfo} );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void pushResponse(String requestId, Object data) {
        try {
            client.invoke( serviceName + ".pushResponse", new Object[]{ requestId, data } );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getPollData(String requestId) {
        try {
            return client.invoke( serviceName + ".getPollData", new Object[]{ requestId } );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
}
