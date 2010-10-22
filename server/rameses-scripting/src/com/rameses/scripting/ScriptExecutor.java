/*
 * ScriptExecutor.java
 *
 * Created on October 15, 2010, 2:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.util.Map;


public interface ScriptExecutor {
    
    Object execute(ScriptServiceLocal scriptService, Object[] params, Map env) throws Exception;
    
    //this needs to be closed in case you are running a pooled execution  
    void close();
}
