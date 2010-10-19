package com.rameses.scripting.impl;

import com.rameses.scripting.*;

public class ScriptManagerImpl extends ScriptManager {
    
    private ResourceInjector resourceInjector;
    private ScriptProvider scriptProvider;
    
    public void init() {
        resourceInjector = new ResourceInjectorImpl();
        scriptProvider = new GroovyScriptProvider();
    }
    
    public ResourceInjector getResourceProvider() {
        return resourceInjector;
    }

    public ScriptProvider getScriptProvider() {
        return scriptProvider;
    }

    protected void destroy() {
    }
    
    
}
