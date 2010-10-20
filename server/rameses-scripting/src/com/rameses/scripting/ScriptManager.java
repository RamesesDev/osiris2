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
import java.util.Hashtable;
import java.util.Map;

public abstract class ScriptManager {
    
    public static ScriptManager instance;
    private boolean loaded;
    private int version;
    
    public static ScriptManager getInstance() {
        if(instance==null) {
            instance = new ScriptManagerImpl();
        }
        return instance;
    }
    
    public static void setInstance(ScriptManager scriptManager ) {
        instance = scriptManager;
    }
    
    private Map<String,ScriptObject> scriptObjects = new Hashtable();
   
    public abstract ScriptProvider getScriptProvider();

    private ScriptLoader scriptLoader;
    private InterceptorLoader interceptorLoader;
    
    public ScriptLoader getScriptLoader() {
        if(scriptLoader==null) scriptLoader = new DefaultScriptLoader(this);
        return scriptLoader;
    }

    public InterceptorLoader getInterceptorLoader() {
        if(interceptorLoader==null) interceptorLoader = new DefaultInterceptorLoader(this);
        return interceptorLoader;
    }
    
    //we have to build the interfaces on the fly
    private Map<String, Class> proxyInterfaces = Collections.synchronizedMap(new Hashtable());
    private InterceptorManager interceptorManager = new InterceptorManager();
    
    protected abstract void init();
    protected abstract void destroy();
    
    /**
     * call this to load or reload.
     */
    public final void load() {
        System.out.println("loading version " + (version++));
        init();
        loaded = true;
        scriptObjects.clear();
        interceptorManager.setLoader( getInterceptorLoader() );
        interceptorManager.load();
        proxyInterfaces.clear();
    }
    
    public final void reload(String name) {
        scriptObjects.remove( name );
    }
    
    public final ScriptObject getScriptObject(String name) {
        if(!loaded)  load();
        if( !scriptObjects.containsKey(name) ) {
            ScriptObject so = getScriptLoader().findScript(name);
            scriptObjects.put( name, so );
        }
        return scriptObjects.get(name);
    }
    
    public final InterceptorManager getInterceptorManager() {
        return interceptorManager;
    }
    
    public byte[] getProxyIntfBytes(String name) {
        try {
            String proxyInterface = getScriptObject(name).getProxyIntfScript();
            if(proxyInterface==null)
                throw new IllegalStateException("Proxy interface " + name + " not found. Please ensure that there is at least one @ProxyMethod");
            return proxyInterface.getBytes();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public ScriptExecutor createExecutor( String serviceName, String methodName, Object[] params ) throws Exception {
        return createExecutor( serviceName, methodName, params, null );
    }
    
    public ScriptExecutor createExecutor( String serviceName, String methodName, Object[] params, ResourceInjector injector ) throws Exception {
        
        boolean bypassAsync = false;
        if( serviceName.startsWith("~") ) {
            bypassAsync = true;
            serviceName = serviceName.substring(1);
        }
        
        ScriptObject so = getScriptObject(serviceName);
        Object target = so.getTargetClass().newInstance();
        ClassDef classDef = so.getClassDef();
        Method actionMethod = classDef.findMethodByName( methodName );
        
        //check first for validation
        if(params!=null) {
            checkParameters( so, methodName, params );
        }
        
        boolean async = (!bypassAsync && actionMethod.isAnnotationPresent(Async.class));
        if(!async) {
            //check first if we need to have interceptors.
            //check if interceptors should fire. This is applied only to all proxy methods that are no local
            boolean applyInterceptors = false;
            if(actionMethod.isAnnotationPresent(ProxyMethod.class)) {
                ProxyMethod pm = actionMethod.getAnnotation(ProxyMethod.class);
                if(!pm.local()) applyInterceptors = true;
            }
            
            //inject the resources here.
            if(injector!=null) {
                classDef.injectFields( target, injector );
            }
            
            //build the interceptors first
            if(applyInterceptors) getInterceptorManager().injectInterceptors(so, methodName);
            
            return new LocalScriptExecutor(so, methodName, target, actionMethod);
        } 
        else {
            return new AsyncScriptExecutor( serviceName, methodName, actionMethod);
        }
    }
    
    public Object createProxy( String name, ScriptProxyInvocationHandler h  ) {
        Class clazz = null;
        if(! proxyInterfaces.containsKey(name) ) {
            String s = getScriptObject( name ).getProxyIntfScript();
            clazz = getScriptProvider().parseClass(s);
            proxyInterfaces.put(name, clazz);
        }
        else {
            clazz = proxyInterfaces.get(name);
        }
        return Proxy.newProxyInstance( clazz.getClassLoader(), new Class[]{clazz}, h);
    }
    
    public void checkParameters( ScriptObject obj, String method, Object[] args ) throws Exception {
        CheckedParameter[] checkedParams = obj.getCheckedParameters(method);
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
    
}
