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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class SqlQuery {
    
    private SqlManager sqlManager;
    protected String statement;
    
    protected List<String> parameterNames;
    protected List parameterValues;
    private FetchHandler fetchHandler;
    private ParameterHandler parameterHandler;
    private Connection connection;
    private int firstResult;
    private int maxResults;
    private int rowsFetched = 0;
    
    
    private String origStatement;
    
    /***
     * By default, DataSource is passed by the SqlManager
     * however connection can be manually overridden by setting
     * setConnection.
     */
    SqlQuery(SqlManager sm, String statement, List parameterNames) {
        this.statement = statement;
        this.origStatement = statement;
        this.sqlManager = sm;
        this.parameterNames = parameterNames;
        parameterValues = new ArrayList();
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
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
        try {
            if(connection!=null)
                conn = connection;
            else
                conn = sqlManager.getConnection();
            
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
            List resultList = new ArrayList();
            if( firstResult != 0 ) {
                rs.absolute(firstResult);
            }
            if( maxResults > 0) {
                ps.setFetchSize(maxResults);
            }
            
            rowsFetched = 0;
            while(rs.next()) {
                //handle the object and return as object.
                //if object is null do not store in list.
                Object val = fetchHandler.getObject(rs);
                if(val!=null) {
                    resultList.add( val );
                }
                if(maxResults>0 && (++rowsFetched)>=maxResults) break;
            }
            fetchHandler.end();
            return resultList;
            
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex.getMessage());
        } finally {
            try {rs.close();} catch(Exception ign){;}
            try {ps.close();} catch(Exception ign){;}
            try {
                //close if connection is not manually injected.
                if(connection==null) {
                    conn.close();
                }
            } catch(Exception ign){;}
            clear();
        }
    }
    
    public void clear() {
        //cleanup existing parameter values after execution.
        parameterValues.clear();
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="SET PARAMETER OPTIONS">
    public SqlQuery setParameter( int idx, Object v ) {
        if(idx<=0)
            throw new IllegalStateException("Index must be 1 or higher");
        parameterValues.add(idx-1, v);
        return this;
    }
    
    public SqlQuery setParameter( String name, Object v ) {
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
    public SqlQuery setParameters( Map map ) {
        if(parameterNames==null)
            throw new IllegalStateException("Parameter Names must not be null. Please indicate $P{paramName} in your statement");
        int sz = parameterNames.size();
        clear();
        for(int i=0;i<sz;i++ ) {
            parameterValues.add(  i, map.get( parameterNames.get(i)  ));
        }
        return this;
    }
    
    
    public SqlQuery setParameters( List params ) {
        if(parameterNames!=null && parameterNames.size()>0){
            if(parameterNames.size()!=params.size())
                throw new IllegalStateException("Parameter count does not match");
        }
        int sz = params.size();
        clear();
        for(int i=0;i<sz;i++ ) {
            parameterValues.add(  i, params.get(i) );
        }
        return this;
    }
    //</editor-fold>
    
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
    
    
    /***
     * Apply the correct statments. can be overridden
     */
    protected String getFixedSqlStatement() {
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
    
    public void setParameterHandler(ParameterHandler parameterHandler) {
        this.parameterHandler = parameterHandler;
    }

    public int getRowsFetched() {
        return rowsFetched;
    }
    
    
    public Object getSingleResult() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(connection!=null)
                conn = connection;
            else
                conn = sqlManager.getConnection();
            
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
            throw new IllegalStateException(ex.getMessage());
        } finally {
            try {rs.close();} catch(Exception ign){;}
            try {ps.close();} catch(Exception ign){;}
            try {
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
        this.statement = SqlUtil.substituteValues( this.origStatement, map );
        return this;
    }
    
    
}
