/*
 * ScriptManager.java
 *
 * Created on October 15, 2010, 8:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.scripting;

import com.rameses.annotations.Async;
import com.rameses.annotations.ProxyMethod;
import com.rameses.classutils.ClassDef;

import com.rameses.schema.SchemaManager;
import com.rameses.schema.ValidationResult;
import com.rameses.scripting.impl.ScriptManagerImpl;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ScriptManager {
    
    public static ScriptManager instance;
    private boolean loaded;
    private int version;
    private ScriptLoader scriptLoader;
    private InterceptorLoader interceptorLoader;
    private ScriptObjectPool scriptObjectPool;
    //we have to build the interfaces on the fly
    private Map<String, Class> proxyInterfaces = Collections.synchronizedMap(new Hashtable());
    private InterceptorManager interceptorManager = new InterceptorManager();
    
    private ScheduledExecutorService poolCleaner;
    
    public static ScriptManager getInstance() {
        if(instance==null) {
            instance = new ScriptManagerImpl();
        }
        return instance;
    }
    
    public static void setInstance(ScriptManager scriptManager ) {
        instance = scriptManager;
    }
    
    
    public abstract ScriptProvider getScriptProvider();
    
    
    
    public ScriptLoader getScriptLoader() {
        if(scriptLoader==null) scriptLoader = new DefaultScriptLoader(this);
        return scriptLoader;
    }
    
    public InterceptorLoader getInterceptorLoader() {
        if(interceptorLoader==null) interceptorLoader = new DefaultInterceptorLoader(this);
        return interceptorLoader;
    }
    
    
    protected abstract void init();
    protected abstract void destroy();
    
    /**
     * call this to load or reload.
     */
    public final void load() {
        System.out.println("loading version " + (version++));
        init();
        loaded = true;
        scriptObjectPool = new ScriptObjectPool(getScriptLoader());
        interceptorManager.setLoader( getInterceptorLoader() );
        interceptorManager.load();
        proxyInterfaces.clear();
        if(poolCleaner!=null) poolCleaner.shutdown();
        poolCleaner = Executors.newScheduledThreadPool(1);
        //clean up every 10 seconds
        poolCleaner.scheduleWithFixedDelay(new PoolCleaner(), 10, 10, TimeUnit.SECONDS);
    }
    
    public final void reload(String name) {
        scriptObjectPool.remove( name );
    }
    
    public final ScriptObject getScriptObject(String name) {
        if(!loaded)  load();
        return (ScriptObject)scriptObjectPool.get(name);
    }
    
    public final InterceptorManager getInterceptorManager() {
        return interceptorManager;
    }
    
    public byte[] getProxyIntfBytes(String name) {
        ScriptObjectPoolItem so = null;
        try {
            ScriptObject sm = getScriptObject(name);
            so = sm.getPooledObject();
            String proxyInterface = so.getProxyIntfScript();
            if(proxyInterface==null)
                throw new IllegalStateException("Proxy interface " + name + " not found. Please ensure that there is at least one @ProxyMethod");
            return proxyInterface.getBytes();
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {so.close();}catch(Exception ign){;}
        }
    }
    
    public ScriptExecutor createExecutor( String serviceName, String methodName, Object[] params ) throws Exception {
        return createExecutor( serviceName, methodName, params, null );
    }
    
    public ScriptExecutor createExecutor( String serviceName, String methodName, Object[] params, ResourceInjector injector ) throws Exception {
        ScriptObjectPoolItem so = null;
        
        try {
            boolean bypassAsync = false;
            if( serviceName.startsWith("~") ) {
                bypassAsync = true;
                serviceName = serviceName.substring(1);
            }
            ScriptObject sm = getScriptObject(serviceName);
            so = sm.getPooledObject();
            
            ClassDef classDef = so.getClassDef();
            Method actionMethod = classDef.findMethodByName( methodName );
            
            //check first for validation
            if(params!=null) {
                checkParameters(sm, so, methodName, params );
            }
            //this is so we can monitor this object.
            sm.registerMethodsAccessed(methodName);
            
            boolean async = (!bypassAsync && actionMethod.isAnnotationPresent(Async.class));
            if(!async) {
                //check first if we need to have interceptors.
                //check if interceptors should fire. This is applied only to all proxy methods that are no local
                boolean applyInterceptors = false;
                if(actionMethod.isAnnotationPresent(ProxyMethod.class)) {
                    ProxyMethod pm = actionMethod.getAnnotation(ProxyMethod.class);
                    if(!pm.local()) applyInterceptors = true;
                }
                //build the interceptors first
                if(applyInterceptors) getInterceptorManager().injectInterceptors(sm, methodName);
                List<String> beforeInterceptors = sm.findBeforeInterceptors(methodName);
                List<String> afterInterceptors = sm.findAfterInterceptors(methodName);
                return new LocalScriptExecutor(serviceName, methodName, so, actionMethod, injector, beforeInterceptors,afterInterceptors);
            } else {
                Map asyncInfo = new HashMap();
                Async asc = (Async) actionMethod.getAnnotation(Async.class);
                boolean hasReturnType = (!actionMethod.getReturnType().toString().equals("void"));
                
                asyncInfo.put( "destination", asc.type() );
                asyncInfo.put( "responseHandler", asc.responseHandler() );
                asyncInfo.put( "hasReturnType", hasReturnType );
                asyncInfo.put("loop", asc.loop());
                asyncInfo.put("loopVar", asc.loopVar());
                return new AsyncScriptExecutor( serviceName, methodName, actionMethod, asyncInfo );
            }
        } catch(Exception ee) {
            throw ee;
        }
    }
    
    public Object createProxy( String name, ScriptProxyInvocationHandler h  ) {
        Class clazz = null;
        ScriptObjectPoolItem so = null;
        try {
            if(! proxyInterfaces.containsKey(name) ) {
                ScriptObject sm = getScriptObject( name );
                so = sm.getPooledObject();
                String s = so.getProxyIntfScript();
                clazz = getScriptProvider().parseClass(s);
                proxyInterfaces.put(name, clazz);
            } else {
                clazz = proxyInterfaces.get(name);
            }
            return Proxy.newProxyInstance( clazz.getClassLoader(), new Class[]{clazz}, h);
        } catch(Exception e){
            throw new RuntimeException(e);
        } finally {
            try { so.close(); } catch(Exception e){;}
        }
    }
    
    public void close() {
        if(poolCleaner!=null) poolCleaner.shutdown();
    }
    
    public void checkParameters( ScriptObject sm, ScriptObjectPoolItem obj, String method, Object[] args ) throws Exception {
        CheckedParameter[] checkedParams = sm.getCheckedParameters(method, obj.getClassDef());
        for( CheckedParameter p : checkedParams ) {
            if(p.isRequired() && args[p.getIndex()]==null )
                throw new Exception( "argument " + p.getIndex() + " for method " + method + " must not be null" );
            String schemaName = p.getSchema();
            if(schemaName!=null && schemaName.trim().length()>0) {
                SchemaManager schemaMgr = SchemaManager.getInstance();
                ValidationResult vr = schemaMgr.validate(schemaName, args[p.getIndex()] );
                if(vr.hasErrors()) {
                    throw new Exception(vr.toString());
                }
            }
        }
    }
    
    private class PoolCleaner implements Runnable {
        public void run() {
            if(scriptObjectPool!=null) scriptObjectPool.maintainPoolSize();
        }
    }
}
