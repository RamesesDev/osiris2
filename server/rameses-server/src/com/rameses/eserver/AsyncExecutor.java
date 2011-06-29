/*
 * AsyncExecutor.java
 * Created on June 28, 2011, 8:22 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class AsyncExecutor implements Runnable {
    
    private String script;
    private String method;
    private Object[] params;
    private Map env;
    
    //message options
    private String destinationType = "queue";
    private boolean hasReturnType = false;
    private String responseHandler;
    private boolean looping = false;
    private String loopVar;
    private String requestId;
    
    public AsyncExecutor(String requestId, String name, String method, Object[] params, Map env, Map asyncInfo) {
        this.script = name;
        this.method = method;
        this.params = params;
        this.env = env;

        Map pass = new HashMap();
        String destinationType = (String)asyncInfo.get("destination");
        if(destinationType.trim().length()==0) destinationType = "queue";
        
        responseHandler = (String)asyncInfo.get("responseHandler");
        if(responseHandler==null || responseHandler.trim().length()==0) responseHandler = null;
        try {
            hasReturnType = Boolean.parseBoolean(asyncInfo.get("hasReturnType")+"");
        }catch(Exception ign){;}
        
        try {
            looping = Boolean.parseBoolean( asyncInfo.get("loop")+"" );
        }catch(Exception ign){;}
        loopVar = (String)asyncInfo.get("loopVar");
        this.requestId = requestId;
    }
    
    public void run() {
        String requestId = null;
        try {
            Map ae = new HashMap();
            ae.put("loop", 0);
            ae.put("requestId", requestId);
            env.put("async-event", ae);

            //execute the result;
            if(!looping) {
                Object result = ScriptServiceDelegate.getScriptService().invoke( "~" +script,method,params,env );
                if(hasReturnType) {
                    ScriptServiceDelegate.getScriptService().pushResponse( this.requestId, result );
                }
            } 
            else {
                while( true ) {
                    int i = Integer.parseInt(ae.get("loop")+"");
                    ae.put("loop", i + 1);
                    Object result = ScriptServiceDelegate.getScriptService().invoke("~" +script,method,params,env);
                    if(result!=null) {
                        if(hasReturnType) {
                            ScriptServiceDelegate.getScriptService().pushResponse( this.requestId, result );
                        }
                        if( result instanceof Exception ) break;
                    } else {
                        break;
                    }
                }
            }
            ScriptServiceDelegate.getScriptService().pushResponse( this.requestId, "EOF" );
        } 
        catch(Exception ex) {
            try {
                ScriptServiceDelegate.getScriptService().pushResponse( this.requestId, ex );
            }
            catch(Exception ign){
                ign.printStackTrace();
            }
        }
    }
}
