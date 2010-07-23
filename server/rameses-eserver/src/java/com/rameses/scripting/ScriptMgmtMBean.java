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

public interface ScriptMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;

    //byte[] getScriptIntfBytes(String name);
    ScriptObject getScriptObject(String name);
    //Class getScriptInfo(String name);
    
    void flushAll();
    void flushInterceptors();
    void flushScript(String name);
    
    List<String> findBeforeInterceptors(String name);
    List<String> findAfterInterceptors(String name);

            
}
