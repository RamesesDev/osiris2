/*
 * SqlDialect.java
 *
 * Created on April 30, 2012, 9:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

/**
 *
 * @author Elmo
 */
public interface SqlDialect 
{
    String getName();
    String getPagingStatement( String sql, int start, int limit, String [] pagingKeys  );
}
