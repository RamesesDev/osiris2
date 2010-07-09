/*
 * AnnotationField.java
 *
 * Created on July 3, 2009, 9:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.classutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author elmo
 */
public class AnnotationField {
    
    private Field field;
    private Annotation annotation;
    
    public AnnotationField(Field f, Annotation a) {
        this.field = f;
        this.annotation = a;
    }

    public Field getField() {
        return field;
    }

    public Annotation getAnnotation() {
        return annotation;
    }
    
}
