/*
 * SchemaMgmt.java
 *
 * Created on August 12, 2010, 2:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.common.ExpressionResolver;
import com.rameses.schema.Schema;
import com.rameses.common.PropertyResolver;
import com.rameses.schema.SchemaCacheProvider;
import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaSerializer;
import com.rameses.schema.ValidationResult;
import com.rameses.util.MapSerializer;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.Serializable;
import java.util.Map;
import javax.naming.InitialContext;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author elmo
 */
public class SchemaMgmt implements SchemaMgmtMBean, Serializable {
    
    private static final String SCHEMA_CACHE = "schemacache";
    private String jndiName = CONSTANTS.SCHEMA_MGMT;
    
    private CacheServiceMBean cacheService;
    private ResourceServiceMBean resourceService;
    private SchemaManager schemaManager;
    
    public SchemaMgmt() {
    }
    
    public void start() throws Exception {
        System.out.println("STARTING SCHEMA MGMT");
        InitialContext ctx = new InitialContext();
        cacheService = (CacheServiceMBean)ctx.lookup(CONSTANTS.CACHE_SERVICE);
        resourceService = (ResourceServiceMBean)ctx.lookup(CONSTANTS.RESOURCE_SERVICE);
        schemaManager = new MgmtSchemaManager();
        JndiUtil.bind( ctx, jndiName, this);
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING SCHEMA MGMT");
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx, jndiName);
        schemaManager.destroy();
        cacheService = null;
        resourceService = null;
        schemaManager = null;
    }
    
    public Schema getSchema(String name) {
        return schemaManager.getSchema(name);
    }
    
    public void flushAll() {
        cacheService.getContext(SCHEMA_CACHE).clear();
    }
    
    public void flush(String name) {
        cacheService.getContext(SCHEMA_CACHE).remove(name);
    }
    
    public void validate(String schemaName, Object value) throws Exception {
        ValidationResult vr = schemaManager.validate(schemaName, value);
        if(vr.hasErrors()) {
            throw new Exception(vr.toString());
        }
    }
    
    public SchemaManager getSchemaManager() {
        return schemaManager;
    }
    
    public class MgmtSchemaManager extends SchemaManager implements Serializable {
        
        private SchemaConf conf;
        
        public MgmtSchemaManager() {
            conf = new MgmtSchemaConf(this);
            conf.setCacheProvider( new SchemaMgmtCache());
            conf.setPropertyResolver( new BeanUtilPropertyResolver() );
            conf.setSerializer(new SchemaMgmtSerializer());
            conf.setExpressionResolver(new SchemaMgmtExpressionResolver() );
        }
        
        public SchemaConf getConf() {
            return conf;
        }
    }
    
    public class MgmtSchemaConf extends SchemaConf implements Serializable {
        MgmtSchemaConf(SchemaManager sm) {
            super(sm);
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="CACHE PROVIDER">
    public class SchemaMgmtCache implements SchemaCacheProvider, Serializable {
        
        public Schema getCache(String key) {
            Map map = (Map)cacheService.getContext(SCHEMA_CACHE);
            return (Schema)map.get(key);
        }
        
        public void putCache(String key, Schema schema) {
            Map map = (Map)cacheService.getContext(SCHEMA_CACHE);
            map.put(key, schema);
        }
        
        public void destroy() {
            //destroy the cache map here.
            Map map = (Map)cacheService.getContext(SCHEMA_CACHE);
            map.clear();
        }
        
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="SERIALIZER">
    public class SchemaMgmtSerializer implements SchemaSerializer, Serializable {
        
        private MapSerializer serializer = new MapSerializer();
        
        public Object read(String s) {
            GroovyShell shell = null;
            try {
                shell = new GroovyShell();
                return shell.evaluate( s );
            } catch(Exception e) {
                throw new RuntimeException(e);
            } finally {
                shell = null;
            }
        }
        
        public String write(Object o) {
            if(!(o instanceof Map))
                throw new RuntimeException("SchemaMgmt error. write serialization must accept a Map object");
            return serializer.toString( (Map)o );
        }
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="PROPERTY RESOLVER">
    public class BeanUtilPropertyResolver implements PropertyResolver {
        
        public void setProperty(Object bean, String propertyName, Object value) {
            try {
                PropertyUtils.setNestedProperty( bean, propertyName, value );
            } catch(Exception e){;}
        }
        
        public Class getPropertyType(Object bean, String propertyName) {
            try {
                Object o = PropertyUtils.getNestedProperty( bean, propertyName );
                if(o!=null) return o.getClass();
                return PropertyUtils.getPropertyType(bean, propertyName);
            } catch(Exception e) {
                //System.out.println("error " + e.getMessage());
                return null;
            }
        }
        
        public Object getProperty(Object bean, String propertyName) {
            try {
                return PropertyUtils.getNestedProperty( bean, propertyName );
            } catch(Exception e) {
                return null;
            }
        }
    }
//</editor-fold>
    
    public class SchemaMgmtExpressionResolver implements ExpressionResolver {
        
        public Object evaluate(String expression, Map vars) {
            GroovyShell shell = null;
            try {
                Binding b = new Binding(vars);
                shell = new GroovyShell(b);
                return shell.evaluate( expression );
            } catch(Exception e) {
                throw new RuntimeException(e);
            } finally {
                shell = null;
            }
        }
        
    }
    
}
