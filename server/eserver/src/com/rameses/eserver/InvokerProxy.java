/*
 * InvokerProxy.java
 *
 * Created on October 18, 2010, 8:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.scripting.ScriptManager;
import com.rameses.scripting.ScriptProxyInvocationHandler;
import com.rameses.util.ExprUtil;
import java.util.Map;

/**
 *
 * @author ms
 */
public class InvokerProxy {
    
    private Map env;
    private String host;
    
    public InvokerProxy(Map env) {
        this.env = env;
    }
    
    public Object create(String svcName) {
        try {
            Map m = AppContext.getSysMap();
            String scriptname = ExprUtil.substituteValues(svcName,m);
            ScriptProxyInvocationHandler handler = new ScriptProxyInvocationHandler(ScriptServiceDelegate.getScriptService(), scriptname,env);
            return ScriptManager.getInstance().createProxy( scriptname, handler );
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    
}
