package com.rameses.osiris2.client;

import com.rameses.osiris2.*;
import com.rameses.rcp.framework.ClientContext;

public final class OsirisContext {
    
    private static SessionContext session;
    private static AsyncSystemConnection conMgr = new AsyncSystemConnection();
    
    public static void setSession(SessionContext aApplication) {
        session = aApplication;
    }
    
    public static SessionContext getSession() {
        return session;
    }
    
    public static ClientContext getClientContext() {
        return ClientContext.getCurrentContext();
    }

    public static AsyncSystemConnection getAsyncConnection() {
        return conMgr;
    }

}
