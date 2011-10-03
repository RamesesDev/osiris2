/*
 * AbstractServiceProxy.java
 * Created on September 22, 2011, 5:39 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service;

import com.rameses.http.HttpClient;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public abstract class AbstractServiceProxy {
    
    protected HttpClient client;
    protected String serviceName;
    
    /** Creates a new instance of AbstractServiceProxy */
    public AbstractServiceProxy(String serviceName, Map conf){
        String appContext = (String)conf.get("app.context");
        String host = (String)conf.get("app.host");
        if(host==null) host = "localhost:8080";
        client = new HttpClient(host, true);
        client.setAppContext(appContext);
        this.serviceName = serviceName;
        
        //settings 
        String _readTimeout = (String)conf.get("readTimeout");
        if(_readTimeout!=null) {
            int rt = 0;
            try {
                rt = Integer.parseInt(_readTimeout+"");
            }catch(Exception e){;}
            client.setReadTimeout( rt );
        }
        
    }
    
}
