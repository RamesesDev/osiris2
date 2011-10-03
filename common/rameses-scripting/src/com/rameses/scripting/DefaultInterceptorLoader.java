/*
 * InterceptorLoader.java
 *
 * Created on October 15, 2010, 8:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.annotations.After;
import com.rameses.annotations.Before;
import com.rameses.classutils.ClassDef;
import com.rameses.io.LineReader;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class DefaultInterceptorLoader implements InterceptorLoader {
    
    protected ScriptManager scriptManager;
    
    private List<Interceptor> beforeList = new ArrayList();
    private List<Interceptor> afterList = new ArrayList();
    
    public DefaultInterceptorLoader(ScriptManager sm) {
        this.scriptManager = sm;
    }
    
    public void load()  {
        beforeList.clear();
        afterList.clear();
        List<String> interceptorNames = new ArrayList();
        buildInterceptorList(interceptorNames);
        for( String name: interceptorNames ) {
            loadInterceptor(name);
        }
    }
    
    //overridable
    public void buildInterceptorList(final List interceptorNames) {
        try {
            String fileName = "META-INF/interceptors.conf";
            Enumeration<URL> e = ScriptManager.class.getClassLoader().getResources(fileName);
            LineReader lr = new LineReader();
            LineReader.Handler h = new LineReader.Handler() {
                public void read(String name) {
                    if( interceptorNames.indexOf(name)<0 ) interceptorNames.add(name);
                }
            };
            while(e.hasMoreElements()) {
                InputStream is = e.nextElement().openStream();
                lr.read(is, h);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void loadInterceptor(String name) {
        ScriptObjectPoolItem so = null;
        try {
            ScriptObject sm = scriptManager.getScriptObject(name);
            so = sm.getPooledObject();
            ClassDef def = so.getClassDef();
            Method[] beforeMethods = def.findAnnotatedMethods(Before.class);
            for(Method m: beforeMethods) {
                Before b = (Before)m.getAnnotation(Before.class);
                beforeList.add( new Interceptor( name,m,b.index(),b.pattern(), b.eval(),b.exclude() ));
            }
            Method[] afterMethods = def.findAnnotatedMethods(After.class);
            for(Method m: afterMethods) {
                After a = (After)m.getAnnotation(After.class);
                afterList.add( new Interceptor(name,m,a.index(),a.pattern(), a.eval(),a.exclude() ));
            }
        } catch(Exception e) {
            System.out.println("InterceptorLoader. Fail loading " + name + ":" + e.getMessage());
        }
        finally {
            try { so.close(); } catch(Exception ign){;}
        }
    }
    
    public List<Interceptor> getBeforeInterceptors() {
        return beforeList;
    }
    
    public List<Interceptor> getAfterInterceptors() {
        return afterList;
    }
    
}
