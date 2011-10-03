/*
 * ServiceProxyInvocationHandler.java
 * Created on September 23, 2011, 11:29 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @author jzamss
 */
public class ServiceProxyInvocationHandler implements InvocationHandler{
    
    private ServiceProxy proxy;
    
    /** Creates a new instance of ServiceProxyInvocationHandler */
    public ServiceProxyInvocationHandler(ServiceProxy p) {
        this.proxy = p;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if( args == null )
            return this.proxy.invoke(method.getName());
        else
            return this.proxy.invoke( method.getName(), args );
    }
    
}
