/*
 * NotificationServerMBean.java
 * Created on August 3, 2011, 7:49 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver;

/**
 *
 * @author jzamss
 */
public interface NotificationServerMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    void addHost(String host);
    void removeHost(String host);
    
    //this should return a token string for successful reconnects
    String register(String sessionId);
    
    void unregister(String sessionId);
    
    Object poll(String sessionid, String tokenid);
    void signal(String sessionid, Object message);
    void signal(String sessionid, Object message, boolean broadcast);
    
    void setPort(String port);
    String getPort();
    void setProtocol(String protocol);
    String getProtocol();
    String getHost();
    
}
