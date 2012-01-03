/*
 * SqlUtil.java
 *
 * Created on February 5, 2009, 5:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rameses
 */
public final class SqlUtil {
    
    /** Creates a new instance of SqlUtil */
    public SqlUtil() {
    }
    
    private static Pattern pattern = Pattern.compile("\\$P\\{.+?\\}");
    
    
    /***
     * This method parses a statement and stores parameters in the
     * paramNames list.
     * <br>
     * For example:
     * "select * from table where fieldname = $P{fieldname}"
     * will return "select * from table where fieldname=?"
     *
     * @param sql original select statement
     * @param paramNames empty list to contain the param names extracted.
     * must not be null.
     *
     * @return the corrected sql statement
     */
    public static String parseStatement( String sql, List paramNames ) {
        if(! sql.contains("$P") ) return sql;
        
        Matcher m = pattern.matcher(sql);
        int start = 0;
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            int end = m.start();
            sb.append( sql.substring(start, end ) + "?" );
            String name = m.group().replaceAll( "\\$P\\{|\\}", "").trim();
            paramNames.add( name );
            start = end + m.group().length();
        }
        sb.append( sql.substring(start) );
        return sb.toString();
    }
    
    
    public static List<Map> resultSetToMapList( ResultSet rs ) throws Exception {
        List resultList = new ArrayList();
        MapFetchHandler fetchHandler = new MapFetchHandler();
        while(rs.next()) {
            //handle the object and return as object.
            //if object is null do not store in list.
            Object val = fetchHandler.getObject(rs);
            if(val!=null) {
                resultList.add( val );
            }
        }
        return resultList;
    }
    
    public static List<Map> resultSetToMetaData(ResultSet rs) throws Exception {
        List<Map> metaData = new ArrayList();
        try {
            ResultSetMetaData rsm = rs.getMetaData();
            for(int i=1; i<=rsm.getColumnCount(); i++) {
                Map m = new HashMap();
                m.put("catalogName", rsm.getCatalogName(i));
                m.put("columnClassName", rsm.getColumnClassName(i));
                m.put("columnDisplaySize", rsm.getColumnDisplaySize(i));
                m.put("columnLabel", rsm.getColumnLabel(i));
                m.put("columnName", rsm.getColumnName(i));
                m.put("columnType", rsm.getColumnType(i));
                m.put("columnTypeName", rsm.getColumnTypeName(i));
                m.put("precision", rsm.getPrecision(i));
                m.put("scale", rsm.getScale(i));
                m.put("schemaName", rsm.getSchemaName(i));
                m.put("tableName", rsm.getTableName(i));
                //other flags
                m.put("autoIncrement", rsm.isAutoIncrement(i));
                m.put("caseSensitive", rsm.isCaseSensitive(i));
                m.put("currency", rsm.isCurrency(i));
                m.put("definitelyWritable", rsm.isDefinitelyWritable(i));
                m.put("nullable", rsm.isNullable(i));
                m.put("readOnly", rsm.isReadOnly(i));
                m.put("searchable", rsm.isSearchable(i));
                m.put("signed", rsm.isSigned(i));
                m.put("writable", rsm.isWritable(i));
                metaData.add(m);
            }
            return metaData;
            
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
        
    }
    
}
