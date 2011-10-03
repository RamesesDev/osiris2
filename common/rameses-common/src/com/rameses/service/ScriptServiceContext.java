/*
 * ScriptServiceContext.java
 * Created on September 21, 2011, 9:34 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service;

import com.sun.jmx.remote.util.Service;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class ScriptServiceContext extends ServiceContext {
    
    public ScriptServiceContext(Map map) {
        super(map);
    }
    
    private ServiceProxy findScriptProxy(String serviceName, Map env) {
        if(getMap().containsKey(ServiceContext.USE_DEFAULT)) {
            return new DefaultScriptServiceProxy(serviceName,getMap(), env);
        }
        
        Iterator<ScriptServiceProxyProvider> iter = Service.providers(ScriptServiceProxyProvider.class, ServiceContext.class.getClassLoader());
        while(iter.hasNext()) {
            ScriptServiceProxyProvider p = iter.next();
            if(p.accept(getMap())) {
                return p.create(serviceName,env,getMap());
            }
        }
        return new DefaultScriptServiceProxy(serviceName,getMap(), env);
    }
    
    public <T> T  create(String serviceName) {
        return (T) create(serviceName, new HashMap(), ServiceProxy.class);
    }
    
    public <T> T create(String serviceName, Map env) {
        return (T) create(serviceName, env, ServiceProxy.class);
    }
    
    public <T> T create(String serviceName,  Class<T> clz) {
        return (T) create(serviceName, new HashMap(), clz);
    }
    
    public <T> T create(String serviceName,  Map env, Class<T> clz) {
        ServiceProxy proxy = findScriptProxy(serviceName, env);
        if(clz.equals(ServiceProxy.class)) {
            return (T) proxy;
        }
        ServiceProxyInvocationHandler handler = new ServiceProxyInvocationHandler(proxy);
        return  (T) Proxy.newProxyInstance( ServiceProxy.class.getClassLoader(), new Class[]{clz}, handler );
    }
    
    
}
