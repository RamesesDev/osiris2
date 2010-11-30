/*
 * AbstractAsyncScriptServiceProxy.java
 *
 * Created on October 24, 2010, 1:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.invoker.client;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 *
 * @author ms
 */
public abstract class AbstractScriptServiceProxy  {
    
    private Map env;
    protected HttpScriptService scriptService;
    
    protected abstract ScriptInterfaceProvider getScriptInterfaceProvider();
    public abstract void invokeLater( ResponseHandler handler );
    
    public Map getEnv() {
        return env;
    }
    
    public AbstractScriptServiceProxy(Map env) {
        this.env = env;
        HttpInvokerClient client = HttpClientManager.getInstance().getService(env);
        this.scriptService = new HttpScriptService(client);
    }
    
    public Object create(String name) {
        Class cls = getScriptInterfaceProvider().getInterface(name, scriptService);
        ClassLoader loader = getScriptInterfaceProvider().getProxyClassLoader();
        return Proxy.newProxyInstance(loader, new Class[]{cls}, new InvokerHandler(this, scriptService, name, getEnv()));
    }
    
}
