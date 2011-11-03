/*
 * ScriptExecutor.java
 *
 * Created on October 15, 2010, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ms
 */
public class LocalScriptExecutor implements ScriptExecutor {
    
    private String name;
    private String methodName;
    
    private Method actionMethod;
    private List<String> beforeInterceptors;
    private List<String> afterInterceptors;
    
    private ScriptObjectPoolItem pooledItem;
    private ResourceInjector injector;
    
    
    public LocalScriptExecutor(String serviceName, String methodName, ScriptObjectPoolItem poolItem, Method actionMethod, ResourceInjector injector, List<String> before, List<String>after) {
        this.name = serviceName;
        this.methodName = methodName;
        this.actionMethod = actionMethod;
        this.pooledItem = poolItem;
        this.beforeInterceptors = before;
        this.afterInterceptors = after;
        this.injector = injector;
    }
    
    public Object execute(ScriptServiceLocal scriptService, Object[] params, Map env) throws Exception {
        //start transaction
        ActionEvent ae = null;
        ScriptEval se = null;
        try {
            Object target = pooledItem.getInstance();
            
            if(injector!=null) {
                pooledItem.getClassDef().injectFields( target, injector );
            }
            
            ae = new ActionEvent( name, actionMethod.getName(), params, env);
            se = new ScriptEval(ae);
            for(String b: beforeInterceptors) {
                Object test = executeInterceptor(b, ae, se, scriptService, env);
                if(test!=null && (test instanceof Exception )) return test;
            }
            Object retval =  actionMethod.invoke( target, params );
            //do not proceed with other interceptors if the return value is an exception
            if( retval instanceof Exception )
                return retval;
            
            ae.setResult(retval);
            for(String b: afterInterceptors ) {
                Object test = executeInterceptor(b, ae, se, scriptService, env);
                if(test!=null && (test instanceof Exception )) return test;
            }
            return retval;
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(se!=null) se.destroy();
            if(ae!=null) ae.destroy();
            pooledItem.close();
        }
    }
    
    private Object executeInterceptor(String serviceName, ActionEvent ae, ScriptEval se, ScriptServiceLocal scriptService, Map env) {
        boolean hasParm = false;
        boolean passEval = true;
        
        if( serviceName.indexOf("#")>0) {
            String[] sarr = serviceName.split("#");
            serviceName = sarr[0];
            se.setResult(ae.getResult());
            passEval = se.eval(sarr[1]);
        }
        
        String[] arr = serviceName.split("\\.");
        String n = arr[0];
        if(n.startsWith("@")) {
            hasParm = true;
            n = n.substring(1);
        }
        String _action = arr[1];
        Object retval = null;
        if(passEval) {
            if(hasParm)
                retval = scriptService.invoke( n,_action,new Object[]{ae},env);
            else
                retval = scriptService.invoke(n,_action,new Object[]{},env);
        }
        return retval;
    }
    
    public void close() {
        
    }
    
}
