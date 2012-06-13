/*
 * CacheManager.java
 *
 * Created on June 13, 2012, 2:21 PM
 * @author jaycverg
 */

package com.rameses.memcached;

import java.net.InetSocketAddress;
import net.spy.memcached.MemcachedClient;


public class CacheManager {
    
    private static CacheManager instance;
    
    public static CacheManager getInstance() {
        if( instance != null ) return instance;
        return (instance = new CacheManager());
    }
    
    
    private MemcachedClient client;
        
    public CacheManager() {}
    
    public void init(String host, String port) throws Exception{
       client = new MemcachedClient(new InetSocketAddress(host, Integer.parseInt(port)));
    }
    
    public MemcachedClient getClient() {
        return client;
    }
   
    
}
