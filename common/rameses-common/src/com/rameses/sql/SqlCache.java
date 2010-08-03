/*
 * SqlQueryHolder.java
 *
 * Created on July 24, 2010, 11:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * cache results is stored here.
 */
public class SqlCache implements Serializable {
 
    private String statement;
    private List paramNames;
    
    public SqlCache(String origStatement) {
        parseStatement( origStatement );
    }
    
    public SqlCache(InputStream is) {
        try {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            while((i=is.read())!=-1) {
                sb.append((char)i);
            }
            parseStatement( sb.toString() );
        } catch(Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
    protected void parseStatement(String stmt) {
        paramNames = new ArrayList();
        this.statement = SqlUtil.parseStatement(stmt,paramNames);
    }

    public String getStatement() {
        return statement;
    }

    public List getParamNames() {
        return paramNames;
    }

    
    
    
}
