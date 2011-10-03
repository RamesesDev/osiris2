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
public class DefaultEJBServiceProxy extends AbstractServiceProxy implements ServiceProxy {
    
    public DefaultEJBServiceProxy(String serviceName, Map conf) {
        super(serviceName, conf);
   }

    public Object invoke(String action, Object[] params) throws Exception {
        return client.post("ejb/"+this.serviceName+"."+action, params );
    }
    
    public Object invoke(String action) throws Exception {
        return invoke(action, null);
    }
}
