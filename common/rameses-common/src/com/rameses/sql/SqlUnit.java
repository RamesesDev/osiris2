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
import java.util.ArrayList;
import java.util.List;

/**
 * cache results is stored here.
 */
public class SqlUnit implements Serializable {
 
    private String statement;
    private List paramNames;
    
    public SqlUnit(String origStatement) {
        paramNames = new ArrayList();
        this.statement = SqlUtil.parseStatement(origStatement,paramNames);
    }
    
    public SqlUnit(String statement, List paramNames) {
        this.paramNames = paramNames;
        this.statement = statement;
    }
    
    public String getStatement() {
        return statement;
    }

    public List getParamNames() {
        return paramNames;
    }

    
    
    
}
