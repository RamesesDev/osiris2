/*
 * MyStatement.java
 * Created on December 21, 2011, 9:19 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class DBServiceStatement implements PreparedStatement {
    
    private Connection conn;
    private String statement;
    private List parameterList = new ArrayList();
    private Map parameterMap = new HashMap();
    private DBService service;
    private ResultSet rs;
    
    /** Creates a new instance of MyStatement */
    public DBServiceStatement(String statement, DBService service, Connection c) {
        this.statement = statement;
        this.service = service;
        this.conn = c;
    }
    
    public ResultSet executeQuery() throws SQLException {
        Object params = null;
        if( parameterList.size()>0 ) {
            params = parameterList;
        } else {
            params = parameterMap;
        }
        try {
            Map m = this.service.getResultSet(statement, params);
            List results = (List)m.get("results");
            List metaData = (List)m.get("metaData");
            rs = new DBServiceResultSet(results,metaData,this);
            return rs;
        } catch(Exception e ) {
            throw new SQLException(e.getMessage());
        }
    }
    
    private void setParameter( int parameterIndex, Object x ) {
        parameterList.add(x);
    }
    
    public int executeUpdate() throws SQLException {
        throw new SQLException("Method not supported. executeUpdate ");
    }
    
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        setParameter(parameterIndex, null);
    }
    
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setByte(int parameterIndex, byte x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setShort(int parameterIndex, short x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setInt(int parameterIndex, int x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setLong(int parameterIndex, long x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setFloat(int parameterIndex, float x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setDouble(int parameterIndex, double x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setString(int parameterIndex, String x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setDate(int parameterIndex, Date x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setTime(int parameterIndex, Time x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new SQLException("Method not supported. setAsciiStream");
    }
    
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new SQLException("Method not supported. setUnicodeStream");
    }
    
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new SQLException("Method not supported. setBinaryStream");
    }
    
    public void clearParameters() throws SQLException {
        this.parameterList.clear();
        this.parameterMap.clear();
    }
    
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setObject(int parameterIndex, Object x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public boolean execute() throws SQLException {
        throw new SQLException("Method not supported. execute");
    }
    
    public void addBatch() throws SQLException {
        throw new SQLException("Method not supported. addBatch");
    }
    
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        throw new SQLException("Method not supported. setCharacterStream");
    }
    
    public void setRef(int i, Ref x) throws SQLException {
        throw new SQLException("Method not supported. setRef");
    }
    
    public void setBlob(int i, Blob x) throws SQLException {
        throw new SQLException("Method not supported. setBlob");
    }
    
    public void setClob(int i, Clob x) throws SQLException {
        throw new SQLException("Method not supported. setClob");
    }
    
    public void setArray(int i, Array x) throws SQLException {
        throw new SQLException("Method not supported. setArray");
    }
    
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.rs.getMetaData();
    }
    
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
        throw new SQLException("Method not supported. setNull");
    }
    
    public void setURL(int parameterIndex, URL x) throws SQLException {
        setParameter(parameterIndex, x);
    }
    
    public ParameterMetaData getParameterMetaData() throws SQLException {
        throw new SQLException("Method not supported. getParameterData");
    }
    
    public ResultSet executeQuery(String sql) throws SQLException {
        throw new SQLException("Method not supported. executeQuery");
    }
    
    public int executeUpdate(String sql) throws SQLException {
        throw new SQLException("Method not supported. executeUpdate");
    }
    
    public void close() throws SQLException {
        
    }
    
    public int getMaxFieldSize() throws SQLException {
        throw new SQLException("Method not supported. getMaxFieldSize");
    }
    
    public void setMaxFieldSize(int max) throws SQLException {
        throw new SQLException("Method not supported. setMaxFieldSize");
    }
    
    public int getMaxRows() throws SQLException {
        throw new SQLException("Method not supported. getMaxRows");
    }
    
    public void setMaxRows(int max) throws SQLException {
        throw new SQLException("Method not supported. setMaxRows");
    }
    
    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw new SQLException("Method not supported. setEscapingProcess");
    }
    
    public int getQueryTimeout() throws SQLException {
        throw new SQLException("Method not supported. getQueryTimeout");
    }
    
    public void setQueryTimeout(int seconds) throws SQLException {
        throw new SQLException("Method not supported. setQueryTimeout");
    }
    
    public void cancel() throws SQLException {
        
    }
    
    public SQLWarning getWarnings() throws SQLException {
        throw new SQLException("Method not supported. getWarnings");
    }
    
    public void clearWarnings() throws SQLException {
        throw new SQLException("Method not supported. clearWarnings");
    }
    
    public void setCursorName(String name) throws SQLException {
        throw new SQLException("Method not supported. setCursorName");
    }
    
    public boolean execute(String sql) throws SQLException {
        throw new SQLException("Method not supported. execute");
    }
    
    public ResultSet getResultSet() throws SQLException {
        return rs;
    }
    
    public int getUpdateCount() throws SQLException {
        throw new SQLException("Method not supported. getUpdateCount");
    }
    
    public boolean getMoreResults() throws SQLException {
        throw new SQLException("Method not supported. getMoreResults");
    }
    
    public void setFetchDirection(int direction) throws SQLException {
        throw new SQLException("Method not supported. setFetchDirection");
    }
    
    public int getFetchDirection() throws SQLException {
        throw new SQLException("Method not supported. getFetchDirection");
    }
    
    public void setFetchSize(int rows) throws SQLException {
        throw new SQLException("Method not supported. setFetchSize");
    }
    
    public int getFetchSize() throws SQLException {
        throw new SQLException("Method not supported. getFetchSize");
    }
    
    public int getResultSetConcurrency() throws SQLException {
        throw new SQLException("Method not supported. getResultSetConcurrency");
    }
    
    public int getResultSetType() throws SQLException {
        throw new SQLException("Method not supported. getResultSetType");
    }
    
    public void addBatch(String sql) throws SQLException {
        throw new SQLException("Method not supported. addBatch");
    }
    
    public void clearBatch() throws SQLException {
        throw new SQLException("Method not supported. clearBatch");
    }
    
    public int[] executeBatch() throws SQLException {
        throw new SQLException("Method not supported. executeBatcjh");
    }
    
    public Connection getConnection() throws SQLException {
        return conn;
    }
    
    public boolean getMoreResults(int current) throws SQLException {
        throw new SQLException("Method not supported. getMoreResults");
    }
    
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLException("Method not supported. getGeneratedKeys");
    }
    
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;
    }
    
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;
    }
    
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;
    }
    
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        throw new SQLException("Method not supported. execute");
    }
    
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLException("Method not supported. execute");
    }
    
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        throw new SQLException("Method not supported. execute");
    }
    
    public int getResultSetHoldability() throws SQLException {
        throw new SQLException("Method not supported. getResultSetHoldability");
    }
}
