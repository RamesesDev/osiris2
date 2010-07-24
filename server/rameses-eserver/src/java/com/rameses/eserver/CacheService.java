package com.rameses.eserver;

import com.sun.jmx.remote.util.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;

/**
 * CacheService is intended for storing key value pairs in memory.
 * This can be configured by adding CacheContextProvider.
 * CacheContextProvider must provide a Map implementation.
 * For example if you want to store values in Memcached,
 * Use a Map implementation that connects to Memcached when 
 * getting and setting values. 
 * 
 * example code:
 *   public class Memcache implements Map {
 *      private InitialContext ctx = new InitialContext();
 *
 *      public Object put(Object key, Object value) {
 *          MemcacheServiceLocal m = (MemcacheServiceLocal)ctx.lookup("MemcacheService/local");
 *          return m.put( key, value );     
 *      }
 *      public Object get(Object key) {
 *          MemcacheServiceLocal m = (MemcacheServiceLocal)ctx.lookup("MemcacheService/local");
 *          return m.get( key );     
 *      }
 *      ...
 *   } 
 *
 */

public class CacheService implements CacheServiceMBean, Serializable  {
    
    
    
    private Map<String, Map> map;
    
    public Map getContext(String key) {
        if(!map.containsKey(key)) {
            synchronized(map) {
                Map ctx = new Hashtable();
                map.put(key,ctx);
            }
        }
        return map.get(key);
    }
    
    public void start() throws Exception {
        JndiUtil.bind(new InitialContext(),CONSTANTS.CACHE_SERVICE, this);
        System.out.println("STARTING CACHE SERVICE");
        map = new Hashtable<String, Map>();
        
        //load the cache context providers
        Iterator<CacheContextProvider> iter = Service.providers(CacheContextProvider.class, Thread.currentThread().getContextClassLoader());
        List<CacheContextProvider> list = new ArrayList();
        while(iter.hasNext()) {
            list.add( iter.next());
        }
        
        Collections.sort(list);
        for(CacheContextProvider cp : list) {
            String name = cp.getName();
            if(!map.containsKey(name)) {
                System.out.println(" Loading Cache Provider ... " + cp.getDescription());
                map.put( name, cp.getCacheMap() );
            }
        }
        
    }
    
    public void stop() throws Exception {
        map.clear();
        System.out.println("STOPPING CACHE SERVICE");
        map = null;
        JndiUtil.unbind(new InitialContext(),CONSTANTS.CACHE_SERVICE);
    }
    
    public void removeContext(String namespace) {
        map.remove(namespace);
    }
    
    public void removeAll() {
        map.clear();
    }
    
    
}
