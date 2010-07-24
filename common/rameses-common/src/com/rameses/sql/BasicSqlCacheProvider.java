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
public class BasicSqlCacheProvider extends SqlCacheProvider {
    
    private Map<String, SqlCache> parsedQueries = new Hashtable();
    
    public SqlCache getSqlCache(String statement)  {
        if( !parsedQueries.containsKey(statement) ) {
            SqlCache sq = createSqlCache(statement);
            parsedQueries.put(statement,sq);
            return sq;
        }
        else {
            return parsedQueries.get(statement);
        }
    }
    
    public SqlCache getNamedSqlCache(String name)  {
        if(!parsedQueries.containsKey(name)) {
            String fileName = "META-INF/sql/" + name;
            if( name.indexOf(".")<0 ) fileName = fileName + ".sql";
            
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            String _sql = getInputStreamToString(is);
            String s = formatText(fileName, _sql);
            SqlCache sq = createSqlCache(s);
            parsedQueries.put(name, sq);
            return sq;
        }
        else {
            return parsedQueries.get(name);
        }
    }

    
    
}
