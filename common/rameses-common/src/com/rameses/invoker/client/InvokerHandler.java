package com.rameses.invoker.client;

import com.rameses.common.AsyncHandler;
import com.rameses.common.AsyncResponse;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;


public class InvokerHandler implements InvocationHandler {
    
    private String serviceName;
    private Map env;
    private HttpScriptService scriptService;
    private AbstractScriptServiceProxy proxy;
    
    public InvokerHandler(AbstractScriptServiceProxy p, HttpScriptService s, String serviceName, Map env) {
        this.serviceName = serviceName;
        this.env = env;
        this.scriptService = s;
        this.proxy = p;
    }
    
    public Object invoke(Object object, Method method, Object[] xargs) throws Throwable {
        if( method.getName().equals("toString")) return serviceName;
        try {
            AsyncHandler handler = null;
            
            Object[] args = xargs;
            //check if last parameter if any is instance of AsyncHandler
            if(args!=null && args.length>0) {
                Object lastArg = args[args.length-1];
                if(lastArg instanceof AsyncHandler) {
                    handler = (AsyncHandler)lastArg;
                    Object[] mlist = new Object[args.length-1];
                    for( int j=0; j<mlist.length;j++ ) {
                        mlist[j] = args[j];
                    } 
                    args = mlist;
                }
            }
            
            Object o = scriptService.invoke( serviceName, method.getName(), args, env );
            if( o !=null  && (o instanceof AsyncResponse) ) {
                //match first the listener
                try {
                    if(handler==null) handler = new UnhandledMessageListener();
                    ResponseHandler r = new ResponseHandler(scriptService, (AsyncResponse)o, handler );
                    proxy.invokeLater(r);
                    
                    //return the response handler
                    return r;
                    
                } catch(Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            
            return o;
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
}