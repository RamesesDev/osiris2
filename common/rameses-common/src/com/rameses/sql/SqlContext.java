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
public class SqlContext {
    
    protected DataSource dataSource;
    private Connection currentConnection;
    private SqlManager sqlManager = SqlManager.getInstance();
    private String catalog;
    private SqlDialect dialect;
    
    /** Creates a new instance of SQLManager */
    SqlContext(DataSource ds) {
        this.dataSource = ds;
    }
    
    SqlContext() {;}
    
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }
    
    public Connection openConnection() throws Exception {
        if(dataSource==null)
            throw new RuntimeException("Datasource is null");
        currentConnection = dataSource.getConnection();
        return currentConnection;
    }
    
    public void closeConnection() {
        if(currentConnection==null) return;
        try {
            currentConnection.close();
        } catch(Exception ign){;}
        currentConnection = null;
    }
    
    
    /**
     * always create a new connection when requesting from SqlManager
     */
    public Connection getConnection() throws Exception {
        if(dataSource==null)
            throw new RuntimeException("Datasource is null");
        
        if(currentConnection!=null && currentConnection.isClosed() )
            currentConnection = null;
        
        if(currentConnection!=null) {
            return currentConnection;
        }
        return dataSource.getConnection();
    }
    
    private SqlUnit getSqlCache(String statement ) {
        return sqlManager.getParsedSqlUnit( statement );
    }
    
    private SqlUnit getNamedSqlCache(String name ) {
        if(name.indexOf(".")<0) name = name + ".sql";
        return sqlManager.getNamedSqlUnit( name );
    }
    
    public SqlQuery createQuery(String statement) {
        SqlUnit sq = getSqlCache(statement);
        SqlQuery q = new SqlQuery(this,sq.getStatement(),sq.getParamNames());
        if( dialect !=null) q.setDialect( dialect );
        if( this.catalog !=null ) q.setCatalog(catalog);
        return q;
    }
    
    public SqlQuery createNamedQuery(String name) {
        SqlUnit sq = getNamedSqlCache(name);
        SqlQuery q = new SqlQuery( this, sq.getStatement(),sq.getParamNames());
        if( dialect !=null) q.setDialect( dialect );
        if( this.catalog !=null ) q.setCatalog(catalog);
        return q;
    }
    
    public SqlExecutor createExecutor(String statement) {
        SqlUnit sq = getSqlCache(statement);
        SqlExecutor q = new SqlExecutor(this,sq.getStatement(),sq.getParamNames());
        if( this.catalog !=null ) q.setCatalog(catalog);
        return q;
    }
    
    public SqlExecutor createNamedExecutor( String name ) {
        SqlUnit sq = getNamedSqlCache(name);
        SqlExecutor q = new SqlExecutor(this,sq.getStatement(),sq.getParamNames());
        if( this.catalog !=null ) q.setCatalog(catalog);
        return q;
    }
    
    public QueryExecutor createQueryExecutor(SqlQuery q, SqlExecutor e) {
        if( this.catalog !=null ) {
            q.setCatalog(this.catalog);
            e.setCatalog(this.catalog);
        }
        return new QueryExecutor(q,e);
    }
    
    public QueryExecutor createQueryExecutor() {
        return new QueryExecutor();
    }
    
    
    public SqlManager getSqlManager() {
        return sqlManager;
    }
    
    public void setSqlManager(SqlManager sqlUnitFactory) {
        this.sqlManager = sqlUnitFactory;
    }
    
    public String getCatalog() {
        return catalog;
    }
    
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
    
    public SqlDialect getDialect() {
        return dialect;
    }
    
    public void setDialect(SqlDialect dialect) {
        this.dialect = dialect;
    }
    
    public void setDialect(String className) {
        try {
            this.dialect = (SqlDialect)getClass().getClassLoader().loadClass(  className ).newInstance();
        } catch(Exception ign) {
            //System.out.println("error in class loader dialect " + className );
        }
    }
}
