/*
 * InvokerProxy.java
 *
 * Created on November 3, 2009, 1:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;


import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class RemoteHttpInvokerProxy {
    
    private final static String INVOKER = "ScriptService";
    private Map<String, Class> map = new Hashtable<String, Class>();
    private GroovyClassLoader loader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
    
    private static RemoteHttpInvokerProxy instance;
    
    public static RemoteHttpInvokerProxy getInstance() {
        if( instance == null ) {
            instance = new RemoteHttpInvokerProxy();
        }
        return instance;
    }
    
    
    public Object create( String name ) throws Exception {
        return create(name, null, false, null );
    }
    
    public Object create( String name, String hostKey  ) throws Exception {
        return create(name, hostKey, false, null );
    }
    
    public Object create( String name, String hostKey, boolean async, String confKey ) throws Exception {
        HttpInvokerClient client = HttpClientManager.getInstance().getService(hostKey, null);
        Class clazz = null;
        if(! map.containsKey(name)) {
            byte[] b = (byte[])client.invoke(INVOKER+".getScriptInfo", new Object[]{name});
            InputStream is = new ByteArrayInputStream(b);
            clazz = loader.parseClass(is);
            map.put(name, clazz);
        }
        clazz = map.get(name);
        return Proxy.newProxyInstance(loader, new Class[]{clazz}, new MyHandler(name, client, async, confKey));
    }
    
    
    public class MyHandler implements InvocationHandler {
        
        private HttpInvokerClient client;
        private String serviceName;
        private boolean async;
        private String asyncConf;
        
        public MyHandler(String serviceName, HttpInvokerClient client, boolean async, String asyncConf) {
            this.serviceName = serviceName;
            this.client = client;
            this.async = async;
            this.asyncConf = asyncConf;
        }
        
        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
            if( method.getName().equals("toString")) return serviceName;
            if( !async ) {
                return client.invoke( INVOKER+".invoke", new Object[]{ serviceName, method.getName(), args } );
            }
            else {
                return client.invoke( INVOKER+".invokeAsync", new Object[]{ serviceName, method.getName(), args, asyncConf } );
            }
        }
    } 
 
    public void reset() {
        map.clear();
        //loader.clearCache();
        loader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        instance = null;
    }
    
}
