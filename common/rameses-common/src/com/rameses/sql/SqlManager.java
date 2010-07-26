/*
 * SQLManager.java
 *
 * Created on July 21, 2010, 8:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.sql.Connection;
import javax.sql.DataSource;

/**
 * The sql manager ideally is unique per datasource.
 * however this behavior can be changed
 */
public class SqlManager {
    
    protected DataSource dataSource;
    private SqlCacheProvider sqlCacheProvider = new BasicSqlCacheProvider();
    
    /** Creates a new instance of SQLManager */
    public SqlManager(DataSource ds) {
        this.dataSource = ds;
    }
    
    public SqlManager() {
        
    }

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }
    
    private Connection txnconn;
    
    public Connection getConnection() throws Exception {
        if(dataSource==null)
            throw new IllegalStateException("Datasource is null");

        if(txnconn==null) txnconn = dataSource.getConnection();
        if(txnconn.isClosed()) txnconn = dataSource.getConnection();
        return txnconn;
    }
    
    
    public SqlQuery createQuery(String statement) {
        if(sqlCacheProvider==null)
            throw new IllegalStateException("SqlCacheProvider must be provided");
        SqlCache sq = sqlCacheProvider.getSqlCache(statement);
        return new SqlQuery(this,sq.getStatement(),sq.getParamNames());
    }
    
    public SqlQuery createNamedQuery(String name) {
        if(sqlCacheProvider==null)
            throw new IllegalStateException("SqlCacheProvider must be provided");
        SqlCache sq = sqlCacheProvider.getNamedSqlCache(name);
        return new SqlQuery( this, sq.getStatement(),sq.getParamNames());
    }

    public SqlExecutor createExecutor(String statement) {
        if(sqlCacheProvider==null)
            throw new IllegalStateException("SqlCacheProvider must be provided");
        SqlCache sq = sqlCacheProvider.getSqlCache(statement);
        return new SqlExecutor(this,sq.getStatement(),sq.getParamNames());
    }

    public SqlExecutor createNamedExecutor( String name ) {
        if(sqlCacheProvider==null)
            throw new IllegalStateException("SqlCacheProvider must be provided");
        SqlCache sq = sqlCacheProvider.getNamedSqlCache(name);
        return new SqlExecutor(this,sq.getStatement(),sq.getParamNames());    
    }
    
    public QueryExecutor createQueryExecutor(SqlQuery q, SqlExecutor e) {
        return new QueryExecutor(q,e);
    }

    public QueryExecutor createQueryExecutor() {
        return new QueryExecutor();
    }
    
    public void setSqlCacheProvider(SqlCacheProvider sq) {
        this.sqlCacheProvider = sq;
    }
    
}
