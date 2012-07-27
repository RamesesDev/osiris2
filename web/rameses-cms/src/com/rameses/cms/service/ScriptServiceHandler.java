/*
 * HttpServiceHandler.java
 *
 * Created on June 24, 2012, 12:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.service;

import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class ScriptServiceHandler  extends AbstractServiceHandler {
    
    public String getName() {
        return "script";
    }
    
    protected AbstractServiceHandler.ServiceInvoker getServiceInvoker(String name, Map conf) {
        return new MyScriptInvoker(name, conf);
    }
    
    
    private class MyScriptInvoker implements AbstractServiceHandler.ServiceInvoker {
        private ServiceProxy serviceProxy;
        
        public MyScriptInvoker(String name, Map conf) {
            String host = (String)conf.get("app.host");
            if(host==null) host = (String)conf.get("host");
            if( host == null )
                throw new RuntimeException("app.host is not defined");
            String appContext = (String)conf.get("app.context");
            if(appContext==null) appContext = (String)conf.get("context");
            if(appContext == null )
                throw new RuntimeException("app.context is not defined");
            Map map = new HashMap();
            map.put("app.host", host);
            map.put("app.context", appContext);
            ScriptServiceContext ctx = new ScriptServiceContext(map);
            serviceProxy = ctx.create( name );
        }
        public Object invoke(String methodName, Object[] args) {
            try {
                
                return serviceProxy.invoke( methodName, args );
            } catch(Exception ex) {
                return "<font color=red>error service:" + ex.getMessage()+"</font>";
            }
        }
    }
    
    
}
