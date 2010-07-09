/*
 * AnnotatedMeta.java
 *
 * Created on July 3, 2009, 9:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.classutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author elmo
 */
public interface AnnotationHandler {

    void handle( Annotation a );
    void handle( Field f, Annotation a ) ;
    void handle( Method m, Annotation a ) ;
    
}
