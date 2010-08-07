/*
 * InvokerProxy.java
 *
 * Created on June 28, 2009, 6:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.invoker.client.HttpInvokerClient;
import com.rameses.invoker.client.HttpClientManager;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.Map;

public class InvokerProxy {

    private final static String INVOKER = "ScriptService";
    private Map<String, Class> map = new Hashtable<String, Class>();
    private GroovyClassLoader loader = new GroovyClassLoader(ClientContext.getCurrentContext().getClassLoader());
    
    private static InvokerProxy instance;
    
    public static InvokerProxy getInstance() {
        if( instance == null ) {
            instance = new InvokerProxy();
        }
        return instance;
    }
    
    
    public Object create( String name ) throws Exception {
        return create(name, null);
    }
    
    public Object create( String name, String hostKey) throws Exception {
        HttpInvokerClient client = HttpClientManager.getInstance().getService(hostKey, OsirisContext.getSession().getEnv());
        Class clazz = null;
        if(! map.containsKey(name)) {
            byte[] b = (byte[])client.invoke(INVOKER+".getScriptInfo", new Object[]{name});
            InputStream is = new ByteArrayInputStream(b);
            clazz = loader.parseClass(is);
            map.put(name, clazz);
        }
        clazz = map.get(name);
        return Proxy.newProxyInstance(loader, new Class[]{clazz}, new MyHandler(name, client));
    }
    
    
    public class MyHandler implements InvocationHandler {
        
        private HttpInvokerClient client;
        private String serviceName;
        
        public MyHandler(String serviceName, HttpInvokerClient client) {
            this.serviceName = serviceName;
            this.client = client;
        }
        
        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
            if( method.getName().equals("toString")) return serviceName;
            
            Map headers = ClientContext.getCurrentContext().getHeaders();            
            return client.invoke( INVOKER+".invoke", new Object[]{ serviceName, method.getName(), args, headers } );
        }
    } 
 
    public void reset() {
        map.clear();
        //loader.clearCache();
        loader = new GroovyClassLoader(ClientContext.getCurrentContext().getClassLoader());
        instance = null;
    }
    
}
