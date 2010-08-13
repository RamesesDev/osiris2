/*
 * AsyncResponseDelegate.java
 *
 * Created on August 10, 2010, 12:01 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import java.util.Map;


public class AsyncResponseDelegate {
    
    private static final String SERVICE_NAME = "AsyncResponseService";
    private HttpInvokerClient client;
    
    public Object getPollData(String reqId) throws Exception {
        return client.invoke(SERVICE_NAME +".getPollData", new Object[]{ reqId });
    }
    
    public AsyncResponseDelegate(String host)  {
        Map env = OsirisContext.getSession().getEnv();
        client = HttpClientManager.getInstance().getService(host, env);
    }

}
