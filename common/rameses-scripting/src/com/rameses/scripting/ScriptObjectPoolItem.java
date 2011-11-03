/*
 * InvokerMeta.java
 *
 * Created on June 26, 2009, 7:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.annotations.AsyncEvent;
import com.rameses.annotations.Env;
import com.rameses.annotations.Invoker;
import com.rameses.annotations.PersistenceContext;
import com.rameses.annotations.Resource;
import com.rameses.annotations.Script;
import com.rameses.annotations.Service;
import com.rameses.annotations.SessionContext;
import com.rameses.annotations.SqlContext;
import com.rameses.classutils.AnnotationField;
import com.rameses.classutils.ClassDef;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 *
 * Meta Data for Invoker
 */
public class ScriptObjectPoolItem implements Serializable {
    
    
    private String name;
    private ClassDef classDef;
    private String proxyIntfScript;
    private Class proxyIntfClass;
    private ScriptObject scriptObject;
    
    private Object instance;
    
    public ScriptObjectPoolItem(Class cp, String name, String proxyInterface, Class proxyClass  ) {
        this.name = name;
        this.classDef = new ClassDef( cp );
        this.proxyIntfScript = proxyInterface;
        this.proxyIntfClass = proxyClass;
    }
    
    public Class getTargetClass() {
        return classDef.getSource();
    }
    
    public Object getInstance() throws Exception {
        if(instance==null) instance = getTargetClass().newInstance();
        return instance;
    }
    
    public String getName() {
        return name;
    }
    
    public ClassDef getClassDef() {
        return classDef;
    }
    
    public String getProxyIntfScript() {
        return proxyIntfScript;
    }
    
    public Class getProxyIntfClass() {
        return proxyIntfClass;
    }
    
    
    public void close() {
        if(scriptObject!=null) {
            cleanUpResources();
            //we must clean up the resources before adding back to the pool
            scriptObject.addBackToPool(this);
        }
    }
    
    private void cleanUpResources() {
        if(instance!=null) {
            for(AnnotationField af: classDef.getAnnotatedFields()) {
                try {
                    Class annotType = af.getAnnotation().annotationType();
                    boolean handleIt = false;
                    if(annotType==PersistenceContext.class) handleIt = true;
                    else if(annotType==Resource.class) handleIt = true;
                    else if(annotType==SqlContext.class) handleIt = true;
                    else if(annotType==Service.class) handleIt = true;
                    else if(annotType==Env.class) handleIt = true;
                    else if(annotType==AsyncEvent.class) handleIt = true;
                    else if(annotType==Invoker.class) handleIt = true;
                    else if(annotType==SessionContext.class) handleIt = true;
                    else if(annotType==Script.class) handleIt = true;
                    if(handleIt) {
                        //System.out.println("cleaning up " + annotType + " for " + this.getName() );
                        Field fld = af.getField();
                        boolean accessible = fld.isAccessible();
                        fld.setAccessible(true);
                        fld.set( instance, null );
                        fld.setAccessible(accessible);
                        
                    }
                } catch(Exception ign){
                    System.out.println("error cleanup ->" + ign.getMessage());
                }
            }
        }
    }
    
    public void setScriptObject(ScriptObject scriptObject) {
        this.scriptObject = scriptObject;
    }
    
    
    
}
