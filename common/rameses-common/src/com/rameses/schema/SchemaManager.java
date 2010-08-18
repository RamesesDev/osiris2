/*
 * SchemaProviderFactory.java
 *
 * Created on August 13, 2010, 8:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.util.Map;

/**
 *
 * This class provides the handling of Schema resources
 * This class merely serves Schema objects, it will not involve caching
 * these resources.
 */
public abstract class SchemaManager {
    
    
    public SchemaManager() {
    }
    
    public abstract SchemaConf getConf();
    
    
    public Schema getSchema(String name) {
        if(getConf().getPropertyResolver()==null)
            throw new RuntimeException("Property Resolver is not set in the conf");
        
        //find the schema and check in cache
        Schema schema = getConf().getCacheProvider().getCache(name);
        if(schema !=null) return schema;
        
        for(SchemaProvider sp: getConf().getProviders()) {
            schema = sp.getSchema(name);
            if( schema!=null ) {
                getConf().getCacheProvider().putCache(name, schema);
                return schema;
            }
        }
        throw new RuntimeException("Schema " + name +  " cannot be found from provided resources");
    }
    
    public void destroy() {
        getConf().destroy();
    }
    
    private static SchemaManager instance;
    
    
    public static SchemaManager getInstance() {
        if(instance==null) {
            instance = new DefaultSchemaFactory();
        }
        return instance;
    }
    
    
    public static class DefaultSchemaFactory extends SchemaManager {
        
        private SchemaConf conf;
        
        public DefaultSchemaFactory() {
            conf = new SchemaConf();
        }
        
        public SchemaConf getConf() {
            return conf;
        }
        
        public void setConf(SchemaConf conf) {
            this.conf = conf;
        }
    }
    
    
    //helper utilities for SchemaManager.
    public SchemaScanner newScanner() {
        return new SchemaScanner(getConf().getPropertyResolver());
    }
    
    public Map createMap(String name) {
        return createMap( getSchema(name) );
    }
    
    public Map createMap(Schema schema) {
        MapBuilderHandler handler = new MapBuilderHandler();
        SchemaScanner scanner = newScanner();
        scanner.scan(schema,handler);
        return handler.getMap();
    }
    
}
