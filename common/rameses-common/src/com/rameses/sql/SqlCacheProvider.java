/*
 * SqlCacheProvider.java
 *
 * Created on August 3, 2010, 2:45 PM
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
public abstract class SqlCacheProvider {
    
    private SqlCacheResourceHandler cacheResourceHandler;
    
    public void setSqlCacheResourceHandler(SqlCacheResourceHandler cacheResource) {
        this.cacheResourceHandler = cacheResource;
    }

    public SqlCacheResourceHandler getSqlCacheResourceHandler() {
        return this.cacheResourceHandler;
    }
    
    public abstract boolean accept( String name );
    public abstract SqlCache createSqlCache(String name);
    
    //utiltiy class
    protected String getInputStreamToString(InputStream is) {
        try {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            while((i=is.read())!=-1) {
                sb.append((char)i);
            }
            return sb.toString();
            
        } catch(Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try {is.close();} catch(Exception ign){;}
        }        
    }
    
}
