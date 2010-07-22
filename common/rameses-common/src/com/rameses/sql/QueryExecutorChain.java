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
public class QueryExecutorChain {
    
    private SqlQuery qry;
    private SqlExecutor executor;
    private FetchHandler handler;
    private int rowsProcessed;
    private int batchSize = 25;
    
    private long delay = 0;
    
    /** Creates a new instance of QueryExecutorChain */
    public QueryExecutorChain(SqlQuery qry, SqlExecutor exec) {
        this.qry = qry;
        this.executor = exec;
    }
    
    public void execute() throws Exception {
        if(handler==null) {
            if(executor.getParameterNames()!=null && executor.getParameterNames().size()>0) 
                handler = new NamedParamFetchHandler();
            else
                handler = new UnnamedParamFetchHandler();
        }
        qry.setFetchHandler(handler);
        qry.setMaxResults(batchSize);
        rowsProcessed = 0;
        int startRow = 0;
        while(true) {
            try {
                qry.setFirstResult(startRow);
                qry.getResultList();
                int rowsFetched = qry.getRowsFetched(); 
                rowsProcessed += rowsFetched;
                if(rowsFetched==0 || rowsFetched<batchSize) {
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
    }
    
    // <editor-fold defaultstate="collapsed" desc="FETCH HANDLER USED FOR UNNAMED COLUMNS">
    private abstract class AbstractFetchHandler implements FetchHandler {
        public void start() {
            System.out.println("start batch");
        }
        
        public void end() {
            try {
                executor.execute();
            }
            catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    
    private class UnnamedParamFetchHandler extends AbstractFetchHandler {
        public Object getObject(ResultSet rs) throws Exception {
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
    
    public int getRowsProcessed() {
        return rowsProcessed;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    
}
