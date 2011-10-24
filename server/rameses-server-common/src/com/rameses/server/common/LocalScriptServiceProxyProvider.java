/*
 * LocalServiceProxyProvider.java
 * Created on September 21, 2011, 9:55 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.server.common;

import com.rameses.service.ServiceProxy;
import com.rameses.service.ScriptServiceProxyProvider;
import java.util.Map;

public class LocalScriptServiceProxyProvider implements ScriptServiceProxyProvider {
    
    public boolean accept(Map map) {
        String appHost = (String)map.get("app.host");
        if(appHost==null || appHost.indexOf("localhost")>=0) {
            return true;
        }
        else {
            return false;
        }
    }

    public ServiceProxy create(String name, Map env, Map conf) {
        return new LocalScriptServiceProxy(name,conf,env);
    }

    
}
