/*
 * InvokerMeta.java
 *
 * Created on June 26, 2009, 7:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.classutils.ClassDef;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * Meta Data for Invoker
 */
public class ScriptObject implements Serializable {
    
    private String name;
    private ClassDef classDef;
    private byte[] proxyBytes;
    private Class proxyClass;
    
    //store the method name and 
    private Map<String, List<String>> beforeInterceptors = new Hashtable();
    private Map<String, List<String>> afterInterceptors = new Hashtable();
    
    
    public ScriptObject(Class cp, String name ) {
        this.name = name;
        this.classDef = new ClassDef( cp );
    }
    
    public Class getTargetClass() {
        return classDef.getSource();
    }

    public String getName() {
        return name;
    }
    
    public ClassDef getClassDef() {
        return classDef;
    }
    
    public byte[] getProxyInterface() {
        if(proxyBytes==null) {
            String intf = InterfaceBuilder.getProxyInterfaceScript(name, classDef.getSource());
            proxyBytes = intf.getBytes();            
        }
        return proxyBytes;
    }
    
    public void buildInterceptors() {
       
    }
    
    //pass the list of 
    public List<String> getBeforeInterceptors(String methodName, List<InterceptorDef> beforeInterceptorList) {
       return null; 
    }
    
    public List<String> getAfterInterceptors(String methodName, List<InterceptorDef> afterInterceptorList) {
        return null;
    }
    
}
