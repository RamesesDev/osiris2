/*
 * DefaultServiceProxy.java
 * Created on September 21, 2011, 9:04 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service;

import java.util.Map;

/**
 *
 * @author jzamss
 */
public class DefaultScriptServiceProxy extends AbstractServiceProxy implements ServiceProxy {
    
    private Map env;
    
    public DefaultScriptServiceProxy(String scriptName, Map conf, Map env) {
        super(scriptName, conf);
        this.env = env;
    }

    public Object invoke(String action, Object[] params) throws Exception {
        Object[] args = new Object[] {
            this.serviceName,
            action,
            params,
            this.env
        };
        return client.post("ejb/ScriptService/local.invoke", args );
    }
    
    public Object invoke(String action) throws Exception {
        return invoke(action, null);
    }
}
