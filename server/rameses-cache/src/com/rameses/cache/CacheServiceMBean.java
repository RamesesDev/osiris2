/*
 * CacheServiceMBean.java
 *
 * Created on August 17, 2011, 9:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cache;

import com.rameses.server.cluster.ClusterServiceMBean;

/**
 *
 * @author jzamss
 */
public interface CacheServiceMBean {
    /*
     * name = name of object to use to retrieve
     * object = object to store/must be serializable
     * timeout = in milliseconds
     */
    void put(String name, Object object);
    void put(String name, Object object, int timeout);
    void put(String name, Object object, int timeout, String updateMode);
    void put(String name, Object object, String updateMode);
    void update(String name, Object object);
    Object get(String name);
    void remove(String name);

    //the ff.methods are provided to avoid recursive calls. This called by the remote hosts.
    Object getLocalContent(String name);
    
    //timedout is true if cache destroyed due to timeout
    void removeLocalCache(String name, boolean timedout);
    void updateLocalCache(String name, Object info);
    
    
    void start() throws Exception;
    void stop() throws Exception;
    String showAllCache();
    void setCluster(ClusterServiceMBean cluster);
    
    void setScriptHandler(String scriptHandler);
}
