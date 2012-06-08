/*
 * SQLQuery.java
 *
 * Created on July 21, 2010, 8:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import com.rameses.util.ExprUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for SqlQuery and SqlExecutor
 */
public abstract class AbstractSqlTxn {
    
    private String catalog;
    protected SqlContext sqlContext;
    protected String statement;
    protected Connection connection;
    protected String origStatement;
    protected String oldStatement;
    protected List origParamNames;
    protected ParameterHandler parameterHandler;
    
    protected Map vars;
    protected List parameterNames = new ArrayList();
    protected Map parameterValues = new HashMap();
    
    
    
    /***
     * By default, DataSource is passed by the SqlManager
     * however connection can be manually overridden by setting
     * setConnection. Dialect can also be set.
     */
    AbstractSqlTxn(SqlContext sm, String statement, List paramNames, String origStatement) {
        this.origStatement = origStatement;
        this.statement = statement;
        this.oldStatement = statement;
        this.origParamNames = paramNames;
        this.sqlContext = sm;
        clear();
    }
    
    //this resets the statements
    public final void clear() {
        this.statement = oldStatement;
        parameterNames.clear();
        if(origParamNames!=null) {
            parameterNames.addAll(origParamNames);
        }
    }
    
    /**
     * this is resusable code but does not return the specific transaction
     *
     */
    protected final void _setConnection(Connection connection) {
        this.connection = connection;
    }
    
    protected final void _setParameter(int idx, Object v) {
        parameterValues.put(idx, v);
    }
    
    protected final void _setParameter(String name, Object v) {
        parameterValues.put(name, v);
    }
    
    
    
    
    /***
     * This method allows adding of parameters stored as one map.
     */
    protected final void _setParameters( Map map ) {
        if(map==null) return;
        
        parameterValues.putAll( map );
    }
    
    
    protected final void _setParameters( List params ) {
        if(params==null) return;
        
        for(int i=0; i<params.size(); ++i) {
            parameterValues.put(i+1, params.get(i));
        }
    }
    
    
    /**
     * used when setting variables to a statement
     */
    protected final void _setVars( Map map ) {
        this.vars = map;
    }
    
    /***
     * Apply the correct statments. can be overridden
     */
    protected final String getFixedSqlStatement() {
        return statement;
    }
    
    protected final void fillParameters( PreparedStatement ps ) throws Exception {
        int sz = parameterNames.size();
        
        for( int i=0; i<sz;i++) {
            int colIndex = i+1;
            Object key = parameterNames.get(i);
            Object value = parameterValues.get(key);
            String name = (key instanceof Integer) ? null : key.toString();
            parameterHandler.setParameter(ps,colIndex,value,name);
        }
    }
    
    public final void setParameterHandler(ParameterHandler parameterHandler) {
        this.parameterHandler = parameterHandler;
    }
    
    public final String getStatement() {
        return statement;
    }
    
    public final String getOriginalStatement() {
        return origStatement;
    }
    
    public final Map getVars() {
        return vars;
    }
    
    public final List<String> getParameterNames() {
        return parameterNames;
    }
    
    public final SqlContext getSqlContext() {
        return sqlContext;
    }
    
    public final Map getParameterValues() {
        return parameterValues;
    }
    
    public final String getCatalog() {
        return catalog;
    }
    
    public final AbstractSqlTxn setCatalog(String dbName) {
        this.catalog = dbName;
        return this;
    }
    
    protected void prepareStatement() {
        if( vars != null && !vars.isEmpty() ) {
            this.statement = ExprUtil.substituteValues( this.origStatement, vars );
            //reparse the statement after parsing to update the parameter names
            parameterNames.clear();
            this.statement = SqlUtil.parseStatement(statement, parameterNames);
        }
    }
}
