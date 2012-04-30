/*
 * SqlDialect.java
 *
 * Created on April 30, 2012, 9:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.util.List;

/**
 *
 * @author Elmo
 */
public interface SqlDialect {
    String getPagingStatement( String sql, int start, int limit  );
}
