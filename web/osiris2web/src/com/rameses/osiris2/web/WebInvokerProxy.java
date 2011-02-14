/*
 * WebInvokerProxy.java
 *
 * Created on June 28, 2009, 6:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.web;

import com.rameses.invoker.client.AbstractScriptServiceProxy;
import com.rameses.invoker.client.ResponseHandler;
import com.rameses.invoker.client.ScriptInterfaceProvider;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.util.Map;

public class WebInvokerProxy extends AbstractScriptServiceProxy {
    
    private GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
    private ScriptInterfaceProvider scriptProvider = new GroovyScriptInterfaceProvider();

    
    public WebInvokerProxy() {
        super(WebContext.getInstance().getSessionContext().getEnv());
    }
    
    public Map getEnv() {
        return WebContext.getInstance().getEnv();
    }
    
    public synchronized void reset() {
        classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
    }
    
    protected ScriptInterfaceProvider getScriptInterfaceProvider() {
        return scriptProvider;
    }
    
    public void invokeLater(ResponseHandler handler) {
        
    }
    
    private class GroovyScriptInterfaceProvider extends ScriptInterfaceProvider {
        
        protected Class parseClass(byte[] bytes) {
            return classLoader.parseClass( new ByteArrayInputStream(bytes));
        }
        
        public ClassLoader getProxyClassLoader() {
            return classLoader;
        }
    }
    
}
