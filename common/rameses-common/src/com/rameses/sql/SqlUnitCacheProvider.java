/*
 * SqlUnitCacheProvider.java
 *
 * Created on August 14, 2010, 6:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

/**
 *
 * @author elmo
 */
public interface SqlUnitCacheProvider {
    void putCache(String key, SqlUnit unit);
    SqlUnit getCache(String key);
    void destroy();
}
