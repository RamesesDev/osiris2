/*
 * InvokerHelper.java
 *
 * Created on December 3, 2010, 8:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.invoker.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.MethodUtils;

/**
 *
 * @author ms
 */
public final class InvokerHelper {
    
    public static Object invoke( NameParser np, Object[] values ) throws Exception {
        InitialContext ctx = new InitialContext();
        Object bean = ctx.lookup(np.getLocalServiceName());
        Method[] methods = InvokerHelper.getMethodByName(bean, np.getAction(), values.length);
        boolean methodFound = false;
        Object response = null;
        for (int i=0; i<methods.length; i++) {
            try {
                response = MethodUtils.invokeMethod(bean, np.getAction(), values, methods[i].getParameterTypes());
                methodFound = true;
                break; //break the loop
            } catch(NoSuchMethodException nsme) {;}
        }
        if (!methodFound)
            throw new Exception("No such method found '" + np.getAction() + "' for service '" + np.getService() );
        
        return response;    
    } 
            
    
    /***
     * this will locate all methods that matches the name, with the same arg count.
     */
    public static Method[] getMethodByName(Object bean, String name, int argCount) throws Exception {
        List list = new ArrayList();
        Method[] methods = bean.getClass().getMethods();
        for (int i=0; i<methods.length; i++) {
            if (!methods[i].getName().equals(name)) continue;
            if (methods[i].getParameterTypes().length != argCount) continue;
            list.add(methods[i]);
        }
        return (Method[]) list.toArray(new Method[]{});
    }
    
    public static NameParser createNameParser(HttpServletRequest req) {
        return new NameParser( req );
    }
    
    
    
    public static class NameParser {
        private String context;
        private String service;
        private String action;
        
        public NameParser(HttpServletRequest req) {
            StringBuffer reqPath = new StringBuffer();
            if ( req.getPathInfo() != null ) {
                reqPath.append(req.getPathInfo());
            } else if ( req.getServletPath() != null ) {
                reqPath.append(req.getServletPath());
            }
            String[] pathInfos = reqPath.toString().split("\\.");
            String appContext = "";
            if( req.getContextPath() != null && req.getContextPath().trim().length()>0 && !req.getContextPath().equals("/") ) {
                context = req.getContextPath().substring(1);
            }
            service = pathInfos[0].substring(1);
            action = pathInfos[1];
        }

        public String getService() {
            return service;
        }

        public String getAction() {
            return action;
        }

        public String getContext() {
            return context;
        }
        
        public String getLocalServiceName() {
            StringBuffer sb = new StringBuffer();
            if(context!=null) sb.append( context + "/");
            sb.append(service+"/local");
            return sb.toString();
        }
        
        public void setService(String s) {
            service = s;
        }
        
        public void setAction(String a) {
            action = a;
        }
        
        public void setContext(String c) {
            context = c;
        }
    }
    
    
}
