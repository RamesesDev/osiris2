/*
 * SqlUnitFactory.java
 *
 * Created on August 13, 2010, 2:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.util.Map;
import javax.sql.DataSource;

/**
 * for caching Sql Units, this class must be extended.
 * 
 */
public abstract class SqlManager {
    
    public abstract SqlConf getConf();
    
    public SqlUnit getParsedSqlUnit(String statement) {
        String key = statement.hashCode()+"";
        SqlUnit su = getConf().getCacheProvider().getCache(key);
        if(su==null) {
            su = new SqlUnit(statement);
            getConf().getCacheProvider().putCache(key, su);
        }
        return su;    
    }
    
    public SqlUnit getNamedSqlUnit(String name) {
        int extIndex = name.lastIndexOf(".");
        String unitName = name; 
        //type is represnted in the extension part.
        String type = name.substring( extIndex+1);

        Map<String, SqlUnitProvider> providers = getConf().getSqlUnitProviders();


        SqlUnit su = getConf().getCacheProvider().getCache(unitName);
        if( su!=null) return su;
        
        //extension represents the type of Sql unit.
        if(!providers.containsKey(type))
            throw new RuntimeException("Sql unit factory error. There is no Sql Unit provider for type " + type );
            
        su = providers.get(type).getSqlUnit(unitName);
        if(su==null)
            throw new RuntimeException("Sql unit " + name + " is not found");
        getConf().getCacheProvider().putCache(name, su);
        return su;
    }
    
    
    private static SqlManager instance;
    
    public static SqlManager getInstance() {
        if(instance==null) {
            instance = new DefaultSqlManager(new SqlConf());
        }
        return instance;
    }
    
    public static class DefaultSqlManager extends SqlManager {
        
        private SqlConf conf = new SqlConf();
        
        public DefaultSqlManager(SqlConf c) {
            conf = c;
        }
        public SqlConf getConf() {
            return conf;
        }
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
}
