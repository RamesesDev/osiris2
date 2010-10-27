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
    
    
    public ScriptObjectPoolItem(Class cp, String name, String proxyInterface, Class proxyClass  ) {
        this.name = name;
        this.classDef = new ClassDef( cp );
        this.proxyIntfScript = proxyInterface;
        this.proxyIntfClass = proxyClass;
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
    
    public String getProxyIntfScript() {
        return proxyIntfScript;
    }
    
    public Class getProxyIntfClass() {
        return proxyIntfClass;
    }

    
    public void close() {
        if(scriptObject!=null) scriptObject.addBackToPool(this);
    }

    public void setScriptObject(ScriptObject scriptObject) {
        this.scriptObject = scriptObject;
    }

    
    
}
