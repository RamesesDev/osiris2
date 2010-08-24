/*
 * DefaultSchemaResourceProvider.java
 *
 * Created on August 13, 2010, 10:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class DefaultSchemaCacheProvider implements SchemaCacheProvider  {

    private Map<String,Schema> cache = new Hashtable();
    
    public void putCache(String key, Schema schema) {
        cache.put(key,schema);
    }

    public Schema getCache(String key) {
        return cache.get(key);
    }

    public void destroy() {
        cache.clear();
        cache = null;
    }
    
    
    
    
}
