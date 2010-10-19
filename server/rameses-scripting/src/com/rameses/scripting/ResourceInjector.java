/*
 * ResourceInjectionHandler.java
 *
 * Created on October 15, 2010, 2:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.classutils.AnnotationFieldHandler;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ms
 */
public class ResourceInjector implements AnnotationFieldHandler {
    
    private Map<Class, Handler> map = new Hashtable(); 
    
    public ResourceInjector() {
    }

    public final void addResourceHandler(Handler h ) {
        map.put( h.getAnnotationClass(), h );
    }
    
    public Object getResource(Field f, Annotation a) throws Exception {
        if( map.containsKey(a.annotationType()) ) {
            return map.get(a.annotationType()).getResource(a);
        }
        return null;
    }
    
   
    public static interface Handler {
        Class getAnnotationClass();
        Object getResource(Annotation a);
    }
    
}
