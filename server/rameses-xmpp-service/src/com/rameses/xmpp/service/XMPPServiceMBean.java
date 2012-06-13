
package com.rameses.xmpp.service;

import java.io.Serializable;


public interface XMPPServiceMBean extends Serializable
{    
    void start() throws Exception;
    void stop() throws Exception;
    void send(String username, Object message) throws Exception;
    
    void setHost(String host);
    String getHost();
    void setPort(String port);
    String getPort();
    void setDomain(String domain);
    String getDomain();
    void setUsername( String username );
    String getUsername();
    void setPassword( String password);
    String getPassword();
    
}
