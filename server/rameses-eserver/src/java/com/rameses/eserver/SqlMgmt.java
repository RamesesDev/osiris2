/*
 * SqlMgmt.java
 *
 * Created on July 24, 2010, 8:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.schema.SchemaManager;
import com.rameses.sql.SqlUnit;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlUnitCacheProvider;
import com.rameses.sql.SqlConf;
import com.rameses.sql.SqlManager;
import java.io.Serializable;
import java.util.Map;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class SqlMgmt implements Serializable, SqlMgmtMBean {
    
    private static final String SQLMGMT_CACHE = "sqlcache";
    private CacheServiceMBean cacheService;
    private ResourceServiceMBean resourceService;
    
    private SqlManager sqlManager;

    public void start() throws Exception {
        System.out.println("STARTING SQL MGMT");
        InitialContext ctx = new InitialContext();
        //set the resources and cache
        cacheService = (CacheServiceMBean)ctx.lookup(CONSTANTS.CACHE_SERVICE);
        resourceService = (ResourceServiceMBean)ctx.lookup(CONSTANTS.RESOURCE_SERVICE);
        
        
        //There's no other way right now to inject the schema manager but to look it up.
        //we need to set this in the sql manager.
        SchemaMgmtMBean schemaMgmt = (SchemaMgmtMBean)ctx.lookup(CONSTANTS.SCHEMA_MGMT);
        sqlManager = new MgmtSqlManager();
        
        //attach the schema management in the sql extensions
        sqlManager.getConf().getExtensions().put(SchemaManager.class, schemaMgmt.getSchemaManager());
        
        JndiUtil.bind(ctx,CONSTANTS.SQLMGMT, this );
                
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING SQL MGMT");
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind(ctx,CONSTANTS.SQLMGMT );
        flushAll();
        sqlManager.destroy();
        cacheService = null;
        resourceService = null;
        sqlManager = null;
    }
    
    public SqlContext createSqlContext(String dataSource) {
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(dataSource);
            if(ds==null)
                throw new IllegalStateException("Data source " + dataSource + " is not available");
            return sqlManager.createContext(ds);
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public SqlContext createSqlContext(DataSource ds) {
        return sqlManager.createContext(ds);
    }
    
    public SqlContext createSqlContext() {
        return sqlManager.createContext();
    }
    
    public void flushAll() {
        cacheService.getContext(SQLMGMT_CACHE).clear();
    }

    
    public class MgmtSqlManager extends SqlManager implements Serializable {
        
        private SqlConf conf;
        
        public MgmtSqlManager() {
            conf = new MgmtSqlManagerConf();
            conf.setCacheProvider( new MgmtSqlUnitCache());
        }

        public SqlConf getConf() {
            return conf;
        }
    }
    
    public class MgmtSqlManagerConf extends SqlConf implements Serializable {;}
        
    
    public class MgmtSqlUnitCache implements SqlUnitCacheProvider, Serializable {
        
        public SqlUnit getCache(String key) {
            Map map = (Map)cacheService.getContext(SQLMGMT_CACHE);
            return (SqlUnit)map.get(key);
        }

        public void putCache(String key, SqlUnit sq) {
            Map map = (Map)cacheService.getContext(SQLMGMT_CACHE);
            map.put(key, sq);
        }

        public void destroy() {
            //destroy the cache map here.
            Map map = (Map)cacheService.getContext(SQLMGMT_CACHE);
            map.clear();
        }
        
    }
    
    
    
}
