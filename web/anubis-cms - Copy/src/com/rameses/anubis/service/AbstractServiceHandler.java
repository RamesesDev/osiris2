/*
 * HttpInvicationHandler.java
 *
 * Created on June 23, 2012, 3:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.service;


import com.rameses.anubis.ServiceAdapter;
import com.rameses.anubis.ServiceInvoker;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class AbstractServiceHandler implements ServiceAdapter {
    
    private GroovyClassLoader classLoader =  new GroovyClassLoader(getClass().getClassLoader());
    private Class metaClass;
    
    private Class getMetaClass() throws Exception {
        if( metaClass == null ) {
            StringBuilder builder = new StringBuilder();
            builder.append( "public class MyMetaClass  { \n" );
            builder.append( "    def invoker; \n");
            builder.append( "    public Object invokeMethod(String string, Object args) { \n");
            builder.append( "        return invoker.invokeMethod(string, args); \n" );
            builder.append( "    } \n");
            builder.append(" } ");
            metaClass = classLoader.parseClass( builder.toString() );
        }
        return metaClass;
    }
    
    protected abstract ServiceInvoker getServiceInvoker(String name, Map conf);
    
    public Object create(String name, Map conf) {
        try {
            Object obj =  getMetaClass().newInstance();
            ServiceInvoker si = getServiceInvoker(name, conf);
            ((GroovyObject)obj).setProperty( "invoker", si );
            return obj;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    
}
