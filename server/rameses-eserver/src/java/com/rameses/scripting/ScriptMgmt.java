package com.rameses.scripting;

import com.rameses.classutils.ClassDef;
import com.rameses.eserver.JndiUtil;
import com.rameses.annotations.After;
import com.rameses.annotations.Before;
import com.rameses.eserver.CONSTANTS;
import com.rameses.eserver.CacheServiceMBean;
import com.rameses.eserver.HtmlMap;
import com.rameses.interfaces.ResourceHandler;
import com.rameses.eserver.ResourceServiceMBean;
import com.rameses.interfaces.ScriptServiceLocal;
import groovy.lang.GroovyClassLoader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;


public class ScriptMgmt implements ScriptMgmtMBean, Serializable {
    
    
    private final static String SCRIPT_PREFIX = "script";
    private final static String INTERCEPTORS = "interceptors";
    
    private List<InterceptorDef> beforeInterceptors;
    private List<InterceptorDef> afterInterceptors;
    
    private GroovyClassLoader classLoader;
    
    private CacheServiceMBean cacheService;
    private ResourceServiceMBean resourceService;
    
    private Map<String, Class> remoteInterfaces = new Hashtable();
    
    //we have to build the interfaces on the fly
    private Map<String, Class> localInterfaces = new Hashtable();
    
    
    /**
     * this is incremented everytime the flush interceptors is called.
     */
    private int interceptorModifiedVersion;
    
    
    public void start() throws Exception {
        System.out.println("STARTING SCRIPT MANAGEMENT");
        InitialContext ctx = new InitialContext();
        JndiUtil.bind(ctx, CONSTANTS.SCRIPT_MGMT , this);
        classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        cacheService = (CacheServiceMBean)ctx.lookup(CONSTANTS.CACHE_SERVICE);
        resourceService = (ResourceServiceMBean)ctx.lookup(CONSTANTS.RESOURCE_SERVICE);
        buildInterceptors();
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING SCRIPT MANAGEMENT");
        JndiUtil.unbind(new InitialContext(), CONSTANTS.SCRIPT_MGMT);
        clearAll();
        classLoader = null;
        cacheService = null;
        resourceService = null;
    }
    
    private void clearAll() {
        Map map = cacheService.getContext(SCRIPT_PREFIX);
        cacheService.removeContext(SCRIPT_PREFIX);
        map.clear();
        
        if(beforeInterceptors!=null) beforeInterceptors.clear();
        if(afterInterceptors!=null) afterInterceptors.clear();
        beforeInterceptors = null;
        afterInterceptors = null;
        remoteInterfaces.clear();
        localInterfaces.clear();
        //we must recreate the classLoader
        classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
    }
    
    public void flushAll() {
        clearAll();
        buildInterceptors();
    }
    
    public void flushInterceptors() {
        if(beforeInterceptors!=null) beforeInterceptors.clear();
        if(afterInterceptors!=null) afterInterceptors.clear();
        beforeInterceptors = null;
        afterInterceptors = null;
        buildInterceptors();
    }
    
    public void flushScript(String name) {
        cacheService.getContext(SCRIPT_PREFIX).remove(name);
    }
    
