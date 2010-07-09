/*
 * ClassMethodKey.java
 *
 * Created on July 3, 2009, 10:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.classutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 *
 * @author elmo
 */
public class AnnotationMethod {
    
    private Annotation annotation;
    private Method method;
    
    public AnnotationMethod( Method method, Annotation a) {
        this.annotation = a;
        this.method = method;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public boolean equals(Object object) {
        if( object == null ) return false;
        if(! (object instanceof AnnotationMethod )) return false;
        AnnotationMethod ma = (AnnotationMethod)object;
        if(! getAnnotation().getClass().getName().equals(ma.getAnnotation().getClass().getName())   )
            return false;
        else
            return true;
    }

    public int hashCode() {
        return (annotation.getClass().getName()).hashCode();    
    }

    public Method getMethod() {
        return method;
    }
    
    
    
}
