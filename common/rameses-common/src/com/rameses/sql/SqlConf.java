/*
 * SqlConf.java
 *
 * Created on August 14, 2010, 6:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import com.sun.jmx.remote.util.Service;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * configuration for SqlUnitFactory
 */
public class SqlConf {
    
    private SqlUnitResourceProvider resourceProvider = new DefaultSqlUnitResourceProvider();
    private SqlUnitCacheProvider cacheProvider = new DefaultSqlUnitCacheProvider();
    private Map<String, SqlUnitProvider> providers;
    
    
    //provision for other configurations that will be used by other providers.
    private Map extensions = new HashMap();
    
    public SqlConf() {
    }

    public SqlUnitResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    public void setResourceProvider(SqlUnitResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public SqlUnitCacheProvider getCacheProvider() {
        return cacheProvider;
    }

    public void setCacheProvider(SqlUnitCacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }
    
    public Map getSqlUnitProviders() {
        if(providers==null) {
            providers = new Hashtable();
            Iterator iter = Service.providers(  SqlUnitProvider.class, Thread.currentThread().getContextClassLoader());
            while(iter.hasNext()) {
                SqlUnitProvider p = (SqlUnitProvider)iter.next();
                p.setConf(this);
                providers.put( p.getType(), p );
            }
            
            //plug in available providers. so that we can immediately
            //basic sql provider
            SqlUnitProvider s = new com.rameses.sql.SimpleSqlUnitProvider();
            s.setConf( this );
            providers.put( s.getType(), s );
            
            //sqlx
            s = new com.rameses.sql.SqlxSqlUnitProvider();
            s.setConf(this);
            providers.put( s.getType(), s );

            //crud
            s = new com.rameses.sql.CrudSqlUnitProvider();
            s.setConf(this);
            providers.put( s.getType(), s );
            
            //this is to check if we have made new changes.
            if(providers.size()==0)
                throw new RuntimeException("There must be at least one SqlUnitProvider specified");
        }
        return providers;
    }
    
    public void destroy() {
         resourceProvider = null;
         if(cacheProvider!=null) cacheProvider.destroy();
         cacheProvider = null;
         if(providers!=null) providers.clear();
         providers = null;
    }

    public Map getExtensions() {
        return extensions;
    }

    
}
