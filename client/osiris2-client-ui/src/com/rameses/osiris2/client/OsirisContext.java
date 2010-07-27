package com.rameses.osiris2.client;

import com.rameses.messaging.ConnectionManager;
import com.rameses.messaging.MessagingConnection;
import com.rameses.osiris2.*;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.MachineInfo;
import java.util.HashMap;
import java.util.Map;


public final class OsirisContext {
    
    private static SessionContext session;
    private static MessagingConnection sysCon;
    
    public static void setSession(SessionContext aApplication) {
        session = aApplication;
    }
    
    public static SessionContext getSession() {
        return session;
    }
    
    public static ClientContext getClientContext() {
        return ClientContext.getCurrentContext();
    }
    
    public static MessagingConnection getSystemConnection() {
        if ( sysCon == null ) {
            try {
                Map conf = new HashMap();
                
                String host = (String) conf.get("host");
                String driverClass = (String) conf.get("driverClass");
                String uname = MachineInfo.getInstance().getMacAddress();
                
                MessagingConnection con = ConnectionManager.getInstance()
                .getConnection(driverClass, host, uname, uname);
                
                sysCon = new MessagingConnectionWrapper(con);
                        
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        
        return sysCon;
    }
    
}
