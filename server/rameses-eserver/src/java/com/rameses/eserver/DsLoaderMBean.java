/*
 * DsLoaderMBean.java
 *
 * Created on August 7, 2010, 1:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

public interface DsLoaderMBean {
    
    void start() throws Exception ;
    void stop() throws Exception;
    void deploy( String name ) throws Exception;
    void undeploy( String name ) throws Exception;
    
    String getDeployPath();
    void setDeployPath(String deployPath);
    
}
