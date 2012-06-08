/*
 * ResourceUtil.java
 *
 * Created on January 31, 2011, 1:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.persistence.EntityManager;
import com.rameses.schema.SchemaManager;
import com.rameses.scripting.ScriptManager;
import com.rameses.scripting.ScriptProxyInvocationHandler;
import com.rameses.scripting.ScriptServiceLocal;
import com.rameses.server.common.*;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.util.ExprUtil;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;

/**
 *
 * @author ms
 */
public final class LookupUtil {
    
    private static String DS_CATALOG = "ds.catalog";
    
    public static Object lookupResource(String name) throws Exception {
        Map m = AppContext.getSysMap();
        String resname = ExprUtil.substituteValues(name,m);
        if(resname==null)
            throw new Exception("Resource name must be provided");
        if(!resname.startsWith("java:")) {
            resname = AppContext.getPath() + resname;
        }
        InitialContext ctx = new InitialContext();
        return ctx.lookup(resname);
    }
    
    public static Object lookupService(ScriptServiceLocal scriptService, String name, String hostname, String defaultServiceName, Map env ) throws Exception {
        Map m = AppContext.getSysMap();
        String scriptname = ExprUtil.substituteValues(name,m);
        String host = ExprUtil.substituteValues(hostname,m);
        if(scriptname==null || scriptname.trim().length()==0) scriptname = defaultServiceName;
        
        ScriptProxyInvocationHandler handler = null;
        if(host==null || host.trim().length()==0) {
            handler = new ScriptProxyInvocationHandler(scriptService,scriptname,env);
        } else {
            throw new RuntimeException("Remote proxy not yet handled");
            //if(scriptname==null || scriptname.trim().length()==0)
            //    throw new IllegalStateException("Please provide a remote service name value for @Service");
            //return mbean.createRemoteProxy(scriptname, env, host );
        }
        return ScriptManager.getInstance().createProxy( scriptname, handler );
    }
    
    public static Object lookupSqlContext(String name) throws Exception {
        return lookupSqlContext(name, null);
    }
    
    public static Object lookupSqlContext(String name, Map env) throws Exception {
        if( env == null ) env = new HashMap();
        Map m = AppContext.getSysMap();
        String dsName =ExprUtil.substituteValues(name, m);
        SqlContext sctx = null;
        if(dsName!=null && dsName.trim().length()>0) {
            sctx = SqlManager.getInstance().createContext( AppContext.lookupDs(dsName, env) );
        } else {
            sctx = SqlManager.getInstance().createContext();
        }
        
         //LOAD THE DIALECT. 
        String sqlDialect = AppContext.getDialect(dsName, env);
        if( sqlDialect !=null ) sctx.setDialect( sqlDialect );
        
        //set catalog only if the ds name is not system
        if( env.containsKey(DS_CATALOG) && !dsName.endsWith("system")) {
            sctx.setCatalog( (String)env.get(DS_CATALOG)  );    
        }
        
        return sctx;
    }
    
    public static Object lookupPersistenceContext(String name) throws Exception {
        return lookupPersistenceContext(name, null);
    }
    
    public static Object lookupPersistenceContext(String name, Map env) throws Exception {
        if( env == null ) env = new HashMap();
        Map m = AppContext.getSysMap();
        String dsName = ExprUtil.substituteValues(name, m);
        SqlContext sqlContext = null;
        if(dsName!=null && dsName.trim().length()>0) {
            sqlContext = SqlManager.getInstance().createContext( AppContext.lookupDs(dsName, env) );
        } else {
            sqlContext = SqlManager.getInstance().createContext();
        }
        //set catalog only if the ds name is not system
        if( env.containsKey(DS_CATALOG) && !dsName.endsWith("system")) {
            sqlContext.setCatalog( (String)env.get(DS_CATALOG)  );    
        }
        //LOAD THE DIALECT. 
        String sqlDialect = AppContext.getDialect(dsName, env);
        if( sqlDialect !=null ) sqlContext.setDialect( sqlDialect );
        
        return new EntityManager( SchemaManager.getInstance(),sqlContext);
    }
    
    
    public static class ResourceUtil {
        public Object create(String name) throws Exception {
            return lookupResource(name);
        }
    }
    
    public static class ServiceUtil {
        private ScriptServiceLocal scriptService;
        private Map env;
        
        public ServiceUtil(ScriptServiceLocal local, Map env) {
            this.scriptService = local;
            this.env = env;
        }
        public Object create(String name) throws Exception {
            return lookupService(scriptService, name, null, null, env );
        }
        public Object create(String name, String host) throws Exception {
            return lookupService(scriptService, name, host, null, env );
        }
    }
    
    public static class SqlContextUtil {
        public Object create(String name) throws Exception {
            return lookupSqlContext(name, null);
        }
    }
    
    public static class PersistenceUtil {
        public Object create(String name) throws Exception {
            return lookupPersistenceContext(name, null);
        }
    }
    
    
}
