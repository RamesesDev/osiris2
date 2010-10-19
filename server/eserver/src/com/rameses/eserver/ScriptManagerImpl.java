/*
 * ScriptManagerImpl.java
 *
 * Created on October 16, 2010, 8:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.scripting.InterceptorLoader;
import com.rameses.scripting.ScriptLoader;
import com.rameses.scripting.ScriptManager;
import com.rameses.scripting.ScriptProvider;
import com.rameses.scripting.impl.GroovyScriptProvider;

/**
 *
 * @author ms
 */
public class ScriptManagerImpl extends ScriptManager {
    
    private ScriptProvider scriptProvider;
    private ScriptLoader scriptLoader = new CustomScriptLoader(this);
    private InterceptorLoader interceptorLoader = new CustomInterceptorLoader(this);
    
    public ScriptProvider getScriptProvider() {
        return scriptProvider;
    }

    protected void init() {
        scriptProvider = new GroovyScriptProvider();
    }

    protected void destroy() {
    }

    public ScriptLoader getScriptLoader() {
        return scriptLoader;
    }

    public InterceptorLoader getInterceptorLoader() {
        return interceptorLoader;
    }
    


    
}
