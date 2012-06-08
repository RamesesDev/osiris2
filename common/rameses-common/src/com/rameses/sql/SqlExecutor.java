/*
 * BatchExecutor.java
 *
 * Created on July 21, 2010, 9:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class SqlExecutor extends AbstractSqlTxn {
    
    private ExecutorExceptionHandler exceptionHandler;
    
    //contains list of parameterValues
    private List<Map> batchData;
    
    /***
     * By default, DataSource is passed by the SqlManager
     * however connection can be manually overridden by setting
     * setConnection.
     */
    SqlExecutor(SqlContext sm, String statement, List paramNames, String origStatement) {
        super(sm,statement,paramNames, origStatement);
    }
        
    public Object execute() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        String oldCatalogName = null;
        try {
            if(connection!=null)
                conn = connection;
            else
                conn = sqlContext.getConnection();
            
            //prepare the statement
            super.prepareStatement();
            
            //use database if specified
            if( super.getCatalog()!=null ) {
                oldCatalogName = conn.getCatalog();
                conn.setCatalog(super.getCatalog());
            }
            
            if(parameterHandler==null)
                parameterHandler = new BasicParameterHandler();
            
            //get the results
            ps = conn.prepareStatement( getFixedSqlStatement() );
            
            if(batchData==null) {
                fillParameters(ps);
                return ps.executeUpdate();
            } else {
                int i = 0;
                for(Map o: batchData)  {
                    parameterValues = o;
                    fillParameters(ps);
                    ps.addBatch();
                }
                return ps.executeBatch();
            }
        } catch(Exception ex) {
            if(exceptionHandler!=null) {
                exceptionHandler.handleException(this, ex);
                return 0;
            } else
                throw new RuntimeException(ex.getMessage());
        } finally {
            try {ps.close();} catch(Exception ign){;}
            try {
                if(oldCatalogName!=null) conn.setCatalog(oldCatalogName);
                //close if connection is not manually injected.
                if(connection==null) {
                    conn.close();
                }
            } catch(Exception ign){;}
            
            //make sure to clear the data
            clear();
        }
    }
    
    
    
    public SqlExecutor setParameter( int idx, Object v ) {
        _setParameter(idx,v);
        return this;
    }
    
    public SqlExecutor setParameter( String name, Object v ) {
        _setParameter(name, v);
        return this;
    }
    
    public SqlExecutor setParameters( Map map ) {
        _setParameters(map);
        return this;
    }
    
    
    public SqlExecutor setParameters( List params ) {
        _setParameters(params);
        return this;
    }
    
    public SqlExecutor addBatch() {
        if(batchData==null) batchData = new ArrayList();
        if(parameterValues.size()==0)
            throw new RuntimeException("Add batch failed. There must be at least one parameter specified");
        
        Map batchParams = new HashMap();
        batchParams.putAll(parameterValues);
        batchData.add(batchParams);
        
        parameterValues.clear();
        return this;
    }
    
    /**
     * used when setting variables to a statement
     */
    public SqlExecutor setVars( Map map ) {
        _setVars(map);
        return this;
    }
    
    public void setExceptionHandler(ExecutorExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
    
    public List<Map> getBatchData() {
        return batchData;
    }
    
    public void setBatchData(List<Map> batchData) {
        this.batchData = batchData;
    }
    
    
    
}
