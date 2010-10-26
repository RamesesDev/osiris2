/*
 * ScriptInterfaceProvider.java
 *
 * Created on October 24, 2010, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.invoker.client;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ms
 */
public abstract class ScriptInterfaceProvider {
    
    private static ScriptInterfaceProvider instance;
    
    public static void setInstance(ScriptInterfaceProvider i) {
        instance = i;
    }
    
    public static ScriptInterfaceProvider getInstance() {
        return instance;
    }

    private Map<String,Class> interfaces = new HashMap();
    protected abstract Class parseClass(byte[] bytes);
    public abstract ClassLoader getProxyClassLoader();
    
    public ScriptInterfaceProvider() {
    }
    
    public final Class getInterface(String name, HttpScriptService svc) {
        Class cls = interfaces.get(name);
        if(cls==null) {
            synchronized(interfaces) {
                if( !interfaces.containsKey(name) ) {
                    byte[] bytes = svc.getScriptInfo( name );
                    Class clazz = parseClass(bytes);
                    interfaces.put( name, clazz );    
                }
            }
            cls = interfaces.get(name); 
        }
        return cls;
    }
    
    
}
