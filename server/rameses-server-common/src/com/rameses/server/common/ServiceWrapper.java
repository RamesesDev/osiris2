/*
 * ServiceInvoker.java
 * Created on September 19, 2011, 5:54 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.server.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.beanutils.MethodUtils;

/**
 *
 * @author jzamss
 */
public class ServiceWrapper {
    
    private String serviceName;
    private Object service;
    
    /** Creates a new instance of ServiceInvoker */
    public ServiceWrapper(String serviceName) {
        try {
            InitialContext ctx = new InitialContext();
            this.service = ctx.lookup( serviceName );
            this.serviceName = serviceName;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Object invoke(String method, Object[] values) throws Exception {
        Method[] methods = getMethodByName(service, method, values);
        Object response = null;
        boolean methodFound = false;
        for (int i=0; i<methods.length; i++) {
            try {
                response = MethodUtils.invokeMethod(service, method, values, methods[i].getParameterTypes());
                methodFound = true;
                break; //break the loop
            } catch(NoSuchMethodException nsme) {;}
            catch(IllegalArgumentException ie) {;}
        }
        if (!methodFound)
            throw new Exception("No such method found '" + method + "' for service '" + serviceName );
         return response;   
    }
    
    private Method[] getMethodByName(Object bean, String name, Object[] values) throws Exception {
        int argCount = 0;
        if( values!=null) argCount = values.length;
        List list = new ArrayList();
        Method[] methods = bean.getClass().getMethods();
        for (int i=0; i<methods.length; i++) {
            if (!methods[i].getName().equals(name)) continue;
            if (methods[i].getParameterTypes().length != argCount) continue;
            list.add(methods[i]);
        }
        return (Method[]) list.toArray(new Method[]{});
    }
    
}
