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
    
    
    public Schema getSchema(String sname) {
        if(getConf().getPropertyResolver()==null)
            throw new RuntimeException("Property Resolver is not set in the conf");
        
        String name = sname;
        if(name.indexOf(":")>0) {
            name = sname.substring(0, sname.indexOf(":"));
        }

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
    
    public SchemaElement getElement(String name) {
        Schema schema = getSchema(name);
        return schema.getElement(name);
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
            conf = new SchemaConf(this);
        }
        
        public SchemaConf getConf() {
            return conf;
        }
    }
    
    
    //helper utilities for SchemaManager.
    public SchemaScanner newScanner() {
        return new SchemaScanner(getConf().getPropertyResolver());
    }
    
    public Map createMap(String name) {
        SchemaElement element = getElement(name);
        return createMap( element.getSchema(), element );
    }
    
    public Map createMap(SchemaElement element) {
        return createMap( element.getSchema(), element );
    }
    
    public Map createMap(Schema schema, SchemaElement element) {
        MapBuilderHandler handler = new MapBuilderHandler();
        SchemaScanner scanner = newScanner();
        if(element!=null)
            scanner.scan(schema,element,handler);
        else
            scanner.scan(schema,handler);
        return handler.getMap();
    }
    
    
    public ValidationResult validate(String schemaName, Object data) {
        String sname = schemaName;
        String elementName = null;
        if(schemaName.indexOf(":")>0) {
            sname = schemaName.substring(0, schemaName.indexOf(":"));
            elementName = schemaName.substring(schemaName.indexOf(":")+1);
        }    
        
        Schema schema = getSchema(sname);
        SchemaValidationHandler handler = new SchemaValidationHandler();
        SchemaScanner scanner = newScanner();
        if(elementName==null)
            scanner.scan(schema, data, handler);
        else 
            scanner.scan(schema, schema.getElement(elementName),data,handler);
        return handler.getResult();
    }
    
     public ValidationResult validate(SchemaElement element, Object data) {
        SchemaValidationHandler handler = new SchemaValidationHandler();
        SchemaScanner scanner = newScanner();
        scanner.scan(element.getSchema(),element,data,handler);
        return handler.getResult();
    }
}
