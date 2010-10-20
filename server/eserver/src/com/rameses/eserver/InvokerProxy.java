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
import com.rameses.scripting.ScriptServiceLocal;
import com.rameses.util.ExprUtil;

import com.rameses.util.SysMap;
import java.util.Map;

/**
 *
 * @author ms
 */
public class InvokerProxy {
    
    private ScriptServiceLocal scriptService;
    private Map env;
    private String host;
    
    public InvokerProxy(ScriptServiceLocal s, Map env) {
        this.scriptService = s;
        this.env = env;
    }

    public Object create(String svcName) {
        SysMap m = new SysMap(env);
        String scriptname = ExprUtil.substituteValues(svcName,m);
        ScriptProxyInvocationHandler handler = new ScriptProxyInvocationHandler(scriptService,scriptname,env);
        return ScriptManager.getInstance().createProxy( scriptname, handler );
    }
    
}
