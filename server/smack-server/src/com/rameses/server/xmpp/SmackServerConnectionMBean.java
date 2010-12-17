/*
 * SmackServerConnectionMBean.java
 *
 * Created on December 17, 2010, 11:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.server.xmpp;

import com.rameses.messaging.Message;
import java.util.Map;

/**
 *
 * @author ms
 */
public interface SmackServerConnectionMBean {
    
    void setJndiName(String name);
    String getJndiName();
    
    void setUsername(String name);
    String getUsername();
    void setHost(String host);
    String getHost();
    void setPort(int port);
    int getPort();
    void setPassword( String pwd);
    
    void start() throws Exception;
    void stop() throws Exception ;
    void send( Message message );
    
    
}
