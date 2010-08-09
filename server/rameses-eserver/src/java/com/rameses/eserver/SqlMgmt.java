/*
 * SqlMgmt.java
 *
 * Created on July 24, 2010, 8:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.eserver.ResourceServiceMBean;
import com.rameses.sql.SqlCache;
import com.rameses.sql.SqlCacheResourceHandler;
import com.rameses.sql.SqlManager;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class SqlMgmt implements Serializable, SqlMgmtMBean {
    
    private CacheServiceMBean cacheService;
    private ResourceServiceMBean resourceService;
    private SqlMgmtCacheProvider sqlCacheProvider = new SqlMgmtCacheProvider();
    
    public void start() throws Exception {
        System.out.println("STARTING SQL MGMT");
        InitialContext ctx = new InitialContext();
        JndiUtil.bind(ctx,CONSTANTS.SQLMGMT, this );
        
        //set the resources and cache
        cacheService = (CacheServiceMBean)ctx.lookup(CONSTANTS.CACHE_SERVICE);
        resourceService = (ResourceServiceMBean)ctx.lookup(CONSTANTS.RESOURCE_SERVICE);
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING SQL MGMT");
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind(ctx,CONSTANTS.SQLMGMT );
        flushAll();
        cacheService = null;
        resourceService = null;
    }
    
    public SqlManager createSqlManager(String dataSource) {
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(dataSource);
            SqlManager sql = new SqlManager(ds);
            sql.setSqlCacheResourceHandler(sqlCacheProvider);
            return sql;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public SqlManager createSqlManager(DataSource ds) {
        SqlManager sql = new SqlManager(ds);
        sql.setSqlCacheResourceHandler(sqlCacheProvider);
        return sql;
    }
    
    public SqlManager createSqlManager() {
        SqlManager sql = new SqlManager();
        sql.setSqlCacheResourceHandler(sqlCacheProvider);
        return sql;
    }
    
    public void flushAll() {
        cacheService.getContext("sqlcache").clear();
    }
    
    
    public class SqlMgmtCacheProvider implements SqlCacheResourceHandler,Serializable {
        
        public InputStream getResource(String name) {
            try {
                return resourceService.getResource( "sql://" + name );
            } catch(Exception e) {
                System.out.println("SqlMgmtCacheProvider error." + e.getMessage());
                return null;
            }
        }
        
        public void storeCache(String key, SqlCache sq) {
            Map map = (Map)cacheService.getContext("sqlcache");
            map.put(key, sq);
        }
        
        public SqlCache getCache(String key) {
            Map map = (Map)cacheService.getContext("sqlcache");
            return (SqlCache)map.get(key);
        }
        
        
    }
    
    
    
    
}
