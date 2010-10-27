/*
 * ScriptDeployerMDB.java
 *
 * Created on October 23, 2010, 2:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

/**
 *
 * @author ms
 */
public interface ScriptDeployerMBean {
    
    public void start() throws Exception;
    public void stop() throws Exception;
    
}
