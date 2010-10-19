/*
 * InvokerProxy.java
 *
 * Created on November 3, 2009, 1:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class ScriptProxyInvocationHandler  implements InvocationHandler {
    
    private ScriptServiceLocal scriptService;
    private String name;
    private Map env;
    
    public ScriptProxyInvocationHandler(ScriptServiceLocal scriptService, String name, Map env) {
        this.scriptService = scriptService;
        this.name = name;
        this.env = env;
    }
    
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        if( method.getName().equals("toString")) return name;
        return scriptService.invoke(name, method.getName(),args,env);
    }
    
    
}
