
package com.rameses.classutils;

import java.lang.annotation.Annotation;

/**
 *
 * @author elmo
 */
public class AnnotationParser {
    
    private ClassDef classDef;
    
    public AnnotationParser(Object o) {
        try {
            classDef = new ClassDef(o.getClass());
        } catch(Exception e) {
            System.out.println("Error: " + e.getStackTrace());
        }
    }
    
    public AnnotationParser(Class clazz) {
        try {
            classDef = new ClassDef(clazz);
        } catch(Exception e) {
            System.out.println("Error: " + e.getStackTrace());
        }
    }
    
    public void parseType( AnnotationHandler handler ) {
        Class clazz = classDef.getSource();
        for(Annotation a: clazz.getAnnotations()) {
            handler.handle(a);
        }
    }
    
    public void parseFields( AnnotationHandler handler ) {
        for( AnnotationField f: classDef.annotatedFields) {
            handler.handle(f.getField(), f.getAnnotation());
        }
    }
    
    public void parseMethods( AnnotationHandler handler ) {
        for( AnnotationMethod m: classDef.annotatedMethods) {
            handler.handle(m.getMethod(), m.getAnnotation());
        }
    }
    
}
