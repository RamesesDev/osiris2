/*
 * SqlExecutor.java
 *
 * Created on May 7, 2009, 12:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.data.server;

import java.io.Serializable;

public class SqlCacheBean implements Serializable {
    
    private String datasource;
    private String statement;
    private String method;
    private String handler;
    private Integer batchSize;
    
    public SqlCacheBean() {
    }
    
    public String getDatasource() {
        return datasource;
    }
    
    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }
    //</editor-fold>

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    
    
}


