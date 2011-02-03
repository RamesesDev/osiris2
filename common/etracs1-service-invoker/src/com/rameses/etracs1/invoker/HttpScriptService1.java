/*
 * ScriptDelegate.java
 *
 * Created on October 24, 2010, 1:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.etracs1.invoker;

import com.rameses.invoker.client.*;

/**
 * This is the old script service that can support current version of etracs.
 * This should be deleted once we have completed the migration 
 */
public class HttpScriptService1 {
    
    private String serviceName = "ScriptService";
    private HttpInvokerClient client;
    
    public HttpScriptService1(HttpInvokerClient c) {
        this.client = c;
    }
    
    public byte[] getScriptInfo(String name) throws Exception {
        return (byte[])client.invoke( serviceName + ".getScriptInfo", new Object[]{name}  );
    }
    
    public Object invoke(String name, String method, Object[] params) throws Exception {
        return client.invoke( serviceName + ".invoke", new Object[]{ name,method,params} );
    }
    
    
}