    //build the InvokerMeta class plus all related interceptors
    public ScriptObject getScriptObject(String name) {
        try {
            ScriptObject so = null;
            Map scriptCache = cacheService.getContext(SCRIPT_PREFIX);
            if( !scriptCache.containsKey(name)) {
                InputStream is = resourceService.getResource(SCRIPT_PREFIX+"://" + name);
                Class clazz = classLoader.parseClass( is );
                
                String proxyInterface = InterfaceBuilder.getProxyInterfaceScript(name, clazz);
                Class proxyClass = null;
                if(proxyInterface!=null) {
                    proxyClass = classLoader.parseClass( proxyInterface );
                }
                
                //build also the proxy class so it can be done on one pass only.
                
                so = new ScriptObject(clazz, name, proxyInterface, proxyClass );
                scriptCache.put(name, so);
            } else {
                so = (ScriptObject) scriptCache.get(name);
            }
            return so;
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
        
    }
    
    /**
     * This loads and finds all interceptors declared in the application.
     * the modified version is needed so that the script objects interceptors
     * can be synchronized properly
     */
    private synchronized void buildInterceptors() {
        interceptorModifiedVersion++;
        System.out.println("Loading interceptors .... v"+interceptorModifiedVersion);
        if( beforeInterceptors !=null ) return;
        beforeInterceptors = new ArrayList<InterceptorDef>();
        afterInterceptors = new ArrayList<InterceptorDef>();
        try {
            InterceptorLoader interceptorLoader = new InterceptorLoader();
            resourceService.scanResources(SCRIPT_PREFIX+"://"+INTERCEPTORS, interceptorLoader);
            
        } catch(Exception e) {;}
        
        //sort the interceptors
        Collections.sort(beforeInterceptors);
        Collections.sort(afterInterceptors);
        
    }
    
    private class InterceptorLoader implements ResourceHandler {
        
        public void handle(InputStream is, String source) throws Exception {
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader r = new BufferedReader(ir);
            String s = null;
            
            while((s=r.readLine())!=null) {
                if( s.trim().length()==0 || s.trim().startsWith("#"))
                    continue;
                
                try {
                    ClassDef def = getScriptObject(s).getClassDef();
                    Method[] beforeMethods = def.findAnnotatedMethods(Before.class);
                    for(Method m: beforeMethods) {
                        Before b = (Before)m.getAnnotation(Before.class);
                        beforeInterceptors.add(new InterceptorDef(s,m,b.index(),b.pattern(),b.eval(),b.exclude()));
                    }
                    Method[] afterMethods = def.findAnnotatedMethods(After.class);
                    for(Method m: afterMethods) {
                        After a = (After)m.getAnnotation(After.class);
                        afterInterceptors.add( new InterceptorDef(s,m,a.index(),a.pattern(), a.eval(),a.exclude()) );
                    }
                } catch(Exception ign) {
                    System.out.println(ign.getMessage());
                }
            }
        }
    }
    
    
    
    
    public Object createLocalProxy(String name, Map env) {
        try {
            InitialContext ctx = new InitialContext();
            ScriptServiceLocal scriptService = (ScriptServiceLocal)ctx.lookup(CONSTANTS.SCRIPT_SERVICE_LOCAL);
            Class clazz = localInterfaces.get( name );
            if(clazz==null) {
                byte[] bytes = scriptService.getScriptInfo(name);
                clazz = classLoader.parseClass( new ByteArrayInputStream(bytes)  );
                localInterfaces.put(name, clazz);
            }
            if(clazz==null)
                throw new IllegalStateException("Interface class does not exist. Please ensure a @ProxyMethod is defined");
            
            ScriptProxyInvocationHandler handler = new ScriptProxyInvocationHandler(scriptService, name, env);
            return Proxy.newProxyInstance(classLoader, new Class[]{clazz}, handler);
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    
    public Object createRemoteProxy(String name, Map env, String hostKey ) {
        try {
            ScriptServiceLocal scriptService = RemoteDelegate.getScriptService(hostKey,env);
            Class clazz = remoteInterfaces.get(name);
            if(clazz==null) {
                byte[] bytes = scriptService.getScriptInfo(name);
                clazz = classLoader.parseClass( new ByteArrayInputStream(bytes)  );
                remoteInterfaces.put(name,clazz);
            }
            if(clazz==null)
                throw new IllegalStateException("Interface class does not exist. Please ensure a @ProxyMethod is defined");
            
            ScriptProxyInvocationHandler handler = new ScriptProxyInvocationHandler(scriptService, name, env);
            return Proxy.newProxyInstance(classLoader, new Class[]{clazz}, handler);
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public Map showLoadedScripts() {
        return new HtmlMap(cacheService.getContext(SCRIPT_PREFIX));
    }
    
    public void loadInterceptors(ScriptObject so, String serviceName, String method ) {
        //load before and after to a method
        //check first the interceptor modified version and compare if we need to reflush.
        boolean needsReload = (so.getInterceptorModifiedVersion() != interceptorModifiedVersion);
        so.setInterceptorModifiedVersion( interceptorModifiedVersion );
        
        Map _beforeInterceptors = so.getBeforeInterceptors();
        Map _afterInterceptors = so.getAfterInterceptors();
        String name = serviceName+"."+method;
        if(!_beforeInterceptors.containsKey(method) || needsReload ) {
            List<String> list = new ArrayList<String>();
            for(InterceptorDef idf: beforeInterceptors) {
                if(idf.accept(name)) {
                    list.add(idf.getSignature());
                }
            }
            _beforeInterceptors.put(method,list);
        }
        
        if(!_afterInterceptors.containsKey(method) || needsReload ) {
            List<String> list = new ArrayList<String>();
            for(InterceptorDef idf: afterInterceptors) {
                if(idf.accept(name)) {
                    list.add(idf.getSignature());
                }
            }
            _afterInterceptors.put(method,list);
        }
    }

    public int getInterceptorModifiedVersion() {
        return interceptorModifiedVersion;
    }

    
    
    
}
