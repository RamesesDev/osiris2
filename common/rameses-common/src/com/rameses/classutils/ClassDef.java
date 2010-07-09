package com.rameses.classutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ClassDef {
    
    private Class clazz;
    
    //this is package level
    List<AnnotationField> annotatedFields;
    List<AnnotationMethod> annotatedMethods;
    
    private List<Method> methods;
    private AnnotationFieldHandler handler;
    
    public ClassDef(Class clazz) {
        this.clazz = clazz;
        annotatedFields =  new ArrayList<AnnotationField>();
        annotatedMethods = new ArrayList<AnnotationMethod>();
        methods = new ArrayList<Method>();
        
        //parse annotated fields
        for(Field f: clazz.getDeclaredFields() ) {
            for(Annotation a: f.getAnnotations()) {
                annotatedFields.add(new AnnotationField(f, a));
            }
        }
        for( Method m : clazz.getDeclaredMethods()) {
            for(Annotation a: m.getAnnotations()) {
                annotatedMethods.add(new AnnotationMethod( m, a));
            }
            methods.add( m );
        }
    }
    
    public AnnotationFieldHandler getHandler() {
        return handler;
    }

    public void setHandler(AnnotationFieldHandler handler) {
        this.handler = handler;
    }
    
    public void injectFields( Object o ) {
        if( handler == null )
            throw new IllegalStateException("Please provide an annotation handler");
        injectFields(o, getHandler());
    }
    
    public void injectFields( Object o, AnnotationFieldHandler handler ) {
        for(AnnotationField f: annotatedFields ) {
            Field fld = f.getField();
            Annotation annot = f.getAnnotation();
            
            try {
                Object res = handler.getResource( fld, annot );
                if( res != null ) {
                    boolean accessible = fld.isAccessible();
                    fld.setAccessible(true);
                    fld.set( o, res );
                    fld.setAccessible(accessible);
                }
                
            } catch(Exception ex) {
                System.out.println("ERROR field->" + fld.getName() + " annotation->" + annot + " " + ex.getMessage());
            }
        }
    }
    
    public Method findAnnotatedMethod( Class a ) {
        for(AnnotationMethod m: annotatedMethods) {
            if( m.getAnnotation().annotationType().getName().equals(a.getName()) ) {
                return m.getMethod();
            }
        }
        return null;
    }
    
    public Method[] findAnnotatedMethods( Class a ) {
        List<Method> list = new ArrayList<Method>(); 
        for(AnnotationMethod m: annotatedMethods) {
            if( m.getAnnotation().annotationType().getName().equals(a.getName()) ) {
                list.add( m.getMethod() );
            }
        }
        return list.toArray(new Method[]{});
    }
    
    public Method findMethodByName( String methodName) {
        for(Method m: methods) {
            if( m.getName().equals(methodName) ) {
                return m;
            }
        }
        return null;
    }

    public void destroy() {
        annotatedFields.clear();
        annotatedMethods.clear();
        methods.clear();
        clazz = null;
        annotatedFields = null;
        annotatedMethods= null;
        methods = null;
        handler = null;
    }
    
    public Class getSource() {
        return clazz;
    }
    
    
}
