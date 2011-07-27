/*
 * AsyncServiceMBean.java
 *
 * Created on July 20, 2011, 8:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

/**
 *
 * @author jzamss
 */
public interface AsyncMessagingServiceMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    void sendMessage(String message);
    
    void setName(String name);
    String getName();
    
    void setHost(String host);
    String getHost();
    void setAppContext(String appContext);
    String getAppContext();
    void setScriptName(String name);
    String getScriptName();
    void setMethod(String method);
    String getMethod();
    
    void pause() throws Exception;
    void resume() throws Exception;
    
}
