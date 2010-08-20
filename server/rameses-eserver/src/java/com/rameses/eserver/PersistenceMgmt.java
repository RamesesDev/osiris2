package com.rameses.eserver;

import com.rameses.persistence.DefaultEntityManager;
import com.rameses.persistence.EntityManager;
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


public class PersistenceMgmt implements Serializable, PersistenceMgmtMBean {
    
    
    private CacheServiceMBean cacheService;
    private ResourceServiceMBean resourceService;
    
    private SqlManager sqlManager;
    private SchemaMgmtMBean schemaMgmt;

    public void start() throws Exception {
        System.out.println("STARTING PERSISTENCE MGMT");
        InitialContext ctx = new InitialContext();
        //set the resources and cache
        cacheService = (CacheServiceMBean)ctx.lookup(CONSTANTS.CACHE_SERVICE);
        resourceService = (ResourceServiceMBean)ctx.lookup(CONSTANTS.RESOURCE_SERVICE);
        
        
        //There's no other way right now to inject the schema manager but to look it up.
        //we need to set this in the sql manager.
        schemaMgmt = (SchemaMgmtMBean)ctx.lookup(CONSTANTS.SCHEMA_MGMT);
        sqlManager = new MgmtSqlManager();
        
        //attach the schema management in the sql extensions
        sqlManager.getConf().getExtensions().put(SchemaManager.class, schemaMgmt.getSchemaManager());
        
        JndiUtil.bind(ctx,CONSTANTS.PERSISTENCE_MGMT, this );
                
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING PERSISTENCE MGMT");
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind(ctx,CONSTANTS.PERSISTENCE_MGMT );
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
        cacheService.getContext(CONSTANTS.SQLMGMT_CACHE).clear();
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
            Map map = (Map)cacheService.getContext(CONSTANTS.SQLMGMT_CACHE);
            return (SqlUnit)map.get(key);
        }

        public void putCache(String key, SqlUnit sq) {
            Map map = (Map)cacheService.getContext(CONSTANTS.SQLMGMT_CACHE);
            map.put(key, sq);
        }

        public void destroy() {
            //destroy the cache map here.
            Map map = (Map)cacheService.getContext(CONSTANTS.SQLMGMT_CACHE);
            map.clear();
        }
    }
    
    
    //PERSISTENCE CONTEXT
    public EntityManager createPersistenceContext(String datasource) {
        SqlContext sqlContext = null;
        if( datasource!=null && datasource.trim().length()>0)
            sqlContext = createSqlContext(datasource);
        else
            sqlContext = createSqlContext();
        return new DefaultEntityManager(schemaMgmt.getSchemaManager(),sqlContext);
    }

    
    
    
}
