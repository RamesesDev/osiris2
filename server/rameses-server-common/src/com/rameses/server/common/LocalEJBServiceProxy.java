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
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class LocalEJBServiceProxy implements ServiceProxy {
    
    private String serviceName;
    private ServiceWrapper wrapper;
    
    public LocalEJBServiceProxy(String serviceName, Map map) {
        String appContext = (String)map.get("app.context");
        if( appContext!=null && appContext.trim().length()>0) {
            this.serviceName = appContext + "/" + serviceName;
        }
        wrapper = new ServiceWrapper(this.serviceName);
    }
    
    public Object invoke(String action, Object[] params) throws Exception {
        return wrapper.invoke(action,params);
    }
    
    public Object invoke(String action) throws Exception {
        return wrapper.invoke(action,null);
    }
    
    public String getServiceName() {
        return this.serviceName;
    }
    
}
