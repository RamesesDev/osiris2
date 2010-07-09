/*
 * ClassDefs.java
 *
 * Created on July 3, 2009, 10:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.classutils;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author elmo
 */
public final class ClassDefUtil {
    
    private static ClassDefUtil instance;
    
    private AnnotationFieldHandler handler;
    private Map<Class, ClassDef> map = new Hashtable<Class, ClassDef>();
    
    private ClassDefUtil() {}
    
    public static ClassDefUtil getInstance() {
        if ( instance == null ) {
            instance = new ClassDefUtil();
        }
        return instance;
    }
    
    public void injectFields( Object o ) {
        injectFields( o, handler );
    }
    
    public void injectFields( Object o, AnnotationFieldHandler h ) {
        Class clazz = o.getClass();
        parseClass( clazz );
        map.get(clazz).injectFields( o, h );
    }

    public void parseClass( Class clazz ) {
        if( !map.containsKey(clazz) ) {
            ClassDef classDef = new ClassDef(clazz);
            map.put( clazz, classDef );
        }
    }

    public void clearAll() {
        for(ClassDef c: map.values()) {
            c.destroy();
        }
        map.clear();
    }
    
    public void clear(Class clazz) {
        ClassDef c = map.get(clazz);
        if( c != null ) c.destroy();
        map.remove(clazz);
    }
    
    public Method findAnnotatedMethod( Class clazz, Class a ) {
        parseClass(clazz);
        return map.get(clazz).findAnnotatedMethod( a );
    }
    
    public Method[] findAnnotatedMethods( Class clazz, Class a ) {
        parseClass(clazz);
        return map.get(clazz).findAnnotatedMethods( a);
    }
    
    public Method findMethodByName(Class clazz, String methodName) {
        parseClass(clazz);
        return map.get(clazz).findMethodByName(methodName);
    }
    
}
