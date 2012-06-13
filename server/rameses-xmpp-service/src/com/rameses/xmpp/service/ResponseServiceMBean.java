/*
 * ResponseServiceMBean.java
 *
 * Created on June 13, 2012, 9:50 AM
 * @author jaycverg
 */

package com.rameses.xmpp.service;

import java.io.Serializable;
import java.util.Map;


public interface ResponseServiceMBean extends Serializable {
    
    void start() throws Exception;
    void stop() throws Exception;
    
    void sendResponse(Map request, Object message) throws Exception;
    
    String getRemoteHost();
    void setRemoteHost(String host);
    String getRemoteContext();
    void setRemoteContext(String context);
    String getRemoteServiceName();
    void setRemoteServiceName(String serviceName);
    
}
