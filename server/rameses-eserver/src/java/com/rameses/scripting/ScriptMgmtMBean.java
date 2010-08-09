/*
 * InvokerMgmtLocal.java
 *
 * Created on June 26, 2009, 7:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.util.List;
import java.util.Map;

public interface ScriptMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;

    ScriptObject getScriptObject(String name);
    Object createLocalProxy( String name, Map env );
    Object createRemoteProxy(String name, Map env, String hostKey );
    
    void flushAll();
    void flushInterceptors();
    void flushScript(String name);
    
    List<String> findBeforeInterceptors(String name);
    List<String> findAfterInterceptors(String name);

    Map getLoadedScripts();
            
}
