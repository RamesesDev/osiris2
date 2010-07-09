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

/**
 *
 * @author elmo
 */
public interface AnnotationFieldHandler {

    Object getResource( Field f, Annotation a ) throws Exception;
}
