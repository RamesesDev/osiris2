/*
 * SessionCache.java
 *
 * Created on June 28, 2010, 8:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.common;

import com.sun.jmx.remote.util.Service;
import java.util.Iterator;
/**
 *
 * @author ms
 */
public abstract class CacheProvider {
    
 
    private static CacheProvider instance;
    
    public static CacheProvider getInstance() {
        return getInstance(CacheProvider.class.getClassLoader());
    }
    
    public static CacheProvider getInstance(ClassLoader classLoader) {
        if(instance==null) {
            Iterator iter  = Service.providers(CacheProvider.class, classLoader);
            if(iter.hasNext()) {
                instance = (CacheProvider)iter.next();
            }
            if(instance==null)
                throw new RuntimeException("There are no Cache Providers installed");
        }
        return instance;
    }
    
    public abstract CacheContext createContext();
    public abstract CacheContext getContext(String id);
    public abstract CacheContext removeContext(String id);
    
    public static abstract class CacheContext {
        public abstract String getId();
        public abstract Object get(Object key);
        public abstract void put(Object key, Object value);
        public abstract void remove(Object key);
    }
    
    
}
