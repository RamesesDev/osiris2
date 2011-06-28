/*
 * ScriptMgmtMBean.java
 *
 * Created on October 16, 2010, 8:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

/**
 *
 * @author ms
 */
public interface ServerMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    
    void reloadScripts();
    void reloadScript(String name);
    void reloadDataSources() throws Exception;
    
    void reloadSchema();
    void reloadSchema(String name);
    
    void reloadTemplates();
    void reloadTemplate(String name);
    
    String showAppProperties();
    String showScriptInfo(String name);
    
}
