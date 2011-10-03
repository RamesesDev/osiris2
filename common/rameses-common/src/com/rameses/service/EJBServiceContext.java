/*
 * EJBServiceContext.java
 * Created on September 21, 2011, 9:33 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service;

import com.sun.jmx.remote.util.Service;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class EJBServiceContext extends ServiceContext {
    
    
    public EJBServiceContext(Map map) {
        super(map);
    }
    
    private ServiceProxy findServiceProxy(String serviceName) {
        if(getMap().containsKey(ServiceContext.USE_DEFAULT)) {
            return new DefaultEJBServiceProxy(serviceName,getMap());
        }
        
        Iterator<EJBServiceProxyProvider> iter = Service.providers(EJBServiceProxyProvider.class, ServiceContext.class.getClassLoader() );
        while(iter.hasNext()) {
            EJBServiceProxyProvider p = iter.next();
            if(p.accept(getMap())) {
                return p.create(serviceName, getMap());
            }
        }
        return new DefaultEJBServiceProxy(serviceName,getMap());
    }

    public <T> T create(String serviceName) {
        return (T) create(serviceName, ServiceProxy.class);
    }
    
    public <T> T create(String serviceName, Class<T> clz) {
        ServiceProxy proxy = findServiceProxy(serviceName);
        if(clz.equals(ServiceProxy.class)) {
            return (T) proxy;
        }
        ServiceProxyInvocationHandler handler = new ServiceProxyInvocationHandler(proxy);
        return  (T) Proxy.newProxyInstance( ServiceProxy.class.getClassLoader(), new Class[]{clz}, handler );
    }

}
