package com.rameses.data.server;

import com.rameses.resource.ResourceProvider;
import com.rameses.jndi.JndiUtil;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.naming.InitialContext;

public class SqlCache implements SqlCacheMBean, Serializable {
    
    private static final String jndiname = "java:/SqlCache";
    
    private Map<String, SqlCacheBean> map = new HashMap<String, SqlCacheBean>();
    
    private ResourceProvider sqlProvider;
    
    public SqlCache() {
    }
    
    public SqlCacheBean get(String name) {
        if( !map.containsKey(name)) {
            InputStream is = null;
            if(sqlProvider!=null) {
                try {
                is = sqlProvider.getResource(name);
                }
                catch(Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            //check if it does not exist in provider, use another.
            if(is==null) {
                String urlPath = "META-INF/sql/" + name;
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                is = loader.getResourceAsStream(urlPath);
            }
            
            if(is !=null)
                map.put( name, SqlParser.parseStatement(is));
            else
                throw new IllegalStateException("Sql resource " + name + " not found ");
        }
        return map.get(name);
    }

    public void flush() {
        map.clear();
    }

    public void start() throws Exception {
        InitialContext ctx = new InitialContext();
        JndiUtil.bind( ctx, jndiname, this );
        Iterator iter = com.sun.jmx.remote.util.Service.providers(ResourceProvider.class, Thread.currentThread().getContextClassLoader());
        //load only the first
        while(iter.hasNext()) {
            ResourceProvider rp = (ResourceProvider)iter.next();
            //if( rp.getNamespace().equals(ResourceProvider.SQLCACHE)) {
            //    sqlProvider = rp;
             //   break;
            //}
        }
    }

    public void stop() throws Exception {
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx, jndiname );
        sqlProvider = null;
    }

    
    
}
