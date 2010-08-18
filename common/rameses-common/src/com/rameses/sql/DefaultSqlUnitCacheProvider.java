package com.rameses.sql;

import java.util.Hashtable;
import java.util.Map;


public class DefaultSqlUnitCacheProvider implements SqlUnitCacheProvider {
    
    private Map<String,SqlUnit> cache = new Hashtable();
    
    public SqlUnit getCache(String name) {
        if( !cache.containsKey(name) )
            return null;
        else {
            return cache.get(name);
        }
    }
    
    public void putCache(String name, SqlUnit su) {
        cache.put( name, su);
    }

    public void destroy() {
        cache.clear();
        cache = null;
    }
    
    
}