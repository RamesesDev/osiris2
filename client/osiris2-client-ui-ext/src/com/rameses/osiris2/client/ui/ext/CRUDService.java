
package com.rameses.osiris2.client.ui.ext;

import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.client.OsirisContext;
import com.rameses.osiris2.client.InvokerProxy;
import com.rameses.rcp.framework.ChangeLog;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.MethodResolver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class CRUDService {
    
    public final static String SERVICE_NAME = "crud.service";
    public final static String SERVICE_HOSTNAME = "crud.hostname";
    
    
    public static Object getService() {
        SessionContext app = OsirisContext.getSession();
        String svc = (String)app.getEnv().get(SERVICE_NAME);
        String host = (String)app.getEnv().get(SERVICE_HOSTNAME);
        if( host == null ) host = "default.host";
        
        if(svc==null)
            throw new IllegalStateException("CRUD service not defined");
        if( host == null )
            throw new IllegalStateException("CRUD host not defined");
        try {
            return InvokerProxy.getInstance().create(svc,host);
        } catch(Exception ign) {
            throw new IllegalStateException(ign);
        }
    }
    
    public static List getList(Object service, String ql, Map params, Map options) {
        try {
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            // do not send null parameter, MethodResolver cannot resolve null params
            if ( options == null ) options = new HashMap();
            return (List)mr.invoke(service, "getList", new Class[]{String.class, Map.class, Map.class }, new Object[]{ql, params, options });
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public static Object read( Object service, String className, Object key, Map options  ) {
        try {
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            if ( options == null ) options = new HashMap();
            return mr.invoke(service, "read",new Class[]{String.class, Object.class, Map.class}, new Object[]{className, key, options});
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public static Object create( Object service, String className, Object key, Object data, Map options  ) {
        try {
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            if ( options == null ) options = new HashMap();
            return mr.invoke(service, "create", new Class[]{String.class, Object.class, Object.class, Map.class}, new Object[]{className, key, data, options});
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public static Object update( Object service, String className, Object key, Object data, Map options ) {
        try {
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            if ( options == null ) options = new HashMap();
            return mr.invoke(service, "update", new Class[]{String.class,Object.class,Object.class, Map.class}, new Object[]{className, key, data, options});
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public static Object delete( Object service, String className, Object key, Object data, Map options  ) {
        try {
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            if ( options == null ) options = new HashMap();
            return mr.invoke(service, "delete",new Class[]{String.class, Object.class, Object.class, Map.class}, new Object[]{className, key, data, options});
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public static Map createLog(String username, String className, Object entityId, String action, ChangeLog log) {
        return createLog(username, className, entityId, action, log, null);
    }
    
    public static Map createLog(String username, String className, Object entityId, String action, ChangeLog log, List logs) {
        Map mlog = new HashMap();
        int idx = className.lastIndexOf(".");
        mlog.put("entityName", className.substring( (idx != -1? idx + 1 : 0) ));
        mlog.put("user", username);
        mlog.put("action", action);
        mlog.put("refId", entityId);
        StringBuffer details = new StringBuffer("");
        if ( log != null)
            details.append(log.getDifference());
        
        if (logs != null) {
            for (Object o : logs) {
                ChangeLog detail = (ChangeLog) o;
                if ( !detail.hasChanges() ) continue;
                details.append("\n-" + detail.getDifference());
            }
        }
        
        mlog.put("details", details.toString());
        return mlog;
    }
    
}
