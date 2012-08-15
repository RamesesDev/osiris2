/*
 * CacheService.java
 * Created on August 17, 2011, 9:27 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.memcached;


import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import com.rameses.util.ExceptionManager;
import java.io.Serializable;
import javax.naming.InitialContext;

/**
 *
 * @author emn
 */
public class CacheService implements CacheServiceMBean, Serializable {

    private static final int DEFAULT_TIMEOUT = 60;
    
    //script cache listener
    private String scriptHandler;
    private String jndiName = CacheService.class.getSimpleName();
    
    private String host;
    private String port;
    private String cachePrefix;
    
    public void start() throws Exception {
        System.out.println("STARTING CACHE SERVICE");
        System.out.println("LOADING cache: "+AppContext.getPath() + jndiName);
        
        CacheManager.getInstance().init(host, port);
        
        InitialContext ictx = new InitialContext();
        JndiUtil.bind( ictx, AppContext.getPath() + jndiName, this );
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING CACHE SERVICE");
        try{
            CacheManager.getInstance().getClient().shutdown();
        }catch(Exception ex){}
        
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind( ictx, AppContext.getPath() + jndiName );
    }
    

    public void put(String name, Object object) {
        try {
            name = getResolvedKey(name);
            CacheManager.getInstance().getClient().set(name, DEFAULT_TIMEOUT, object);
        }
        catch(Exception e) {
            Exception orig = ExceptionManager.getOriginal(e);
            System.out.println("Failed to put the object: " + e.getMessage());
        }
    }
    
    public void put(String name, Object object, int timeout) {
        try {
            timeout = timeout / 1000;
            name = getResolvedKey(name);
            CacheManager.getInstance().getClient().set(name, timeout, object);
        } 
        catch(Exception e) {
            Exception orig = ExceptionManager.getOriginal(e);
            System.out.println("Failed to put the object: " + e.getMessage());
        }
    }
    
    public Object get(String name) {
        try {
            name = getResolvedKey(name);
            return CacheManager.getInstance().getClient().get(name);
        }
        catch(Exception e) {
            Exception orig = ExceptionManager.getOriginal(e);
            System.out.println("Failed to get the object: " + e.getMessage());
        }
        return null;
    }

    public void remove(String name) {
        try {
            name = getResolvedKey(name);
            CacheManager.getInstance().getClient().delete(name);
        }
        catch(Exception e) {
            Exception orig = ExceptionManager.getOriginal(e);
            System.out.println("Failed to remove the object: " + e.getMessage());
        }
    }

    public String showAllCache() {
        return null;
    }
    
    private String getResolvedKey(String key) {
        if( cachePrefix != null ) 
            return cachePrefix + ":" + key;
        
        return key;
    }

    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setScriptHandler(String scriptHandler) {
        this.scriptHandler = scriptHandler;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }
    //</editor-fold>
    
}
