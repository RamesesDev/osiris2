/*
 * AsyncResponse.java
 *
 * Created on August 9, 2010, 5:26 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import java.util.Map;
import org.apache.commons.beanutils.MethodUtils;


public class AsyncResponse {
    
    private Object bean;
    private String respHandler;
    private String reqId;
    private String host;
    
    
    public AsyncResponse(Object bean, String respHandler, String reqId, String host) {
        this.bean = bean;
        this.respHandler = respHandler;
        this.reqId = reqId;
        this.host = host;
    }
    
    public void onResponse(Map data) {
        System.out.println("firing response....");
        try {
            Map env = OsirisContext.getSession().getEnv();
            HttpInvokerClient client = HttpClientManager.getInstance().getService(host, env);
            Object obj = client.invoke("ResponseService.getPushData", new Object[]{ data.get("pushId") });
            if ( obj != null ) {
                invokeMethod(bean, respHandler, new Object[]{ obj }, null);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private Object invokeMethod(Object xbean, String xaction, Object[] args, Class[] params ) throws Exception {
        if(params==null)
            return MethodUtils.invokeMethod(xbean, xaction, args);
        else
            return MethodUtils.invokeMethod(xbean, xaction, args, params);
    }
    
}
