/*
 * ScriptExecutorAsync.java
 *
 * Created on October 15, 2010, 2:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.annotations.Async;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class AsyncScriptExecutor implements ScriptExecutor, Serializable {
    
    private String serviceName;
    private String methodName;
    private Object target;
    private Method actionMethod;
    
    public AsyncScriptExecutor(String serviceName, String methodName, Method actionMethod) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.actionMethod = actionMethod;
    }
    

    /************************************************
    * ASYNCHRONOUS PROCESS
    ***********************************************/
    public Object execute(ScriptServiceLocal scriptService, Object[] params, Map env) throws Exception {

        Map pass = new HashMap();
        Async asc = (Async) actionMethod.getAnnotation(Async.class);
        String destinationType = asc.type();
        if(destinationType.trim().length()==0) destinationType = "queue";
        String responseHandler = asc.responseHandler();
        if(responseHandler.trim().length()==0) responseHandler = null;
        boolean hasReturnType = (!actionMethod.getReturnType().toString().equals("void"));
        
        boolean loop = asc.loop();
        
        pass.put("script", serviceName);
        pass.put("method", methodName);
        pass.put("params", params);
        
        pass.put("env", env);
        pass.put("hasReturnType", hasReturnType);
        
        //apply response handler only if there is a return type
        if(hasReturnType) {
            if( responseHandler!=null ) pass.put("responseHandler", responseHandler);
            if( loop ) {
                pass.put("loop", true);
                pass.put( "loopVar", asc.loopVar() );
            }
        }
        return scriptService.invokeAsync( pass, destinationType );
    }
    
}
