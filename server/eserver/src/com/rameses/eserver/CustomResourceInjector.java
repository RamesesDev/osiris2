/*
 * CustomSessionInjector.java
 *
 * Created on October 16, 2010, 10:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.eserver;

import com.rameses.persistence.EntityManager;
import com.rameses.schema.SchemaManager;
import com.rameses.scripting.ResourceInjector;
import com.rameses.scripting.ScriptManager;
import com.rameses.scripting.ScriptProxyInvocationHandler;
import com.rameses.scripting.ScriptServiceLocal;

import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.util.ExprUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

public class CustomResourceInjector extends ResourceInjector {
    
    private String serviceName;
    private SessionContext ctx;
    private Map env;
    private ScriptServiceLocal scriptService;
    
    public CustomResourceInjector(String serviceName, SessionContext ctx, Map env, ScriptServiceLocal ss) {
        this.env = env;
        this.ctx = ctx;
        this.serviceName = serviceName;
        this.scriptService = ss;
        super.addResourceHandler( new ResourceHandler() );
        super.addResourceHandler( new EnvHandler() );
        super.addResourceHandler( new SessionCtxHandler() );
        super.addResourceHandler( new ServiceHandler() );
        super.addResourceHandler( new AsyncEventHandler() );
        super.addResourceHandler( new SqlContextHandler() );
        super.addResourceHandler( new PersistenceContextHandler() );
        super.addResourceHandler( new InvokerHandler() );
    }
    
    private class ResourceHandler implements ResourceInjector.Handler {
        public Class getAnnotationClass() {
            return com.rameses.annotations.Resource.class;
        }
        
        public Object getResource(Annotation annotation) {
            String resname = null;
            try {
                Map m = AppContext.getSysMap();
                resname = ExprUtil.substituteValues(((com.rameses.annotations.Resource)annotation).value(),m);
                if(resname==null)
                    throw new Exception("Resource name must be provided");
                if(!resname.startsWith("java:")) {
                    resname = AppContext.getPath() + resname;
                }
                InitialContext ctx = new InitialContext();
                return ctx.lookup(resname);
            } catch(Exception e) {
                System.out.println("Cannot inject @Resource. " + resname + "." + e.getMessage() );
                return null;
            }
        }
    }
    
    private class EnvHandler implements ResourceInjector.Handler {
        public Class getAnnotationClass() {
            return com.rameses.annotations.Env.class;
        }
        public Object getResource(Annotation a) {
            return env;
        }
    }
    
    private class SessionCtxHandler implements ResourceInjector.Handler {
        public Class getAnnotationClass() {
            return com.rameses.annotations.SessionContext.class;
        }
        public Object getResource(Annotation a) {
            return ctx;
        }
    }
    
    private class ServiceHandler implements ResourceInjector.Handler {
        public Class getAnnotationClass() {
            return com.rameses.annotations.Service.class;
        }
        public Object getResource(Annotation a) {
            com.rameses.annotations.Service asvc = (com.rameses.annotations.Service)a;
            Map m = AppContext.getSysMap();
            String scriptname = ExprUtil.substituteValues(asvc.value(),m);
            String host = ExprUtil.substituteValues(asvc.host(),m);
            if(scriptname==null || scriptname.trim().length()==0) scriptname = serviceName;
            
            ScriptProxyInvocationHandler handler = null;
            if(host==null || host.trim().length()==0) {
                handler = new ScriptProxyInvocationHandler(scriptService,scriptname,env);
            } else {
                throw new RuntimeException("Remote proxy not yet handled");
                //if(scriptname==null || scriptname.trim().length()==0)
                //    throw new IllegalStateException("Please provide a remote service name value for @Service");
                //return mbean.createRemoteProxy(scriptname, env, host );
            }
            return ScriptManager.getInstance().createProxy( scriptname, handler );
        }
    }
    
    private class AsyncEventHandler implements ResourceInjector.Handler {
        public Class getAnnotationClass() {
            return com.rameses.annotations.AsyncEvent.class;
        }
        public Object getResource(Annotation a) {
            return env.get("async-event");
        }
    }
    
    private class SqlContextHandler implements ResourceInjector.Handler {
        public Class getAnnotationClass() {
            return com.rameses.annotations.SqlContext.class;
        }
        public Object getResource(Annotation a) {
            Map m = AppContext.getSysMap();
            String dsName =ExprUtil.substituteValues(((com.rameses.annotations.SqlContext)a).value(), m);
            if(dsName!=null && dsName.trim().length()>0) {
                return SqlManager.getInstance().createContext( AppContext.lookupDs(dsName) );
            } else {
                return SqlManager.getInstance().createContext();
            }
        }
    }
    
    private class PersistenceContextHandler implements ResourceInjector.Handler {
        
        public Class getAnnotationClass() {
            return com.rameses.annotations.PersistenceContext.class;
        }
        public Object getResource(Annotation a) {
            Map m = AppContext.getSysMap();
            String dsName = ExprUtil.substituteValues(((com.rameses.annotations.PersistenceContext)a).value(), m);
            SqlContext sqlContext = null;
            if(dsName!=null && dsName.trim().length()>0) {
                sqlContext = SqlManager.getInstance().createContext( AppContext.lookupDs(dsName) );
            } else {
                sqlContext = SqlManager.getInstance().createContext();
            }
            return new EntityManager( SchemaManager.getInstance(),sqlContext);
        }
    }
    
    private class InvokerHandler implements ResourceInjector.Handler {
        
        public Class getAnnotationClass() {
            return com.rameses.annotations.Invoker.class;
        }
        public Object getResource(Annotation a) {
            return new InvokerProxy(env);
        }
    }
    
}
