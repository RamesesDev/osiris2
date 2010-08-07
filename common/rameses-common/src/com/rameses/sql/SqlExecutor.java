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
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class SqlExecutor {
    
    private SqlManager sqlManager;
    protected String statement;
    private List<String> parameterNames = new ArrayList();
    private List parameterValues;
    private ParameterHandler parameterHandler;
    private Connection connection;
    
    private String origStatement;
    private List origParamNames;
    private ExecutorExceptionHandler exceptionHandler;
    
    //contains list of parameterValues
    private List<List> batchData;
    private Map vars;
    
    /***
     * By default, DataSource is passed by the SqlManager
     * however connection can be manually overridden by setting
     * setConnection.
     */
    SqlExecutor(SqlManager sm, String statement, List paramNames) {
        this.origStatement = statement;
        this.origParamNames = paramNames;
        this.sqlManager = sm;
        clear();
    }
    
    public void clear() {
        this.statement = origStatement;
        parameterNames.clear();
        this.parameterNames.clear();
        if(origParamNames!=null) {
            for(Object o : origParamNames) {
                this.parameterNames.add((String)o);
            }
        }
        parameterValues = new ArrayList();
        parameterValues.clear();
        if(batchData!=null) batchData.clear();
        batchData = null;        
    }
    
    
    public SqlExecutor setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }
    
    public void setParameterHandler(ParameterHandler paramHandler) {
        this.parameterHandler = paramHandler;
    }
    
    
    public Object execute() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            if(connection!=null)
                conn = connection;
            else
                conn = sqlManager.getConnection();
            if(parameterHandler==null) 
                parameterHandler = new BasicParameterHandler();
            
            //get the results
            ps = conn.prepareStatement( getFixedSqlStatement() );
            
            if(batchData==null) {
                fillParameters(ps);
                return ps.executeUpdate();
            }
            else {
                int i = 0;
                for(List o: batchData)  {
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
            }    
            else
                throw new IllegalStateException(ex.getMessage());
        } 
        finally {
            try {ps.close();} catch(Exception ign){;}
            try {
                //close if connection is not manually injected.
                if(connection==null) {
                    conn.close();
                }
            } catch(Exception ign){;}
            
            //make sure to clear the data
            clear();
        }
    }
    
   
    
    // <editor-fold defaultstate="collapsed" desc="SETTING PARAMETER OPTIONS">
    public SqlExecutor setParameter( int idx, Object v ) {
        if(idx<=0) 
            throw new IllegalStateException("Index must be 1 or higher");         
        parameterValues.add(idx-1, v);
        return this;
    }
    
    public SqlExecutor setParameter( String name, Object v ) {
        int idx = -1;
        for(int i=0; i<parameterNames.size();i++) {
            if(parameterNames.get(i).equals(name)) {
                parameterValues.add(i, v);
                return this;
            }
        }
        throw new IllegalStateException("Parameter " + name + " is not found");
    }
    
    
    /***
     * This method allows adding of parameters stored as one map.
     * It requires parameter names must exist. To do this,
     * your statement must have the $P{param} named parameter.
     */
    public SqlExecutor setParameters( Map map ) {
        if(map==null) {
            //do nothing and return it
            return this;
        }
        
        if(parameterNames==null)
            throw new IllegalStateException("Parameter Names must not be null. Please indicate $P{paramName} in your statement");
        int sz = parameterNames.size();
        parameterValues.clear();
        for(int i=0;i<sz;i++ ) {
            parameterValues.add(  i, map.get( parameterNames.get(i)  ));
        }
        return this;
    }
    
    
    public SqlExecutor setParameters( List params ) {
        if(params==null) {
            //do nothing and return it
            return this;
        }

        if(parameterNames!=null && parameterNames.size()>0){
            if(parameterNames.size()!=params.size())
                throw new IllegalStateException("Parameter count does not match");
        }        
        int sz = params.size();
        parameterValues.clear();
        for(int i=0;i<sz;i++ ) {
            parameterValues.add(  i, params.get(i) );
        }
        return this;
    }
    //</editor-fold>

    /***
     * Apply the correct statments. can be overridden
     */
    private String getFixedSqlStatement() {
        return statement;
    }
    
   
    protected void fillParameters( PreparedStatement ps ) throws Exception {
        
        int sz = parameterValues.size();
        for( int i=0; i<sz;i++) {
            String name = null;
            if(parameterNames!=null && parameterNames.size()>0) {
                name = parameterNames.get(i);
            }     
            int colIndex = i+1;        
            Object value = parameterValues.get(i);
            parameterHandler.setParameter(ps,colIndex,value,name);
        }
    }

    public SqlExecutor addBatch() {
        if(batchData==null) batchData = new ArrayList();
        if(parameterValues.size()==0)
            throw new IllegalStateException("Add batch failed. There must be at least one parameter specified");
        batchData.add(parameterValues);
        parameterValues = new ArrayList();
        return this;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }
    
    /**
     * used when setting variables to a statement
     */
    public SqlExecutor setVars( Map map ) {
        this.vars = map;
        this.statement = SqlUtil.substituteValues( this.origStatement, map );
        //reparse the statement after parsing to update the parameter names
        this.statement = SqlUtil.parseStatement(statement, parameterNames);         
        return this;
    }
    
    public String getStatement() {
        return statement;
    }

    public void setExceptionHandler(ExecutorExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public List<List> getBatchData() {
        return batchData;
    }

    public List getParameterValues() {
        return parameterValues;
    }

    public String getOriginalStatement() {
        return origStatement;
    }

    public void setBatchData(List<List> batchData) {
        this.batchData = batchData;
    }
    
    
}
