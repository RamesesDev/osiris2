/*
 * SqlMgmt.java
 *
 * Created on July 24, 2010, 8:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.data.server;

import com.rameses.eserver.CONSTANTS;
import com.rameses.eserver.JndiUtil;
import com.rameses.eserver.CacheServiceMBean;
import com.rameses.eserver.ResourceServiceMBean;
import com.rameses.sql.SqlCache;
import com.rameses.sql.SqlCacheProvider;
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
            sql.setSqlCacheProvider(sqlCacheProvider);
            return sql;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public SqlManager createSqlManager(DataSource ds) {
        SqlManager sql = new SqlManager(ds);
        sql.setSqlCacheProvider(sqlCacheProvider);
        return sql;
    }
    
    public void flushAll() {
        cacheService.getContext("sqlcache").clear();
    }
    
    
    public class SqlMgmtCacheProvider extends SqlCacheProvider implements Serializable {
        
        public SqlCache getSqlCache(String statement) {
            Map map = (Map)cacheService.getContext("sqlcache");
            SqlCache sq = (SqlCache)map.get(statement);
            if(sq==null) {
                System.out.println("parsing first time " + statement);
                sq = createSqlCache(statement);
                map.put(statement, sq);
            }
            return sq;
        }
        
        //find first in the resources
        public SqlCache getNamedSqlCache(String name) {
            try {
                Map map = (Map)cacheService.getContext("sqlcache");
                SqlCache sq = (SqlCache)map.get(name);
                if(sq==null) {
                    System.out.println("parsing named sql name " + name);
                    String fileName = name;
                    if( name.indexOf(".")<0 ) fileName = fileName + ".sql";
                    
                    //find first in the resources
                    InputStream is = resourceService.getResource( "sql://" + fileName );
                    String s = getInputStreamToString(is);
                    s = formatText( fileName, s );
                    sq = createSqlCache(s);
                    map.put(name, sq);
                }
                return sq;
            } catch(Exception ex) {
                throw new IllegalStateException(ex);
            }
            
        }
        
    }
    
    
    
    
}
