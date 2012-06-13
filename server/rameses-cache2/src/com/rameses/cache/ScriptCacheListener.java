/*
 * ScriptCacheListener.java
 * Created on September 28, 2011, 2:12 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import com.rameses.server.common.AppContext;
import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class ScriptCacheListener implements CacheListener {
    
    private Map conf;
    private String scriptName = "CacheServiceHandler";
    private String timeoutMethod = "timeout";
    private String updateMethod = "updated";
    private String removeMethod = "removed";
    
    /** Creates a new instance of ScriptCacheListener */
    public ScriptCacheListener() {
        
    }

    public ScriptCacheListener(String scriptName) {
        this(scriptName, null );
    }
    
    public ScriptCacheListener(String scriptName, Map conf ) {
        this.scriptName = scriptName;
    }
    
    private Map _getConf() {
        if( conf==null) {
            conf = new HashMap();
            try {
                String ctx = AppContext.getName();
                if(ctx!=null) conf.put("app.context",ctx);
            } catch(Exception e) {;}
        }
        return conf;
    }
    
    public void timeout(String id, Object info) {
        if(timeoutMethod==null) return;
        try {
            ScriptServiceContext ctx = new ScriptServiceContext(_getConf() );
            ServiceProxy proxy = ctx.create(this.scriptName);
            proxy.invoke(timeoutMethod, new Object[]{id, info});
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void removed(String id, Object info) {
        if(removeMethod==null) return;
        try {
            ScriptServiceContext ctx = new ScriptServiceContext(_getConf() );
            ServiceProxy proxy = ctx.create(this.scriptName);
            proxy.invoke(removeMethod, new Object[]{id, info});
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updated(String id, Object info) {
        if(updateMethod==null) return;
        try {
            Map c = _getConf();
            ScriptServiceContext ctx = new ScriptServiceContext(c );
            ServiceProxy proxy = ctx.create(this.scriptName);
            proxy.invoke(updateMethod, new Object[]{id, info});
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getTimeoutMethod() {
        return timeoutMethod;
    }

    public void setTimeoutMethod(String timeoutMethod) {
        this.timeoutMethod = timeoutMethod;
    }

    public String getUpdateMethod() {
        return updateMethod;
    }

    public void setUpdateMethod(String updateMethod) {
        this.updateMethod = updateMethod;
    }


    public String getRemoveMethod() {
        return removeMethod;
    }

    public void setRemoveMethod(String removeMethod) {
        this.removeMethod = removeMethod;
    }
    
    
    
}
