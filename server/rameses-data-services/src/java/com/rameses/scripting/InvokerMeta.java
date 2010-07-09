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
import javax.ejb.SessionContext;

/**
 *
 * Meta Data for Invoker
 */
public class InvokerMeta implements Serializable {
    
    private String name;
    private ClassDef classDef;
    
    public InvokerMeta(Class cp, String name ) {
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
    
  
    
}
