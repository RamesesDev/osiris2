/*
 * SqlUnitFactory.java
 *
 * Created on August 13, 2010, 2:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.sql.DataSource;

/**
 * for caching Sql Units, this class must be extended.
 * 
 */
public class SqlManager {
    
    //=== static area ===
    private static SqlManager instance;
    
    public static void setInstance(SqlManager sm) {
        instance = sm;
    }
    
    public static SqlManager getInstance() {
        if(instance==null) {
            instance = new SqlManager(new SqlConf());
        }
        return instance;
    }
    
    
    //=== instance area ===
    private SqlConf conf = new SqlConf();
    private SqlDialect dialect;
     
    public SqlConf getConf() {
        return conf;
    }
    
    private Map<String,SqlUnit> cache = Collections.synchronizedMap(new Hashtable());
    
    public SqlManager(SqlConf conf) {
        this.conf = conf;
        this.conf.load();
    }
    
    public SqlUnit getParsedSqlUnit(String statement) {
        String key = statement.hashCode()+"";
        SqlUnit su = cache.get(key);
        if(su==null) {
            su = new SqlUnit(statement);
            cache.put(key, su);
        }
        return su;    
    }
    
    public SqlUnit getNamedSqlUnit(String name) {
        int extIndex = name.lastIndexOf(".");
        String unitName = name;
        //type is represnted in the extension part.
        String type = name.substring( extIndex+1 );
        
        SqlUnit su = cache.get(unitName);
        if( su!=null) return su;
        
        Map<String, SqlUnitProvider> providers = getConf().getSqlUnitProviders();
        //extension represents the type of Sql unit.
        if(!providers.containsKey(type)) {
            throw new RuntimeException("Sql unit factory error. There is no Sql Unit provider for type " + type );
        }

        su = providers.get(type).getSqlUnit(unitName);
        if(su==null) {
            throw new RuntimeException("Sql unit " + name + " is not found");
        }
        
        cache.put(name, su);
        
        return su;
    }
    
    public void destroy() {
        getConf().destroy();
    }

    
    public SqlContext createContext() {
        SqlContext sm = new SqlContext();
        sm.setSqlManager(this);
        return sm;
    }
    
    public SqlContext createContext(DataSource ds) {
        SqlContext sm = new SqlContext(ds);
        sm.setSqlManager(this);
        return sm;
    }

    public Map<String, SqlUnit> getCache() {
        return cache;
    }

    public SqlDialect getDialect() {
        return dialect;
    }

    public void setDialect(SqlDialect dialect) {
        if( this.dialect == dialect ) return;
        
        this.dialect = dialect;
        Iterator itr = conf.getSqlUnitProviders().values().iterator();
        while( itr.hasNext() ) {
            ((SqlUnitProvider) itr.next()).setDialect(dialect);
        }
    }
}
