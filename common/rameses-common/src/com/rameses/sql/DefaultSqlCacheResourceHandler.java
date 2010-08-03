/*
 * BasicSqlQueryCacheProvider.java
 *
 * Created on July 24, 2010, 11:07 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class DefaultSqlCacheResourceHandler implements SqlCacheResourceHandler {
    
    private static Map<String, SqlCache> parsedQueries = new Hashtable();
    
    //this is overridable
    public InputStream getResource(String name ) {
        String fileName = "META-INF/sql/" + name;
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
    
    //this is overridable
    public void storeCache(String key, SqlCache sq) {
        parsedQueries.put(key,sq);
    }
    
    public SqlCache getCache(String key) {
        return parsedQueries.get(key);
    }


}
