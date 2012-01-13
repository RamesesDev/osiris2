/*
 * MyResultSet.java
 * Created on December 21, 2011, 9:21 AM
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
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class DBServiceResultSet implements ResultSet {
    
    private List<Map> results;
    private Iterator<Map> iterator;
    private ResultSetMetaData metaData;
    private Map currentRecord;
    private int rowIndex=0;
    private int fetchSize = 10;
    private Statement statement;
    
    private Object lastValueRead;
    
    /** Creates a new instance of MyResultSet */
    public DBServiceResultSet(List results, List metaDataList, Statement st) {
        this.results = results;
        this.metaData = new DBServiceResultSetMetaData(metaDataList);
        this.iterator = results.iterator();
        this.statement = st;
    }
    
    public DBServiceResultSet(Map map) {
        this.results = (List)map.get("results");
        List metaDataList = (List)map.get("metaData");
        this.metaData = new DBServiceResultSetMetaData(metaDataList);
        this.iterator = results.iterator();
    }

    
    public boolean next() throws SQLException {
        boolean hasNext = iterator.hasNext();
        rowIndex++;
        if(hasNext) {
            currentRecord = iterator.next();
        }
        else {
            currentRecord = null;
        }
        return hasNext;
    }
    
    public void close() throws SQLException {
        iterator = null;
        results = null;
        currentRecord = null;
        metaData = null;
    }
    
    public String _getColumnName(int idx) {
        try {
            return this.metaData.getColumnLabel(idx);
        }
        catch(Exception ign) {
            return null;
        }
    }
    
    public boolean wasNull() throws SQLException {
        return (lastValueRead==null);
    }
    
    public String getString(int columnIndex) throws SQLException {
        return getString( _getColumnName(columnIndex) );
    }
    
    public boolean getBoolean(int columnIndex) throws SQLException {
        return getBoolean( _getColumnName(columnIndex) );
    }
    
    public byte getByte(int columnIndex) throws SQLException {
        return getByte( _getColumnName(columnIndex) );
    }
    
    public short getShort(int columnIndex) throws SQLException {
        return getShort( _getColumnName(columnIndex) );
    }
    
    public int getInt(int columnIndex) throws SQLException {
        return getInt( _getColumnName(columnIndex) );
    }
    
    public long getLong(int columnIndex) throws SQLException {
        return getLong(  _getColumnName(columnIndex)  );
    }
    
    public float getFloat(int columnIndex) throws SQLException {
        return getFloat(  _getColumnName(columnIndex) );
    }
    
    public double getDouble(int columnIndex) throws SQLException {
        return getDouble( _getColumnName(columnIndex) );
    }
    
    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return getBigDecimal( _getColumnName(columnIndex), scale );
    }
    
    public byte[] getBytes(int columnIndex) throws SQLException {
        return getBytes(_getColumnName(columnIndex) );
        
    }
    
    public Date getDate(int columnIndex) throws SQLException {
        return getDate( _getColumnName(columnIndex) );
    }
    
    public Time getTime(int columnIndex) throws SQLException {
        return getTime( _getColumnName(columnIndex) );
    }
    
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return getTimestamp( _getColumnName(columnIndex) );
    }
    
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return getAsciiStream( _getColumnName(columnIndex)  );
    }
    
    @Deprecated
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        throw new SQLException("Method getUnicodeStream not supported");
    }
    
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return getBinaryStream(  _getColumnName(columnIndex) );
    }
    
    public String getString(String columnName) throws SQLException {
        String s = (String)currentRecord.get(columnName);
        this.lastValueRead = s;
        return s;
    }
    
    public boolean getBoolean(String columnName) throws SQLException {
         boolean b = Boolean.parseBoolean(currentRecord.get(columnName)+"");
         this.lastValueRead = b;
         return b;
    }
    
    public byte getByte(String columnName) throws SQLException {
        byte b = Byte.parseByte(currentRecord.get( columnName)+"");
        this.lastValueRead = b;
        return b;
    }
    
    public short getShort(String columnName) throws SQLException {
        short s = Short.parseShort(currentRecord.get( columnName)+"");
        this.lastValueRead = s;
        return s;
    }
    
    public int getInt(String columnName) throws SQLException {
        int i = Integer.parseInt(currentRecord.get( columnName )+"");
        this.lastValueRead = i;
        return i;
    }
    
    public long getLong(String columnName) throws SQLException {
        long l = Long.parseLong(currentRecord.get( columnName )+"");
        this.lastValueRead = l;
        return l;
    }
    
    public float getFloat(String columnName) throws SQLException {
        float f = Float.parseFloat(currentRecord.get(columnName)+"");
        this.lastValueRead = f;
        return f;
    }
    
    public double getDouble(String columnName) throws SQLException {
        double d = Double.parseDouble(currentRecord.get(columnName)+"");
        this.lastValueRead = d;
        return d;
    }
    
    @Deprecated
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        BigDecimal bd = new BigDecimal(currentRecord.get( columnName )+"");
        bd.setScale(scale);
        this.lastValueRead = bd;
        return bd;
    }
    
    public byte[] getBytes(String columnName) throws SQLException {
        byte[] b = (byte[])currentRecord.get( columnName );
        this.lastValueRead = b;
        return b;
    }
    
    public Date getDate(String columnName) throws SQLException {
        Date d = (Date)currentRecord.get( columnName );
        this.lastValueRead = d;
        return d;
    }
    
    public Time getTime(String columnName) throws SQLException {
        Time t = (Time)currentRecord.get( columnName );
        this.lastValueRead = t;
        return t;
    }
    
    public Timestamp getTimestamp(String columnName) throws SQLException {
        Timestamp t = (Timestamp)currentRecord.get( columnName );
        this.lastValueRead = t;
        return t;
    }
    
    public InputStream getAsciiStream(String columnName) throws SQLException {
        InputStream is = (InputStream)currentRecord.get(columnName );
        this.lastValueRead = is;
        return is;
    }
    
    @Deprecated
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        InputStream is = (InputStream) currentRecord.get(columnName );
        this.lastValueRead = is;
        return is;
    }
    
    public InputStream getBinaryStream(String columnName) throws SQLException {
        InputStream is = (InputStream)currentRecord.get(columnName);
        this.lastValueRead = is;
        return is;
    }
    
    public SQLWarning getWarnings() throws SQLException {
        throw new SQLException("Unsupported method. getWarnings");
    }
    
    public void clearWarnings() throws SQLException {
        throw new SQLException("Unsupported method. clearWarnings");
    }
    
    public String getCursorName() throws SQLException {
        throw new SQLException("Unsupported method. getCursorName");
    }
    
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.metaData;
    }
    
    public Object getObject(int columnIndex) throws SQLException {
        return getObject( _getColumnName(columnIndex)) ;
    }
    
    public Object getObject(String columnName) throws SQLException {
        Object o = currentRecord.get(columnName);
        this.lastValueRead = o;
        return o;
    }
    
    public int findColumn(String columnName) throws SQLException {
        for(int i=1; i<=metaData.getColumnCount();i++) {
            if( columnName.equals(metaData.getColumnLabel(i)) ) {
                return i;
            }
        }
        return 0;
    }
    
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return getCharacterStream( _getColumnName(columnIndex) );
    }
    
    public Reader getCharacterStream(String columnName) throws SQLException {
        Reader r = (Reader)currentRecord.get(columnName);
        this.lastValueRead = r;
        return r;
    }
    
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return getBigDecimal(columnIndex, 0);
    }
    
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return getBigDecimal(columnName, 0);
    }
    
    public boolean isBeforeFirst() throws SQLException {
         throw new SQLException("Not supported.isBeforeFirst");
        //System.out.println("RS::isBeforeFirst");
        //return rs.isBeforeFirst();
    }
    
    public boolean isAfterLast() throws SQLException {
        throw new SQLException("Not supported.isAfterFirst");
    }
    
    public boolean isFirst() throws SQLException {
        throw new SQLException("Not supported.isFirst");
    }
    
    public boolean isLast() throws SQLException {
        throw new SQLException("Not supported.isLast");
    }
    
    public void beforeFirst() throws SQLException {
        throw new SQLException("Not supported.beforeFirst");
    }
    
    public void afterLast() throws SQLException {
        throw new SQLException("Not supported.afterLast");
    }
    
    public boolean first() throws SQLException {
        return rowIndex == 1;
    }
    
    public boolean last() throws SQLException {
        throw new SQLException("Not supported.last");
    }
    
    public int getRow() throws SQLException {
        return rowIndex;
    }
    
    public boolean absolute(int row) throws SQLException {
       throw new SQLException("Not supported.absolute");
    }
    
    public boolean relative(int rows) throws SQLException {
       throw new SQLException("Not supported.relative");
    }
    
    public boolean previous() throws SQLException {
        throw new SQLException("Not supported.previous");
    }
    
    public void setFetchDirection(int direction) throws SQLException {
        throw new SQLException("Not supported.direction");
    }
    
    public int getFetchDirection() throws SQLException {
        throw new SQLException("Not supported.fetchDirection");
    }
    
    public void setFetchSize(int rows) throws SQLException {
        throw new SQLException("Not supported.setFetchSize");
    }
    
    public int getFetchSize() throws SQLException {
        throw new SQLException("Not supported.getFetchSize");
    }
    
    public int getType() throws SQLException {
       throw new SQLException("Not supported.getType");
    }
    
    public int getConcurrency() throws SQLException {
       throw new SQLException("Not supported.getConcurrency");
    }
    
    public boolean rowUpdated() throws SQLException {
        throw new SQLException("Not supported.rowUpdated");
    }
    
    public boolean rowInserted() throws SQLException {
        throw new SQLException("Not supported.rowInserted");
    }
    
    public boolean rowDeleted() throws SQLException {
        throw new SQLException("Not supported.rowDeleted");
    }
    
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLException("Not supported.updateNull");
    }
    
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLException("Not supported.updateBoolean");
    }
    
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateNull(String columnName) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateByte(String columnName, byte x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateShort(String columnName, short x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateInt(String columnName, int x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateLong(String columnName, long x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateFloat(String columnName, float x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateDouble(String columnName, double x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateString(String columnName, String x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateBytes(String columnName, byte[] x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateDate(String columnName, Date x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateTime(String columnName, Time x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateObject(String columnName, Object x) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void insertRow() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateRow() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void deleteRow() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void refreshRow() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void cancelRowUpdates() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void moveToInsertRow() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void moveToCurrentRow() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Statement getStatement() throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Ref getRef(int i) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Blob getBlob(int i) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Clob getClob(int i) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Array getArray(int i) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Ref getRef(String colName) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Blob getBlob(String colName) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Clob getClob(String colName) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Array getArray(String colName) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public URL getURL(int columnIndex) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public URL getURL(String columnName) throws SQLException {
        throw new SQLException("Not supported.");
    }
    
    public void updateRef(int columnIndex, Ref x) throws SQLException {
    }
    
    public void updateRef(String columnName, Ref x) throws SQLException {
    }
    
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
    }
    
    public void updateBlob(String columnName, Blob x) throws SQLException {
    }
    
    public void updateClob(int columnIndex, Clob x) throws SQLException {
    }
    
    public void updateClob(String columnName, Clob x) throws SQLException {
    }
    
    public void updateArray(int columnIndex, Array x) throws SQLException {
    }
    
    public void updateArray(String columnName, Array x) throws SQLException {
    }
    
}
