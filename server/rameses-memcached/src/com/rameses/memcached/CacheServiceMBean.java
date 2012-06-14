/*
 * CacheServiceMBean.java
 *
 * Created on August 17, 2011, 9:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.memcached;



/**
 *
 * @author jzamss
 */
public interface CacheServiceMBean 
{    
    /**
     * name = name of object to use to retrieve
     * object = object to store/must be serializable
     * timeout = in milliseconds
     */
    void put(String name, Object object);
    void put(String name, Object object, int timeout);
    Object get(String name);
    void remove(String name);

    void start() throws Exception;
    void stop() throws Exception;
    String showAllCache();
    
    void setScriptHandler(String scriptHandler);
    String getJndiName();
    void setJndiName(String jndiName);
    
    void setHost( String host);
    String getHost();
    void setPort( String port);
    String getPort();
    void setCachePrefix(String cachePrefix);
    String getCachePrefix();
}
