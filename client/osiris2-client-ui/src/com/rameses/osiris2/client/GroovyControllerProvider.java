package com.rameses.osiris2.client;

import com.rameses.classutils.AnnotationFieldHandler;
import com.rameses.classutils.ClassDefUtil;
import com.rameses.osiris2.CodeProvider;
import com.rameses.rcp.annotations.Service;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import javax.swing.JOptionPane;

public class GroovyControllerProvider implements CodeProvider {
    
    private AnnotationFieldHandler fieldHandler = new FieldHandler();
    private GroovyClassLoader loader;
    
    public GroovyControllerProvider(ClassLoader cl) {
        this.loader = new GroovyClassLoader( cl );
    }
    
    public Class createClass(String source) {
        try {
            return loader.parseClass( new ByteArrayInputStream(source.getBytes()) );
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
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
    
    
    //<editor-fold defaultstate="collapsed" desc="  FieldHandler (class)  ">
    private class FieldHandler implements AnnotationFieldHandler {
        
        public Object getResource(Field f, Annotation a) throws Exception {
            if ( a.annotationType() == Service.class ) {
                Service s = (Service) f.getAnnotation(Service.class);
                String serviceName = s.value();
                String hostKey = s.host();
                return InvokerProxy.getInstance().create(serviceName, hostKey);
            }
            return null;
        }
        
    }
    //</editor-fold>
    
}

