/*
 * HttpServiceHandler.java
 *
 * Created on June 24, 2012, 12:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.service;

import com.rameses.anubis.ServiceInvoker;
import com.rameses.classutils.ClassDefMap;
import com.rameses.service.EJBServiceContext;
import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class ScriptServiceHandler  extends AbstractServiceHandler {
    
    public String getName() {
        return "script";
    }
    
    protected ServiceInvoker getServiceInvoker(String name, Map conf) {
        return new MyScriptInvoker(name, conf);
    }
    
    public Map getClassInfo(String name, Map conf) {
        String host = (String)conf.get("app.host");
        if(host==null) host = (String)conf.get("host");
        if( host == null )
            throw new RuntimeException("app.host is not defined");
        String appContext = (String)conf.get("app.context");
        if(appContext==null) appContext = (String)conf.get("context");
        if(appContext == null )
            throw new RuntimeException("app.context is not defined");
        Map map = new HashMap();
        map.put("app.host", host);
        map.put("app.context", appContext);
        EJBServiceContext ctx = new EJBServiceContext(map);
        IScriptService svc = ctx.create( "ScriptService/local", IScriptService.class );
        GroovyClassLoader loader = new GroovyClassLoader();
        
        Class clazz = loader.parseClass(new ByteArrayInputStream( svc.getScriptInfo(name) ));
        return  ClassDefMap.toMap(clazz);
    }
    
    private interface IScriptService  {
        byte[] getScriptInfo(String name);
    }
    
    private class MyScriptInvoker implements ServiceInvoker {
        
        private ServiceProxy serviceProxy;
        
        public MyScriptInvoker(String name, Map conf) {
            String host = (String)conf.get("app.host");
            if(host==null) host = (String)conf.get("host");
            if( host == null )
                throw new RuntimeException("app.host is not defined");
            String appContext = (String)conf.get("app.context");
            if(appContext==null) appContext = (String)conf.get("context");
            if(appContext == null )
                throw new RuntimeException("app.context is not defined");
            Map map = new HashMap();
            map.put("app.host", host);
            map.put("app.context", appContext);
            ScriptServiceContext ctx = new ScriptServiceContext(map);
            
            //build the env. include special parameters
            Map env = new HashMap();
            for(Object es : conf.entrySet()) {
                Map.Entry me = (Map.Entry)es;
                if( me.getKey().toString().startsWith("ds.")) {
                    env.put( me.getKey(), me.getValue() );
                }
            }
            serviceProxy = ctx.create( name, env );
        }
        public Object invokeMethod(String methodName, Object[] args) {
            try {
                return serviceProxy.invoke( methodName, args );
            } catch(Exception ex) {
                throw new RuntimeException(ex);
                //return "<font color=red>error service:" + ex.getMessage()+"</font>";
            }
        }
    }
    
    
}
