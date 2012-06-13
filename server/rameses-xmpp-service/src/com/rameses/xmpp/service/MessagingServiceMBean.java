/*
 * PrivateMessagingMBean.java
 *
 * Created on June 12, 2012, 10:09 PM
 * @author jaycverg
 */

package com.rameses.xmpp.service;

import java.io.Serializable;
import java.util.Map;

public interface MessagingServiceMBean extends Serializable {

    void start() throws Exception;
    void stop() throws Exception;
    
    void notify(Map request, Object message) throws Exception;
    void broadcast(Object message) throws Exception;
    
    void setHost(String host);
    String getHost();
    void setPort(String port);
    String getPort();
    void setDomain(String domain);
    String getDomain();
    
    void setOriginName(String originName);
    String getOriginName();
    
    void setBroadcastUsername( String username );
    String getBroadcastUsername();
    void setBroadcastPassword( String password);
    String getBroadcastPassword();
    
    void setPrivateUsername( String username );
    String getPrivateUsername();
    void setPrivatePassword( String password);
    String getPrivatePassword();
    
}
