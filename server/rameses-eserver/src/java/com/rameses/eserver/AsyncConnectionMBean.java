/*
 * AsyncConnectionMBean.java
 *
 * Created on July 24, 2010, 1:35 PM
 * @author jaycverg
 */

package com.rameses.eserver;

import com.rameses.messaging.MessagingConnection;
import java.util.Map;


public interface AsyncConnectionMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    Map getConf();
    MessagingConnection getConnection() throws Exception;
    
    String getJndiName();
    void setJndiName(String jndiName);
    
    String getHost();
    void setHost(String host);
    
    int getPort();
    void setPort(int port);
    
    String getUsername();
    void setUsername(String username);
    
    String getPassword();
    void setPassword(String password);
    
    String getDriverClass();
    void setDriverClass(String driverClass);
    
}
