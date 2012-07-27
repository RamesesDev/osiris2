/*
 * SQLQuery.java
 *
 * Created on July 21, 2010, 8:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class SqlQuery extends AbstractSqlTxn {
    
    private FetchHandler fetchHandler;
    private int firstResult;
    private int maxResults;
    private int rowsFetched = 0;
    private SqlDialect dialect;
    private String[] pagingKeys = new String[]{};
    
    /***
     * By default, DataSource is passed by the SqlManager
     * however connection can be manually overridden by setting
     * setConnection.
     */
    SqlQuery(SqlContext sm, String statement, List paramNames) {
        super( sm, statement, paramNames );
    }
    
    public void setFetchHandler(FetchHandler resultHandler) {
        this.fetchHandler = resultHandler;
    }
    
    /***
     * if there are no records found, this method throws
     * a NoResultFoundException.
     */
    public List getResultList() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String oldCatalogName = null;
        try {
            if(connection!=null)
                conn = connection;
            else
                conn = sqlContext.getConnection();
            
            //use the database if specified
            if( super.getCatalog()!=null ) {
                oldCatalogName = conn.getCatalog();
                conn.setCatalog(super.getCatalog());
            }
            
            if(fetchHandler==null)
                fetchHandler = new MapFetchHandler();
            
            if(parameterHandler==null)
                parameterHandler = new BasicParameterHandler();
            
            //get the results
            String _sql = getFixedSqlStatement();
            if( dialect != null && maxResults > 0 ) {
                _sql = dialect.getPagingStatement( statement, firstResult, maxResults, pagingKeys );
            }
            
            ps = conn.prepareStatement( _sql );
            fillParameters(ps);
            
            if( maxResults > 0) {
                ps.setFetchSize(maxResults);
            }
            
            //do paging here.
            rs = ps.executeQuery();
            List resultList = new ArrayList();
            
            //check first the size of the cursor against the size of the set.
            //if maxResults > 0 and size of set > maxResults
            /*
            if( firstResult != 0 ) {
                rs.absolute(firstResult);
            }
            
             */
            fetchHandler.start();
            rowsFetched = 0;
            while(rs.next()) {
                rowsFetched = rowsFetched+1;
                //handle the object and return as object.
                //if object is null do not store in list.
                Object val = fetchHandler.getObject(rs);
                if(val!=null) {
                    resultList.add( val );
                }
                if(maxResults>0 && (rowsFetched>=maxResults)) break;
            }
            fetchHandler.end();
            return resultList;
            
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {rs.close();} catch(Exception ign){;}
            try {ps.close();} catch(Exception ign){;}
            try {
                if(oldCatalogName!=null) conn.setCatalog(oldCatalogName);
                //close if connection is not manually injected.
                if(connection==null) {
                    conn.close();
                }
            } catch(Exception ign){;}
            clear();
        }
    }
    
    
    public SqlQuery setParameter( int idx, Object v ) {
        _setParameter(idx, v);
        return this;
    }
    
    public SqlQuery setParameter( String name, Object v ) {
        _setParameter(name, v);
        return this;
    }
    
    public SqlQuery setParameters( Map map ) {
        _setParameters(map);
        return this;
    }
    
    public SqlQuery setParameters( List params ) {
        _setParameters(params);
        return this;
    }
    
    public SqlQuery setFirstResult(int startRow) {
        this.firstResult = startRow;
        return this;
    }
    
    public SqlQuery setMaxResults(int maxRows) {
        this.maxResults = maxRows;
        return this;
    }
    
    public int getStartRow() {
        return firstResult;
    }
    
    public int getMaxRows() {
        return maxResults;
    }
    
    public int getRowsFetched() {
        return rowsFetched;
    }
    
    public Object getSingleResult() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String oldCatalogName = null;
        try {
            if(connection!=null)
                conn = connection;
            else
                conn = sqlContext.getConnection();
            
            //use the database if specified
            if( super.getCatalog()!=null ) {
                oldCatalogName = conn.getCatalog();
                conn.setCatalog(super.getCatalog());
            }
            
            if(fetchHandler==null)
                fetchHandler = new MapFetchHandler();
            
            if(parameterHandler==null)
                parameterHandler = new BasicParameterHandler();
            
            //get the results
            ps = conn.prepareStatement( getFixedSqlStatement() );
            fillParameters(ps);
            
            //do paging here.
            rs = ps.executeQuery();
            
            fetchHandler.start();
            if(!rs.next())
                return null;
            Object val = fetchHandler.getObject(rs);
            fetchHandler.end();
            return val;
            
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {rs.close();} catch(Exception ign){;}
            try {ps.close();} catch(Exception ign){;}
            try {
                if(oldCatalogName!=null) conn.setCatalog(oldCatalogName);
                //close if connection is not manually injected.
                if(connection==null) {
                    conn.close();
                }
            } catch(Exception ign){;}
            clear();
        }
    }
    
    /**
     * used when setting variables to a statement
     */
    public SqlQuery setVars( Map map ) {
        _setVars(map);
        return this;
    }
    
    private List<Map> metaData;
    
    public void resetMetaData() {
        metaData = null;
    }
    
    public List<Map> getMetaData() {
        if(metaData!=null) return metaData;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String oldCatalogName = null;
        try {
            if(connection!=null)
                conn = connection;
            else
                conn = sqlContext.getConnection();
            //use the database if specified
            if( super.getCatalog()!=null ) {
                oldCatalogName = conn.getCatalog();
                conn.setCatalog(super.getCatalog());
            }
            
            if(parameterHandler==null)
                parameterHandler = new BasicParameterHandler();
            
            //get the results
            ps = conn.prepareStatement( getFixedSqlStatement() );
            fillParameters(ps);
            
            //do paging here.
            rs = ps.executeQuery();
            metaData = new ArrayList();
            
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
        } finally {
            try {rs.close();} catch(Exception ign){;}
            try {ps.close();} catch(Exception ign){;}
            try {
                if(oldCatalogName!=null) conn.setCatalog(oldCatalogName);
                //close if connection is not manually injected.
                if(connection==null) {
                    conn.close();
                }
            } catch(Exception ign){;}
            clear();
        }
    }
    
    
    
    public SqlDialect getDialect() {
        return dialect;
    }

    public void setDialect(SqlDialect dialect) {
        this.dialect = dialect;
    }

    public String[] getPagingKeys() {
        return pagingKeys;
    }

    public void setPagingKeys(String[] pagingIds) {
        this.pagingKeys = pagingIds;
    }
    
    public void setPagingKeys(String pagingIds) {
        this.pagingKeys = new String[]{pagingIds};
    }
}
