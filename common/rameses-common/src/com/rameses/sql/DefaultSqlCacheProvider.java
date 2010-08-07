/*
 * SimpleSqlCacheProvider.java
 *
 * Created on August 3, 2010, 2:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.InputStream;

/**
 *
 * @author elmo
 */
public class DefaultSqlCacheProvider extends SqlCacheProvider {
    
    /** Creates a new instance of SimpleSqlCacheProvider */
    public DefaultSqlCacheProvider() {
    }
    
    public boolean accept(String name) {
        return name.endsWith(".sql");
    }

    public SqlCache createSqlCache(String name) {
        InputStream is = this.getSqlCacheResourceHandler().getResource( name );
        if( is == null )
            throw new IllegalStateException( "getNamedSqlCache error. Resource [" + name + "] does not exist");
        String txt = getInputStreamToString(is);
        return new SqlCache(txt);
    }
    
    

}
