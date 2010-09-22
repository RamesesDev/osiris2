package com.rameses.osiris2.client;

import com.rameses.classutils.AnnotationFieldHandler;
import com.rameses.classutils.ClassDefUtil;
import com.rameses.osiris2.CodeProvider;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;

public class GroovyControllerProvider implements CodeProvider {
    
    private AnnotationFieldHandler fieldHandler = new FieldInjectionHandler();
    private GroovyClassLoader loader;
    
    public GroovyControllerProvider(ClassLoader cl) {
        this.loader = new GroovyClassLoader( cl );
    }
    
    public Class createClass(String source) {
        try {
            return loader.parseClass( new ByteArrayInputStream(source.getBytes()) );
        } catch(Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }
    
    public Object createObject(Class clazz) {
        try {
            Object retVal = clazz.newInstance();
            ClassDefUtil.getInstance().injectFields(retVal, fieldHandler);
            return retVal;
        } catch(Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }
 
}

