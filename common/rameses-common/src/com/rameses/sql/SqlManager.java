/*
 * SQLManager.java
 *
 * Created on July 21, 2010, 8:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * The sql manager ideally is unique per datasource.
 * however this behavior can be changed
 */
public abstract class SqlManager {
    
    protected SqlDialect sqlDialect;
    protected DataSource dataSource;
    private Map<String,Object[]> parsedQueries = new Hashtable();
    private NamedQueryProvider namedQueryProvider = new BasicNamedQueryProvider();
    
    /** Creates a new instance of SQLManager */
    public SqlManager() {
    }
    
    //a statement example could come in this form:
    //select * from table where field=$P{fieldname}
    //the parsed statement is cached so we do not have
    //to parse again. SqlQuery must be created everytime.
    private Object[] getParsedSql(String statement) {
        if( !parsedQueries.containsKey(statement) ) {
            List paramNames = new ArrayList();
            String parsedStatement = SqlUtil.parseStatement(statement,paramNames);
            Object[] arr = new Object[2];
            arr[0] = parsedStatement;
            arr[1] = paramNames;
            parsedQueries.put(statement,arr);
            return arr;
        }
        else {
            return parsedQueries.get(statement);
        }
    }
    
    
    public SqlQuery createQuery(String statement) {
        Object[] arr = getParsedSql(statement);
        String parsedStatement = (String)arr[0];
        List paramNames = (List)arr[1];
        return new SqlQuery(dataSource,parsedStatement,paramNames);
    }
    
    public SqlExecutor createExecutor(String statement) {
        Object[] arr = getParsedSql(statement);
        String parsedStatement = (String)arr[0];
        List paramNames = (List)arr[1];
        return new SqlExecutor(dataSource,parsedStatement,paramNames);
    }

    public SqlQuery createNamedQuery(String name) {
        if(namedQueryProvider==null)
            throw new IllegalStateException("NamedQueryProvider must be provided");
        return createQuery( namedQueryProvider.getStatement(name));
    }

    
    public NamedQueryProvider getNamedQueryProvider() {
        return namedQueryProvider;
    }

    public void setNamedQueryProvider(NamedQueryProvider namedQueryProvider) {
        this.namedQueryProvider = namedQueryProvider;
    }
}
