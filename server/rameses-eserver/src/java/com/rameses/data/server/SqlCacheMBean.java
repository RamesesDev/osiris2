/*
 * SqlCacheMgmt.java
 *
 * Created on May 7, 2009, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.data.server;

public interface SqlCacheMBean {
    
    SqlCacheBean get(String name); 
    void flush();
    void start() throws Exception;
    void stop() throws Exception;

}
