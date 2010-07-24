/*
 * SqlQueryHolder.java
 *
 * Created on July 24, 2010, 11:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.Serializable;
import java.util.List;

/**
 * cache results is stored here.
 */
public class SqlCache implements Serializable {
 
    private String statement;
    private List paramNames;
    
    public SqlCache(String statement, List paramNames) {
        this.statement = statement;
        this.paramNames = paramNames;
    }

    public String getStatement() {
        return statement;
    }

    public List getParamNames() {
        return paramNames;
    }
    
}
