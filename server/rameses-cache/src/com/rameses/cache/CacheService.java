/*
 * CacheService.java
 * Created on August 17, 2011, 9:27 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import com.rameses.server.cluster.ClusterServiceMBean;
import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.sun.jmx.remote.util.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author emn
 */
public class CacheService implements CacheServiceMBean,Serializable,Runnable {
    
    private ConcurrentHashMap<String, CacheUnit> cache = new ConcurrentHashMap();
    private ScheduledExecutorService cleaner;
    private ClusterServiceMBean cluster;
    
    private Map<String, CacheUpdateHandler> updateHandlers = new HashMap();
    
    //script cache listener
    private String scriptHandler;
    
    private int remoteTimeout = 60000;
    
    public void start() throws Exception {
        System.out.println("STARTING CACHE SERVICE");
        System.out.println("LOADING cache: "+AppContext.getPath()+ CacheService.class.getSimpleName());
        
        InitialContext ictx = new InitialContext();
        JndiUtil.bind( ictx, AppContext.getPath()+ CacheService.class.getSimpleName(), this );
        
        //load the update handlers
        updateHandlers.put("default", new DefaultCacheUpdateHandler());
        Iterator iter = Service.providers(CacheUpdateHandler.class, this.getClass().getClassLoader());
        while(iter.hasNext()) {
            CacheUpdateHandler ch = (CacheUpdateHandler)iter.next();
            updateHandlers.put(ch.getMode(), ch );
        }
        cleaner = Executors.newScheduledThreadPool(1);
        cleaner.scheduleWithFixedDelay(this,10,10,TimeUnit.SECONDS);
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING CACHE SERVICE");
        //remove all caches for this server
        String host = cluster.getCurrentHostName();
        getSqlContext().createNamedExecutor("cache:remove-host").setParameter(1, host).execute();
        
        cleaner.shutdown();
        cache.clear();
        updateHandlers.clear();
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind( ictx, AppContext.getPath()+ CacheService.class.getSimpleName() );
    }
    
    private SqlContext getSqlContext() {
        DataSource ds = AppContext.getSystemDs();
        SqlContext ctx = SqlManager.getInstance().createContext(ds);
        ctx.setDialect(AppContext.getDialect("system", null));
        return ctx;
    }
    
    //basic timeout is 1 minute
    public void put(String name, Object object) {
        put(name, object, 60000, "default");
    }
    
    public void put(String name, Object object, int timeout) {
        put(name, object, timeout, "default");
    }
    
    public void put(String name, Object object, String updateMode) {
        put(name, object, 60000, updateMode);
    }
    
    public void put(String name, Object object, int timeout, String updateMode) {
        //before putting cache, check the database first if the cache already exists.
        //do this by testing to insert it.
        try {
            String host = cluster.getCurrentHostName();
            getSqlContext().createNamedExecutor("cache:add-cache").setParameter(1, name).setParameter(2,host).execute();
            CacheUpdateHandler handler = this.updateHandlers.get(updateMode);
            LocalCacheUnit cu = new LocalCacheUnit(name, object, handler);
            if(this.scriptHandler!=null) {
                cu.setListener( new ScriptCacheListener(this.scriptHandler) );
            }
            cu.setTimeout( timeout );
            cache.put(name,cu);
        }
        catch(Exception e) {
            System.out.println("CacheService.put error. cache already exists. " +e.getMessage());
        }
    }
    
    private CacheUnit findCacheUnit(String name) {
        CacheUnit cu = cache.get(name);
        
        //if it does not exist in local cache, check the database.
        if( cu == null ) {
            try {
                //find the cache in database
               Map map = (Map)getSqlContext().createNamedQuery("cache:find-cache-host").setParameter(1,name).getSingleResult();
               if(map!=null && map.get("host")!=null) {
                   String sHost = (String)map.get("host");
                   Map remoteConf = cluster.findHost(sHost);
                   cu = new RemoteCacheUnit(name,remoteConf);
                   cu.setTimeout( remoteTimeout );
                   cache.put( name, cu );
               }
            }
            catch(Exception e) {
                System.out.println("find cache error "+e.getMessage());
                //e.printStackTrace();
            }
        }
        return cu;
    }
    
    public Object get(String name) {
        try {
            CacheUnit cu = findCacheUnit(name);
            if(cu==null) return null;
            return cu.getContent();
        } 
        catch(RemoteCacheLostException e) {
            cache.remove(name);
            return null;
        }
    }
    
    
    //return only if it is local cache to avoid recursion.
    public Object getLocalContent(String name) {
        CacheUnit cu = cache.get(name);
        if(cu!=null && (cu instanceof LocalCacheUnit)) {
            return cu.getContent();
        }
        return null;
    }
    
    /**
     *1.remove the cache in db
     *2.scan all clusters and call remove local on each 
     */
    public void remove(String name ) {
        _remove( name, false );
    }
    
    private void _remove(String name, boolean timedout) {
        try {
            getSqlContext().createNamedExecutor("cache:remove-cache").setParameter(1, name).execute();
        }
        catch(Exception e){;}
        //remove first local cache
        removeLocalCache(name, timedout );
        callRemoteHosts(name, new RemoveCacheCallable(timedout));
    }
    
    
    //upon calling destroy, the cache listener is triggered
    public void removeLocalCache(String name, boolean timedout) {
        CacheUnit cu = cache.remove(name);
        if( cu !=null && (cu instanceof LocalCacheUnit) ) {
            ((LocalCacheUnit)cu).destroy(timedout);
        }
    }
    
    
    public void run() {
        Iterator<String> iter = cache.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            CacheUnit cu = cache.get(key);
            if(cu.isExpired()) {
                _remove(key, true);
            }
        }
    }
    
    public String showAllCache() {
        StringBuffer sb = new StringBuffer();
        sb.append("<b>Caches</b><br>");
        Iterator<String> iter = cache.keySet().iterator();
        while(iter.hasNext()) {
            String k = iter.next();
            CacheUnit cu = cache.get(k);
            sb.append( cu.toString() + "<br>");
        }
        return sb.toString();
    }
    
    
    public void setCluster(ClusterServiceMBean cluster) {
        this.cluster = cluster;
    }
    
    //this method assumes that cache proxy has been created.
    public void update(String name, Object info) {
        CacheUnit cu = findCacheUnit(name);
        if(cu!=null) {
            cu.updateContent( info );
        }
    }
    
    public void updateLocalCache(String name, Object info) {
        CacheUnit cu = cache.get(name);
        if(cu!=null && (cu instanceof LocalCacheUnit) ) {
            cu.updateContent( info );
        } else
            throw new RuntimeException("Cache " +  name  + " does not exist!");
    }
    
    private Object callRemoteHosts(String name, CacheCallableProvider provider) {
        //attempt to scan all servers in cluster and check if cache exists somewhere.
        //test each host by creating a Cacheproxy. the one that succeeds will be stored
        ExecutorService svc = Executors.newCachedThreadPool();
        List<Future> futures = new ArrayList();
        Iterator<Map> iter = cluster.getRemoteHosts().values().iterator();
        while(iter.hasNext()) {
            futures.add( svc.submit( provider.getCallable(name, iter.next()) ) );
        }
        
        //break if there is a return value.
        for(Future f: futures) {
            try {
                Object result = f.get(3000, TimeUnit.MILLISECONDS);
                if(result!=null) {
                    return result;
                }
            } catch(Exception ign){;}
        }
        return null;
    }
    
    public void setScriptHandler(String scriptHandler) {
        this.scriptHandler = scriptHandler;
    }
    
    
    
    
    
}
