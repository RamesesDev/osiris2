package com.rameses.interfaces;

import java.util.Map;

public interface ScriptServiceLocal {
    
    byte[] getScriptInfo(String name);
    Class getScriptIntfClass(String name);

    Object getScript(String name);
    
    Object invoke(String name, String method, Object[] params, Map env);   
    
}
