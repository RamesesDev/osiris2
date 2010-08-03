/*
 * SQLManager.java
 *
 * Created on July 21, 2010, 8:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import com.sun.jmx.remote.util.Service;
import java.sql.Connection;
import java.util.Iterator;
import javax.sql.DataSource;

/**
 * The sql manager ideally is unique per datasource.
 * however this behavior can be changed
 */
public class SqlManager {
    
    protected DataSource dataSource;
    
    private SqlCacheResourceHandler sqlCacheResourceHandler = new DefaultSqlCacheResourceHandler();
    
    /** Creates a new instance of SQLManager */
    public SqlManager(DataSource ds) {
        this.dataSource = ds;
    }
    
    public SqlManager() {;}

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }
    
    /**
     * always create a new connection when requesting from SqlManager
     */
    public Connection getConnection() throws Exception {
        if(dataSource==null)
            throw new IllegalStateException("Datasource is null");
        return dataSource.getConnection();
    }
    
    private SqlCache getSqlCache(String statement ) {
        SqlCache sq = sqlCacheResourceHandler.getCache( statement );
        if(sq==null) {
            sq = new SqlCache(statement);
            sqlCacheResourceHandler.storeCache( statement, sq );
        }
        return sq;
    }
    
    private SqlCache getNamedSqlCache(String name ) {
        if(name.indexOf(".")<0) name = name + ".sql";
        SqlCache sq = sqlCacheResourceHandler.getCache( name );
        if( sq == null ) {
            Iterator iter = Service.providers( SqlCacheProvider.class, Thread.currentThread().getContextClassLoader() );
            while(iter.hasNext()) {
                SqlCacheProvider sp = (SqlCacheProvider)iter.next();
                if( sp.accept( name )) {
                    sp.setSqlCacheResourceHandler( sqlCacheResourceHandler );
                    sq = sp.createSqlCache(name);
                    sqlCacheResourceHandler.storeCache(name, sq);
                }
            }
        }
        if(sq ==null)
            throw new IllegalStateException("Sql Cache " + name + " is not found!");
        return sq;
    }
    
    
    
    public SqlQuery createQuery(String statement) {
        if(sqlCacheResourceHandler==null)
            throw new IllegalStateException("SqlCacheProvider must be provided");
        
        SqlCache sq = getSqlCache(statement);
        return new SqlQuery(this,sq.getStatement(),sq.getParamNames());
    }
    
    public SqlQuery createNamedQuery(String name) {
        if(sqlCacheResourceHandler==null)
            throw new IllegalStateException("SqlCacheProvider must be provided");
        SqlCache sq = getNamedSqlCache(name);
        return new SqlQuery( this, sq.getStatement(),sq.getParamNames());
    }

    public SqlExecutor createExecutor(String statement) {
        if(sqlCacheResourceHandler==null)
            throw new IllegalStateException("SqlCacheProvider must be provided");
        SqlCache sq = getSqlCache(statement);
        return new SqlExecutor(this,sq.getStatement(),sq.getParamNames());
    }

    public SqlExecutor createNamedExecutor( String name ) {
        if(sqlCacheResourceHandler==null)
            throw new IllegalStateException("SqlCacheProvider must be provided");
        SqlCache sq = getNamedSqlCache(name);
        return new SqlExecutor(this,sq.getStatement(),sq.getParamNames());    
    }
    
    public QueryExecutor createQueryExecutor(SqlQuery q, SqlExecutor e) {
        return new QueryExecutor(q,e);
    }

    public QueryExecutor createQueryExecutor() {
        return new QueryExecutor();
    }
    
    public void setSqlCacheResourceHandler(SqlCacheResourceHandler sq) {
        this.sqlCacheResourceHandler = sq;
    }
    
}
