/*
 * SessionPoller.java
 * Created on September 26, 2011, 4:38 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.client.session;

import com.rameses.http.HttpClient;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class HttpNotificationServiceProvider implements NotificationServiceProvider,Serializable {
    
    private HttpClient client;
    
    public HttpNotificationServiceProvider(String host, String appContext) {
        client = new HttpClient(host);
        client.setAppContext(appContext);        
        client.setReadTimeout(-1);
    }
    
    public HttpNotificationServiceProvider(Map conf) {
        String host = (String) conf.get("app.host");
        if(host==null) host = "localhost:8080";
        client = new HttpClient(host,true);
        if(conf.containsKey("app.context")) {
            String appContext = (String)conf.get("app.context");
            client.setAppContext(appContext);        
        }
        client.setReadTimeout(-1);
    }

    public Object poll(String sessionid, String tokenid) {
        try {
            Map param = new HashMap();
            param.put( "sessionid", sessionid );
            param.put( "tokenid", tokenid);
            return client.post("poll", param );
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    
}
