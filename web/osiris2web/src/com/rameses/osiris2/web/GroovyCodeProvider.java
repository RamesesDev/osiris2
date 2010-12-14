package com.rameses.osiris2.web;

import com.rameses.osiris2.CodeProvider;
import com.rameses.osiris2.web.annotations.Service;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

public class GroovyCodeProvider implements CodeProvider {
    
    private GroovyClassLoader loader;
    
    public GroovyCodeProvider(ClassLoader cl) {
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
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(Service.class)) {
                    boolean b = f.isAccessible();
                    f.setAccessible(true);
                    try {
                        Service s = (Service) f.getAnnotation(Service.class);
                        String serviceName = s.value();
                        String hostKey = s.host();
                        f.set(retVal, WebContext.getInvokerProxy().create(serviceName, hostKey));
                    } catch(Exception ign) {
                        System.out.println("error injecting @Service->" + ign.getMessage());
                    }
                    f.setAccessible(b);
                }
            }
            return retVal;
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public Class loadClass(String className) {
        try {
            return loader.loadClass(className);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
}

