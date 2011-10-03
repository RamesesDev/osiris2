/*
 * SessionClient.java
 * Created on September 28, 2011, 12:02 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.client.session;

import com.rameses.service.EJBServiceContext;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class SessionClient {
    
    public interface SessionClientInvoker {
        String register(String username, Object info);
        void destroy(String sessionId);
        boolean push(String sessionid, String tokenid, Object msg);
        Object getInfo(String sessionid);
    }
    
    private SessionClientInvoker invoker;
    
    public SessionClient(Map conf) {
        EJBServiceContext ctx  = new EJBServiceContext(conf);
        invoker = ctx.create("SessionService", SessionClientInvoker.class);
    }
    
    public SessionClientInvoker getInvoker() {
        return invoker;
    }
    
}
