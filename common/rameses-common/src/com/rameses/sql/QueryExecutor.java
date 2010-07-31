/*
 * QueryExecutorChain.java
 *
 * Created on July 22, 2010, 12:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * this is a special class for chaining queries and execution.
 * Useful for migration purposes.
 */
public class QueryExecutor {
    
    private SqlQuery qry;
    private SqlExecutor executor;
    private FetchHandler handler;
    private long rowsProcessed;
    private int batchSize = 10;
    
    /** Creates a new instance of QueryExecutorChain */
    public QueryExecutor(SqlQuery qry, SqlExecutor exec) {
        this.qry = qry;
        this.executor = exec;
    }
    
    public QueryExecutor() {
        
    }
    
    public void setQuery(SqlQuery q) {
        this.qry = q;
    }
    
    public void setExecutor(SqlExecutor e) {
        this.executor = e;
    }
    
    public long execute(SqlQuery q, SqlExecutor e) throws Exception {
        this.qry = q;
        this.executor = e;
        return execute();
    }
    
    /***
     * This method gets reult list from the query and for each 
     * record read, it is feed to the sql executor. The query
     * result list must contain fields with same name as the executor's
     * parameters.
     * 
     * @return  number of records processed
     */
    
    public long execute() throws Exception {
        if(qry==null)
            throw new Exception("Sql Query must not be null");
        if(executor==null)
            throw new Exception("Query Executor must not be null");

        if(handler==null) {
            if(executor.getParameterNames()!=null && executor.getParameterNames().size()>0) 
                handler = new NamedParamFetchHandler();
            else
                handler = new UnnamedParamFetchHandler();
        }
        qry.setFetchHandler(handler);
        rowsProcessed = 0;
        qry.getResultList();
        
        //make sure to nullify qry and executor so it cannot be used again.
        qry = null;
        return rowsProcessed;
    }
    
    private abstract class AbstractFetchHandler implements FetchHandler {
        private int batchCounter;
        
        public void start() {
            batchCounter = 0;
            executor.clear();
        }
        public void end() {
            try {
                if(batchCounter>0) {
                    executor.execute();
                }
            }
            catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
        
        protected abstract void setObjectParams(ResultSet rs) throws Exception;
        
        public Object getObject(ResultSet rs) throws Exception {
            batchCounter++;
            rowsProcessed = rowsProcessed + 1;
            setObjectParams(rs);
            executor.addBatch();

            if(batchCounter>=batchSize) {
                executor.execute();
                executor.clear();
                batchCounter = 0;
            }
            //return null so it wont be stored in result list.
            return null;
        }
    }
    
    
    private class UnnamedParamFetchHandler extends AbstractFetchHandler {
        public void setObjectParams(ResultSet rs) throws Exception {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i=0; i<columnCount; i++) {
                executor.setParameter(i+1, rs.getObject(i+1) );
            }
        }
    }
    
    private class NamedParamFetchHandler extends AbstractFetchHandler {
        public void setObjectParams(ResultSet rs) throws Exception {
            ResultSetMetaData meta = rs.getMetaData();
            for(String name: executor.getParameterNames()) {
                executor.setParameter( name, rs.getObject( name ) );
            }
        }
    }
    
    public long getRowsProcessed() {
        return rowsProcessed;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public QueryExecutor setBatchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    
}
