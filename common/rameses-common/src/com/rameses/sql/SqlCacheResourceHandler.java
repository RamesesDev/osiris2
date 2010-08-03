/*
 * SqlQueryCache.java
 *
 * Created on July 24, 2010, 11:03 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.InputStream;

/***
 * .sqlx has a special parsing for insert statements.
 * This is used because of the difficulty of producing
 * sql text with several columns.
 * <br>
 * Basic format as follows:
 * <br>
 * insert into table values (firstname=$P{firstname}, lastname=$P{lastname})
 *
 */
public interface SqlCacheResourceHandler {
    InputStream getResource(String name );
    void storeCache(String key, SqlCache sq);
    SqlCache getCache(String key);
}
