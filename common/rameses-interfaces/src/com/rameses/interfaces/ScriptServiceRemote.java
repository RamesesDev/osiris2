package com.rameses.interfaces;

import java.util.Map;

public interface ScriptServiceRemote {
    
    byte[] getScriptInfo(String name);
    Class getScriptIntfClass(String name);

    Object getScript(String name);
    
    Object invoke(String name, String method, Object[] params);   
    
}
