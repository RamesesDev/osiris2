/*
 * LocalServiceProxyProvider.java
 * Created on September 21, 2011, 9:55 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.server.common;

import com.rameses.service.EJBServiceProxyProvider;
import com.rameses.service.ServiceProxy;
import java.util.Map;

public class LocalEJBServiceProxyProvider implements EJBServiceProxyProvider {
    
    
    public boolean accept(Map map) {
        String appHost = (String)map.get("app.host");
        if(appHost==null || appHost.indexOf("localhost")>=0) {
            return true;
        }
        else {
            return false;
        }
    }

    public ServiceProxy create(String name,Map conf) {
        return new LocalEJBServiceProxy(name,conf);
    }

    
}
