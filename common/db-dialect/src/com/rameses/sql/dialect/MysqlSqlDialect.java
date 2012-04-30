/*
 * MysqlSqlDialect.java
 *
 * Created on April 30, 2012, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql.dialect;

import com.rameses.sql.SqlDialect;

/**
 *
 * @author Elmo
 */
public class MysqlSqlDialect implements SqlDialect {
    
    public String getPagingStatement(String sql, int start, int limit) {
        String s = sql + " LIMIT " + start + "," + limit;
        System.out.println("statement is " + s);
        return s;
    }
    
    
}
