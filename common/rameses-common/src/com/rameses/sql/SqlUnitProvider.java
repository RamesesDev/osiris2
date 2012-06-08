/*
 * Sql.java
 *
 * Created on August 3, 2010, 2:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

/**
 *
 * @author elmo
 */
public abstract class SqlUnitProvider {
    
    private SqlConf conf;
    private SqlDialect dialect;
    
    public abstract String getType();
    
    public abstract SqlUnit getSqlUnit(String name);

    public SqlConf getConf() {
        return conf;
    }

    public void setConf(SqlConf c) {
        this.conf = c;
    }

    public SqlDialect getDialect() {
        return dialect;
    }

    public void setDialect(SqlDialect dialect) {
        this.dialect = dialect;
    }

    
}
