package com.rameses.invoker.client;

import com.rameses.common.AsyncListener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;


public class InvokerHandler implements InvocationHandler {
    
    private String serviceName;
    private Map<String,AsyncListener> handlers;
    private Map env;
    private HttpScriptService scriptService;
    private AbstractScriptServiceProxy proxy;
    
    public InvokerHandler(AbstractScriptServiceProxy p, HttpScriptService s, String serviceName, Map handlers, Map env) {
        this.serviceName = serviceName;
        this.handlers = handlers;
        this.env = env;
        this.scriptService = s;
        this.proxy = p;
    }
    
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        if( method.getName().equals("toString")) return serviceName;
        try {
            Object o = scriptService.invoke( serviceName, method.getName(), args, env );
            if( o !=null  && (o instanceof String) && ((String)o).startsWith("ASYNC")) {
                //match first the listener
                try {
                    AsyncListener listener = handlers.get( method.getName() );
                    if(listener==null) listener = new UnhandledMessageListener();
                    ResponseHandler r = new ResponseHandler(scriptService, (String)o, listener );
                    proxy.invokeLater(r);
                } catch(Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            return o;
        } catch(Exception e) {
            throw e;
        }
    }
    
    
}