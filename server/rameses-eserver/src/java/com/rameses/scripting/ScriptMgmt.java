package com.rameses.scripting;

import com.rameses.classutils.ClassDef;
import com.rameses.jndi.JndiUtil;
import com.rameses.annotations.After;
import com.rameses.annotations.Before;
import com.rameses.resource.CacheServiceMBean;
import com.rameses.resource.MultiResourceHandler;
import com.rameses.resource.ResourceServiceMBean;
import groovy.lang.GroovyClassLoader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;


public class ScriptMgmt implements ScriptMgmtMBean, Serializable {
    
    public final static String JNDI_NAME = "ScriptMgmt";
    private final static String SCRIPT_PREFIX = "script";
    private final static String INTERCEPTORS = "interceptors";
  
    private List<InterceptorDef> beforeInterceptors;
    private List<InterceptorDef> afterInterceptors;
    private Map<String,List<String>> beforeInterceptorsMap = new Hashtable<String,List<String>>();
    private Map<String,List<String>> afterInterceptorsMap = new Hashtable<String,List<String>>();
    
    private GroovyClassLoader classLoader;
    
    private CacheServiceMBean cacheService;
    private ResourceServiceMBean resourceService;
    
    public void start() throws Exception {
        System.out.println("STARTING SCRIPT MANAGEMENT");
        InitialContext ctx = new InitialContext();
        JndiUtil.bind(ctx, JNDI_NAME , this);
        classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        cacheService = (CacheServiceMBean)ctx.lookup("CacheService");
        resourceService = (ResourceServiceMBean)ctx.lookup("ResourceService");
        buildInterceptors();
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING SCRIPT MANAGEMENT");
        JndiUtil.unbind(new InitialContext(), JNDI_NAME);
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
        beforeInterceptorsMap.clear();
        afterInterceptorsMap.clear();
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
        beforeInterceptorsMap.clear();
        afterInterceptorsMap.clear();
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
                so = new ScriptObject(clazz, name );
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
     * steps:
     *  1. create a groovy script file under META-INF
     *  2. register the path of file in the interceptors.conf file similar to META-INF/services
     */
    private synchronized void buildInterceptors() {
        if( beforeInterceptors !=null ) return;
        beforeInterceptors = new ArrayList<InterceptorDef>();
        afterInterceptors = new ArrayList<InterceptorDef>();
        try {
            System.out.println("LOADING INTERCEPTORS NOW...");
            InterceptorLoader interceptorLoader = new InterceptorLoader();
            resourceService.scanResources(SCRIPT_PREFIX+"://"+INTERCEPTORS, interceptorLoader);
            
        } catch(Exception e) {;}
        
        //sort the interceptors
        Collections.sort(beforeInterceptors);
        Collections.sort(afterInterceptors);
    }
    
    private class InterceptorLoader implements MultiResourceHandler {
        
        public void handle(InputStream is) throws Exception {
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
                        beforeInterceptors.add(new InterceptorDef(s,m,b.index(),b.pattern(),b.eval()));
                    }
                    Method[] afterMethods = def.findAnnotatedMethods(After.class);
                    for(Method m: afterMethods) {
                        After a = (After)m.getAnnotation(After.class);
                        afterInterceptors.add( new InterceptorDef(s,m,a.index(),a.pattern(), a.eval()) );
                    }
                } catch(Exception ign) {
                    System.out.println(ign.getMessage());
                }
            }
        }
    }
    
    
    public List<String> findBeforeInterceptors(String name) {
        if( !beforeInterceptorsMap.containsKey(name) ) {
            synchronized(beforeInterceptorsMap) {
                List<String> list = new ArrayList<String>();
                for(InterceptorDef idf: beforeInterceptors) {
                    if(idf.accept(name)) {
                        list.add(idf.getSignature());
                    }
                }
                beforeInterceptorsMap.put(name,list);
            }
        }
        return beforeInterceptorsMap.get(name);
    }
    
    public List<String> findAfterInterceptors(String name) {
        if( !afterInterceptorsMap.containsKey(name) ) {
            synchronized(afterInterceptorsMap) {
                List<String> list = new ArrayList<String>();
                for(InterceptorDef idf: afterInterceptors) {
                    if(idf.accept(name)) {
                        list.add(idf.getSignature());
                    }
                }
                afterInterceptorsMap.put(name,list);
            }
        }
        return afterInterceptorsMap.get(name);
    }
    
    
}
