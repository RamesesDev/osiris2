/*
 * MySqlDialect.java
 *
 * Created on April 30, 2012, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql.dialect;

import com.rameses.sql.CrudSqlBuilder;
import com.rameses.sql.SqlDialect;
import com.rameses.sql.dialect.mysql.MySqlCrudSqlBuilder;

/**
 *
 * @author Elmo
 */
public class PostgreSqlDialect implements SqlDialect {
    
    public String getName() {
        return "pgsql";
    }
    
    public String getPagingStatement(String sql, int start, int limit, String[] pagingKeys) {
        return sql + " LIMIT " + limit + " OFFSET " + limit;
    }

    public CrudSqlBuilder createCrudSqlBuilder() {
        return new MySqlCrudSqlBuilder();
    }
    
}
