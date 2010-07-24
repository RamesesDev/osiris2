/*
 * DeployerServiceMBean.java
 *
 * Created on November 5, 2009, 1:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

public interface ScriptDeployerMBean {

    void start() throws Exception;
    void stop();
    
}
