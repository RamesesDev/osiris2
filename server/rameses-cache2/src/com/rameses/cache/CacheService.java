/*
 * CacheService.java
 * Created on August 17, 2011, 9:27 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;


import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.naming.InitialContext;

/**
 *
 * @author emn
 */
public class CacheService implements CacheServiceMBean,Serializable,Runnable {
    
    private Map<String, CacheUnit> cache = new Hashtable();
    private ScheduledExecutorService cleaner;
   
    //script cache listener
    private String scriptHandler;
    
    private int remoteTimeout = 60000;
    
    public void start() throws Exception {
        System.out.println("STARTING CACHE SERVICE");
        System.out.println("LOADING cache: "+AppContext.getPath()+ CacheService.class.getSimpleName());
        
        InitialContext ictx = new InitialContext();
        JndiUtil.bind( ictx, AppContext.getPath()+ CacheService.class.getSimpleName(), this );
        
        cleaner = Executors.newScheduledThreadPool(1);
        cleaner.scheduleWithFixedDelay(this,10,10,TimeUnit.SECONDS);
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING CACHE SERVICE");
        //remove all caches for this server
        
        cleaner.shutdown();
        cache.clear();
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind( ictx, AppContext.getPath()+ CacheService.class.getSimpleName() );
    }
    
   
    //basic timeout is 1 minute
    public void put(String name, Object object) {
        put(name, object, 60000);
    }
    
    public void put(String name, Object object, int timeout) {
        //before putting cache, check the database first if the cache already exists.
        //do this by testing to insert it.
        try {
            CacheUnit cu = new CacheUnit(name);
            cu.setContent( object );
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
    
    public Object get(String name) {
        CacheUnit cu = cache.get(name);
        if(cu==null) return null;
        return cu.getContent();
    }
    
    
    
    /**
     *1.remove the cache in db
     *2.scan all clusters and call remove local on each 
     */
    public void remove(String name ) {
        CacheUnit cu = cache.remove(name);
        if( cu !=null ) {
            cu.destroy(false);
        }
    }
    
    public void run() {
        List<CacheUnit> forRemoval = new ArrayList();
        Iterator<String> iter = cache.keySet().iterator();

        while(iter.hasNext()) {
            String key = iter.next();
            CacheUnit cu = cache.get(key);
            if(cu.isExpired()) {
                forRemoval.add( cu );
            }
        }
        for(CacheUnit cu: forRemoval) {
            cache.remove( cu.getName() );
            cu.destroy(true);
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
    
    
    //this method assumes that cache proxy has been created.
    public void update(String name, Object info) {
        CacheUnit cu = cache.get(name);
        if(cu!=null) {
            cu.updateContent( info );
        }
    }
    
    
    public void setScriptHandler(String scriptHandler) {
        this.scriptHandler = scriptHandler;
    }
    
    
    
    
    
}
