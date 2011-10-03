/*
 * NotifierConnection.java
 * Created on September 28, 2011, 12:14 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.client.session;

import java.util.Map;

/**
 *
 * @author jzamss
 */
public class NotifierConnection {
   
    private Map conf;
    
    public NotifierConnection(Map conf) {
        this.conf = conf;
    }
    
    public Notifier createConnection(String username, Object info) {
        String sessionid = (new SessionClient(conf)).getInvoker().register(username, info);
        HttpNotificationServiceProvider hs = new HttpNotificationServiceProvider(this.conf);
        return new Notifier(sessionid,hs);
    }
}
