/*
 * MyConnection.java
 * Created on December 21, 2011, 9:17 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class DBServiceConnection implements Connection {
    
    private DBService scriptService;
    private DBServiceDatabaseMetaData dbmeta;
    private Map conf;
    
    public DBServiceConnection(Map conf,String serviceName) {
        this.conf = conf;
        this.scriptService = new DBServiceImpl(conf,serviceName);
    }

    public DBServiceConnection(Map conf) {
        this.conf = conf;
        this.scriptService = new DBServiceImpl(conf);
    }

    public DBServiceConnection(String host,String appContext) {
        this(host,appContext,null);
    }
    public DBServiceConnection(String host,String appContext,String serviceName) {
        this.conf = new HashMap();
        this.conf.put("app.host", host);
        this.conf.put("app.context", appContext);
        if(serviceName!=null)
            this.scriptService = new DBServiceImpl(this.conf,serviceName);
        else
            this.scriptService = new DBServiceImpl(this.conf);
    }
    
    
    public Statement createStatement() throws SQLException {
        return new DBServiceStatement(null,scriptService,this);
    }
    
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new DBServiceStatement(sql, scriptService, this );
    }
    
    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public String nativeSQL(String sql) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void setAutoCommit(boolean autoCommit) throws SQLException {}
    
    public boolean getAutoCommit() throws SQLException {
        return true;
    }
    
    public void commit() throws SQLException {
    }
    
    public void rollback() throws SQLException {
    }
    
    public void close() throws SQLException {
    }
    
    public boolean isClosed() throws SQLException {
        return true;
    }
    
    public DatabaseMetaData getMetaData() throws SQLException {
        if(dbmeta==null) {
            dbmeta = new DBServiceDatabaseMetaData(this, this.conf);
        }
        return dbmeta;
    }
    
    public void setReadOnly(boolean readOnly) throws SQLException {
        throw new SQLException("Method not supported. setReadOnly");
    }
    
    public boolean isReadOnly() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void setCatalog(String catalog) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public String getCatalog() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void setTransactionIsolation(int level) throws SQLException {
    }
    
    public int getTransactionIsolation() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public SQLWarning getWarnings() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void clearWarnings() throws SQLException {
    }
    
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void setHoldability(int holdability) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public int getHoldability() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Savepoint setSavepoint() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Savepoint setSavepoint(String name) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void rollback(Savepoint savepoint) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        throw new SQLException("Not supported.");
    }
}
