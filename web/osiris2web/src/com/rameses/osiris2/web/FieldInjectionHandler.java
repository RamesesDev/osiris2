/*
 * FieldInjectionHandler.java
 *
 * Created on August 11, 2010, 2:21 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.classutils.AnnotationFieldHandler;
import com.rameses.osiris2.web.annotations.Script;
import com.rameses.osiris2.web.annotations.Service;
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
                return WebContext.getInstance().getInvokerProxy();
            else {
                //return InvokerProxy.getInstance().create(serviceName, hostKey);
                return WebContext.getInstance().getInvokerProxy().create(serviceName);
            }
        } else if ( a.annotationType() == Script.class ) {
            Script s = (Script) f.getAnnotation(Script.class);
            String scriptName = s.value();
            if( scriptName == null || scriptName.trim().length()==0 )
                return WebContext.getInstance().getScriptProvider();
            else
                return WebContext.getInstance().getScriptProvider().create(scriptName);
        }
        
        return null;
    }
    
}
