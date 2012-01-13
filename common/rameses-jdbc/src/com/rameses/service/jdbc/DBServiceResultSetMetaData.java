/*
 * MyResultSetMetaData.java
 * Created on December 21, 2011, 9:23 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class DBServiceResultSetMetaData implements ResultSetMetaData {
    
    private List<Map> metaData;
    
    /** Creates a new instance of MyResultSetMetaData */
    public DBServiceResultSetMetaData(List<Map> list) {
        this.metaData = list;
    }

    public int getColumnCount() throws SQLException {
        return metaData.size();
    }
        
    private Map getColumn(int column) {
        return metaData.get(column-1);
    }
    
    private boolean getBoolValue(int column, String name) {
        return Boolean.valueOf(getColumn(column).get(name)+"");
    }
    private int getIntValue(int column, String name) {
        return Integer.valueOf(getColumn(column).get(name)+"");
    }
    private String getStringValue(int column, String name) {
        return (String)(getColumn(column).get(name));
    }
    
    
    public boolean isAutoIncrement(int column) throws SQLException {
        return getBoolValue(column, "autoIncrement" );
    }

    public boolean isCaseSensitive(int column) throws SQLException {
        return getBoolValue(column, "caseSensitive" );
    }

    public boolean isSearchable(int column) throws SQLException {
        return getBoolValue(column, "searchable" );
    }

    public boolean isCurrency(int column) throws SQLException {
        return getBoolValue(column, "currency" );
    }

    public int isNullable(int column) throws SQLException {
        return  getIntValue(column, "nullable");
    }

    public boolean isSigned(int column) throws SQLException {
        return getBoolValue(column, "signed");
    }

    public int getColumnDisplaySize(int column) throws SQLException {
        return  getIntValue(column, "columnDisplaySize");
    }

    public String getColumnLabel(int column) throws SQLException {
        return  getStringValue(column, "columnLabel");
    }

    public String getColumnName(int column) throws SQLException {
        return  getStringValue(column, "columnName");
    }

    public String getSchemaName(int column) throws SQLException {
        return  getStringValue(column, "schemaName");
    }

    public int getPrecision(int column) throws SQLException {
         return  getIntValue(column, "precision");
    }

    public int getScale(int column) throws SQLException {
         return  getIntValue(column, "scale");
    }

    public String getTableName(int column) throws SQLException {
        return  getStringValue(column, "tableName");
    }

    public String getCatalogName(int column) throws SQLException {
        return  getStringValue(column, "catalogName");
    }

    public int getColumnType(int column) throws SQLException {
        return  getIntValue(column, "columnType");
    }

    public String getColumnTypeName(int column) throws SQLException {
        return  getStringValue(column, "columnTypeName");
    }

    public boolean isReadOnly(int column) throws SQLException {
        return  getBoolValue(column, "readOnly");
    }

    public boolean isWritable(int column) throws SQLException {
        return  getBoolValue(column, "writable");
    }

    public boolean isDefinitelyWritable(int column) throws SQLException {
        return  getBoolValue(column, "definitelyWritable");
    }

    public String getColumnClassName(int column) throws SQLException {
        return  getStringValue(column, "columnClassName");
    }
    
}
