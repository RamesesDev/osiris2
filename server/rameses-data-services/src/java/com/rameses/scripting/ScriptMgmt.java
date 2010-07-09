package com.rameses.scripting;

import com.rameses.classutils.ClassDef;
import com.rameses.interfaces.ResourceProvider;
import com.rameses.jndi.JndiUtil;
import com.rameses.scripting.annotations.After;
import com.rameses.scripting.annotations.Before;
import groovy.lang.GroovyClassLoader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;


public class ScriptMgmt implements ScriptMgmtMBean, Serializable {
    
    public final static String JNDI_NAME = "ScriptMgmt";
    private Hashtable<String,InvokerMeta> invokers = new Hashtable<String,InvokerMeta>();
    
    //this caches the interfaces created
    private Map<String, byte[]> scriptInterfaceBytes = new Hashtable<String, byte[]>();
    private Map<String, Class> scriptInterfaceClasses = new Hashtable<String, Class>();
    
    private GroovyClassLoader groovyLoader;
    
    private ResourceProvider scriptProvider;
    private ResourceProvider confProvider;
    
    private List<InterceptorDef> beforeInterceptors;
    private List<InterceptorDef> afterInterceptors;
    private Map<String,List<String>> beforeInterceptorsMap = new Hashtable<String,List<String>>();
    private Map<String,List<String>> afterInterceptorsMap = new Hashtable<String,List<String>>();
    
    public void start() throws Exception {
        System.out.println("START SCRIPT MANAGEMENT");
        
        JndiUtil.bind(new InitialContext(), JNDI_NAME , this);
        
        groovyLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        //initialize scriptService immediately
        Iterator iter = com.sun.jmx.remote.util.Service.providers(ResourceProvider.class, Thread.currentThread().getContextClassLoader());
        //load only the first
        while(iter.hasNext()) {
            ResourceProvider rp = (ResourceProvider)iter.next();
            if( rp.getNamespace().equals(ResourceProvider.SERVICE)) {
                scriptProvider = rp;
            }
            else if(rp.getNamespace().equals(ResourceProvider.CONF)) {
                confProvider = rp;
            }
        }
        flushAll();
    }
    
    public void stop() throws Exception {
        JndiUtil.unbind(new InitialContext(), JNDI_NAME);
        groovyLoader = null;
        scriptProvider = null;
        confProvider = null;
        clearAll();
    }
    
    private void clearAll() {
        invokers.clear();
        scriptInterfaceBytes.clear();
        scriptInterfaceClasses.clear();
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
        invokers.remove(name);
        scriptInterfaceBytes.remove(name);
        scriptInterfaceClasses.remove(name);
    }
    
    
    //build the InvokerMeta class plus all related interceptors
    public InvokerMeta getInvokerMeta(String name) {
        if( !invokers.containsKey(name)) {
            Class clazz = getScriptInfo(name);
            InvokerMeta im = new InvokerMeta(clazz, name );
            invokers.put(name, im);
        }
        return invokers.get(name);
    }
    
    //upon requesting the remote interface class, store it in the map
    public byte[] getScriptIntfBytes(String name) {
        if( !scriptInterfaceBytes.containsKey(name)) {
            Class targetClass = getScriptInfo( name );
            String intf = InterfaceBuilder.getProxyInterfaceScript(name, targetClass);
            byte[] b = intf.getBytes();
            scriptInterfaceBytes.put(name, b);
            //also create a parsed class to be ready
            scriptInterfaceClasses.put(name, groovyLoader.parseClass( new ByteArrayInputStream(b)));
        }
        return scriptInterfaceBytes.get(name);
    }
    
    //get info text first before
    public Class getScriptIntfClass(String name) {
        if(! scriptInterfaceClasses.containsKey(name) ) {
            getScriptIntfBytes(name);
        }
        return scriptInterfaceClasses.get(name);
    }
    
    public Class getScriptInfo(String name ) {
        InputStream is = null;
        try {
            if(scriptProvider!=null) {
                is = scriptProvider.getResource(name);
            }
            
            //try another source as fallback
            if(is==null) {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/services/" +name );
            }
            
            if(is==null) {
                throw new Exception("Script " + name + " is not found!");
            }
            return groovyLoader.parseClass( is );
            
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try {is.close();} catch(Exception ign){;}
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
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if( confProvider!=null ) {
                try {
                    InputStream is = confProvider.getResource("interceptors");
                    if(is!=null) loadInterceptors( is );
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            Enumeration en = loader.getResources("META-INF/interceptors.conf");
            while(en.hasMoreElements()) {
                URL u = (URL)en.nextElement();
                loadInterceptors( u.openStream() );
            }
        } catch(Exception e) {;}
        
        //sort the interceptors
        Collections.sort(beforeInterceptors);
        Collections.sort(afterInterceptors);
    }
    
    private void loadInterceptors( InputStream is) throws Exception{
        InputStreamReader ir = new InputStreamReader(is);
        BufferedReader r = new BufferedReader(ir);
        String s = null;

        while((s=r.readLine())!=null) {
            if( s.trim().length()==0 || s.trim().startsWith("#"))
                continue;
            
            try {
                Class clazz = getScriptInfo(s);
                ClassDef def = new ClassDef(clazz);
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
            } 
            catch(Exception ign) {
                System.out.println(ign.getMessage());
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
