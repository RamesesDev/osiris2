/*
 * MySqlDialect.java
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
public class MySqlDialect implements SqlDialect {
    
    public String getPagingStatement(String sql, int start, int limit) {
        return sql + " LIMIT " + start + "," + limit;
    }
    
    
}
