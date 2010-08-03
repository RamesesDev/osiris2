/*
 * SqlCacheProvider.java
 *
 * Created on August 3, 2010, 2:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

/**
 *
 * @author elmo
 */
public abstract class SqlCacheProvider {
    
    private SqlCacheResourceHandler cacheResourceHandler;
    
    public void setSqlCacheResourceHandler(SqlCacheResourceHandler cacheResource) {
        this.cacheResourceHandler = cacheResource;
    }

    public SqlCacheResourceHandler getSqlCacheResourceHandler() {
        return this.cacheResourceHandler;
    }
    
    public abstract boolean accept( String name );
    public abstract SqlCache createSqlCache(String name);
    public abstract void flush( String name );
    
}
