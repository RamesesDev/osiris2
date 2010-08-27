package com.rameses.osiris2.client;

import com.rameses.osiris2.*;
import com.rameses.rcp.framework.ClientContext;
import java.util.Map;

public final class OsirisContext {
    
    private static SessionContext session;
    private static Osiris2MainWindowListener mainWindowListener;
    
    public static void setSession(SessionContext aApplication) {
        session = aApplication;
    }
    
    public static SessionContext getSession() {
        return session;
    }
    
    public static ClientContext getClientContext() {
        return ClientContext.getCurrentContext();
    }

    public static Osiris2MainWindowListener getMainWindowListener() {
        if ( mainWindowListener == null ) {
            mainWindowListener = new Osiris2MainWindowListener();
        }
        return mainWindowListener;
    }
    
    public static Map getEnv() {
        return getClientContext().getHeaders();
    }

}
