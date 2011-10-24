/*
 * LocalServiceProxy.java
 * Created on September 21, 2011, 9:57 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.server.common;

import com.rameses.service.ServiceProxy;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class LocalScriptServiceProxy implements ServiceProxy {
    
    private String scriptName;
    private ServiceWrapper wrapper;
    private Map env;

    public LocalScriptServiceProxy(String scriptName, Map conf, Map env) {
        this.scriptName = scriptName;
        String appContext = (String)conf.get("app.context");
        String service = "ScriptService/local";
        if( appContext!=null && appContext.trim().length()>0) {
            service = appContext + "/" + service;
        }
        this.env = env;
        if(this.env == null) this.env = new HashMap();
        wrapper = new ServiceWrapper(service);
    }
    
    public Object invoke(String action, Object[] params) throws Exception {
        Object[] args = new Object[]{
            scriptName,
            action,
            params,
            this.env
        };
        return wrapper.invoke("invoke",args);
    }
    
    public Object invoke(String action) throws Exception {
        return invoke(action, null);
    }
    
}
