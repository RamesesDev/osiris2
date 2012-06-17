package com.rameses.osiris2.client;

import com.rameses.common.AsyncHandler;
import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;


public class OsirisInvokerHandler implements InvocationHandler {
    
    private String serviceName;
    private Map appEnv;
    private ServiceProxy proxy;
    
    public OsirisInvokerHandler( String serviceName, Map appEnv) {
        this.serviceName = serviceName;
        this.appEnv = appEnv;
        
        ScriptServiceContext sc = new ScriptServiceContext(appEnv);
        Map clientEnv = OsirisContext.getEnv();
        proxy = sc.create( serviceName, clientEnv );
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
            
            Object o = proxy.invoke( method.getName(), args );
            
            /*
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
            */
            
            return o;
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
}