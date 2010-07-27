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
    private int batchSize = 0;
    
    private long delay = 0;
    
    
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
        if(batchSize>0) qry.setMaxResults(batchSize);
        rowsProcessed = 0;
        int startRow = 0;
        while(true) {
            try {
                if(batchSize>0) qry.setFirstResult(startRow);
                qry.getResultList();
                int rowsFetched = qry.getRowsFetched(); 
                if(batchSize==0 || rowsFetched<batchSize) {
                    //we have reached the end
                    break;
                } 
                startRow += batchSize; 
                
                //dont be greedy. add a delay so it can play nicely with other threads 
                if(delay > 0 )Thread.sleep(delay);
            }
            catch(Exception e) {
                throw e;
            }
        }
        //this starts the processor.
        return rowsProcessed;
    }
    
    // <editor-fold defaultstate="collapsed" desc="FETCH HANDLER USED FOR UNNAMED COLUMNS">
    private abstract class AbstractFetchHandler implements FetchHandler {
        public void start() {
            executor.clear();
        }
        public void end() {
            try {
                if(rowsProcessed>0) {
                    executor.execute();
                }
            }
            catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    
    private class UnnamedParamFetchHandler extends AbstractFetchHandler {
        public Object getObject(ResultSet rs) throws Exception {
            rowsProcessed = rowsProcessed + 1;
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i=0; i<columnCount; i++) {
                executor.setParameter(i+1, rs.getObject(i+1) );
            }
            executor.addBatch();
            //return null so it wont be stored in result list.
            return null;
        }
    }
    
    private class NamedParamFetchHandler extends AbstractFetchHandler {
        public Object getObject(ResultSet rs) throws Exception {
            rowsProcessed = rowsProcessed + 1;            
            ResultSetMetaData meta = rs.getMetaData();
            for(String name: executor.getParameterNames()) {
                executor.setParameter( name, rs.getObject( name ) );
            }
            executor.addBatch();
            //return null so it wont be stored in result list.
            return null;
        }
    }
    //</editor-fold>
    
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
