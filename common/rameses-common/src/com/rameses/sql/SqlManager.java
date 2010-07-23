/*
 * SQLManager.java
 *
 * Created on July 21, 2010, 8:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * The sql manager ideally is unique per datasource.
 * however this behavior can be changed
 */
public class SqlManager {
    
    protected DataSource dataSource;
    private Map<String,Object[]> parsedQueries = new Hashtable();
    private NamedQueryProvider namedQueryProvider = new BasicNamedQueryProvider();
    
    /** Creates a new instance of SQLManager */
    public SqlManager(DataSource ds) {
        this.dataSource = ds;
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
    
    private Connection txnconn;
    
    public Connection getConnection() throws Exception {
        if(dataSource==null)
            throw new IllegalStateException("Datasource is null");

        if(txnconn==null) txnconn = dataSource.getConnection();
        if(txnconn.isClosed()) txnconn = dataSource.getConnection();
        return txnconn;
    }
    
    
    public SqlQuery createQuery(String statement) {
        Object[] arr = getParsedSql(statement);
        String parsedStatement = (String)arr[0];
        List paramNames = (List)arr[1];
        return new SqlQuery(this,parsedStatement,paramNames);
    }
    
    public SqlQuery createNamedQuery(String name) {
        if(namedQueryProvider==null)
            throw new IllegalStateException("NamedQueryProvider must be provided");
        return createQuery( namedQueryProvider.getStatement(name));
    }

    public SqlExecutor createExecutor(String statement) {
        Object[] arr = getParsedSql(statement);
        String parsedStatement = (String)arr[0];
        List paramNames = (List)arr[1];
        return new SqlExecutor(this,parsedStatement,paramNames);
    }

    public SqlExecutor createNamedExecutor( String name ) {
        if(namedQueryProvider==null)
            throw new IllegalStateException("NamedQueryProvider must be provided");
        return createExecutor( namedQueryProvider.getStatement(name));    
    }
    
    public NamedQueryProvider getNamedQueryProvider() {
        return namedQueryProvider;
    }

    public void setNamedQueryProvider(NamedQueryProvider namedQueryProvider) {
        this.namedQueryProvider = namedQueryProvider;
    }
    
    public QueryExecutor createQueryExecutor(SqlQuery q, SqlExecutor e) {
        return new QueryExecutor(q,e);
    }
    
}
