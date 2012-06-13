/*
 * CacheServiceMBean.java
 *
 * Created on August 17, 2011, 9:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cache;



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
    void update(String name, Object object);
    Object get(String name);
    void remove(String name);

    void start() throws Exception;
    void stop() throws Exception;
    String showAllCache();
    
    void setScriptHandler(String scriptHandler);
}
