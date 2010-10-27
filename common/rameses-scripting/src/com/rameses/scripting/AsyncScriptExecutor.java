/*
 * ScriptExecutorAsync.java
 *
 * Created on October 15, 2010, 2:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;


public class AsyncScriptExecutor implements ScriptExecutor, Serializable {
    
    private String serviceName;
    private String methodName;
    private Object target;
    private Method actionMethod;
    private Map asyncInfo;        
    
    public AsyncScriptExecutor(String serviceName, String methodName, Method actionMethod, Map asyncInfo ) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.actionMethod = actionMethod;
        this.asyncInfo = asyncInfo;
    }
    

    /************************************************
    * ASYNCHRONOUS PROCESS
    ***********************************************/
    public Object execute(ScriptServiceLocal scriptService, Object[] params, Map env) throws Exception {
        return scriptService.invokeAsync( serviceName,methodName,params,env,asyncInfo );
    }

    public void close() {
    }
    
}
