/*
 * FieldInjectionHandler.java
 *
 * Created on August 11, 2010, 2:21 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.classutils.AnnotationFieldHandler;
import com.rameses.rcp.annotations.Script;
import com.rameses.rcp.annotations.Service;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


public class FieldInjectionHandler implements AnnotationFieldHandler {
    
    public FieldInjectionHandler() {
    }
    
    public Object getResource(Field f, Annotation a) throws Exception {
        if ( a.annotationType() == Service.class ) {
            Service s = (Service) f.getAnnotation(Service.class);
            String serviceName = s.value();
            String hostKey = s.host();
            if(serviceName==null || serviceName.trim().length()==0)
                return InvokerProxy.getInstance();
            else {
                //return InvokerProxy.getInstance().create(serviceName, hostKey);
                if( f.getType() != Object.class && f.getType().isInterface() ) {
                    return InvokerProxy.getInstance().create(serviceName, f.getType());
                }
                
                return InvokerProxy.getInstance().create(serviceName);
            }
        }
        else if ( a.annotationType() == Script.class ) {
            Script s = (Script) f.getAnnotation(Script.class);
            String scriptName = s.value();
            if( scriptName == null || scriptName.trim().length()==0 )
                return ScriptProvider.getInstance();
            else
                return ScriptProvider.getInstance().create(scriptName);
        }
        
        return null;
    }
    
}
