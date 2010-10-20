package com.rameses.scripting;

import java.util.Map;


public interface ScriptServiceLocal {
    
    byte[] getScriptInfo(String name);
    Object invoke(String name, String method, Object[] params, Map env); 
    Object invokeAsync(Map data, String destination); 
    
    void pushResponse( String requestId, Object data );
    Object getPollData(String requestId);
    
}
