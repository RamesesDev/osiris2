/*
 * NotificationServerMBean.java
 * Created on August 3, 2011, 7:49 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.server.session;

import com.rameses.server.cluster.ClusterServiceMBean;

/**
 *
 * @author jzamss
 */
public interface SessionServiceMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    
    //this should return a token string for successful reconnects
    String register(String username, Object info);
    
    //destroy the session
    Object destroy(String sessionId);
    
    Object poll(String sessionid, String tokenid);
    boolean push(String sessionid, String tokenid, Object msg);

    Object getInfo(String sessionid);
    
    void setCluster(ClusterServiceMBean cluster);
    
}
