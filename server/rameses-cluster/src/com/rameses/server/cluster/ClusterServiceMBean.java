/*
 * ClusterService.java
 *
 * Created on September 18, 2011, 8:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.server.cluster;

import java.io.Serializable;
import java.util.Map;

public interface ClusterServiceMBean extends Serializable {
    
    void start() throws Exception;
    void stop() throws Exception;
    
    //called by other clusters
    void addRemoteHost(String name, String context, String host);

    //called by other clusters
    void removeRemoteHost(String name);
    
    String getCurrentHostName();
    String getCurrentHost();
    String getCurrentContext();
    Map findHost(String name);
    void setHostName(String n);
    
    //returns a map of name-host keys
    Map<String,Map> getRemoteHosts();
    
}
