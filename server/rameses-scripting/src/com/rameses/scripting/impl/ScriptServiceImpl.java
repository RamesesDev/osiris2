/*
 * ScriptServiceImpl.java
 *
 * Created on October 15, 2010, 2:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting.impl;

import com.rameses.scripting.ScriptExecutor;
import com.rameses.scripting.ScriptManager;
import com.rameses.scripting.ScriptServiceLocal;
import java.util.Map;

/**
 *
 * @author ms
 */
public class ScriptServiceImpl implements ScriptServiceLocal {
    
    /** Creates a new instance of ScriptServiceImpl */
    public ScriptServiceImpl() {
    }

    public byte[] getScriptInfo(String name) {
        return ScriptManager.getInstance().getProxyIntfBytes(name);
    }

    public Object invoke(String serviceName, String methodName, Object[] params, Map env) {
        try {
            ScriptExecutor se = ScriptManager.getInstance().createExecutor( serviceName, methodName, params );
            return se.execute( this, params, env );
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Object invokeAsync(Map data, String destination) {
        System.out.println("invoking async " + destination);
        System.out.println(data);
        return "async";
    }

    public void pushResponse(String requestId, Object data) {
        System.out.println("do nothing...");
    }

    public Object getPollData(String requestId) {
        return null;
    }
    
}
