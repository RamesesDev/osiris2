/*
 * RuleMonitorLocal.java
 *
 * Created on December 28, 2009, 9:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

/**
 *
 * local interface for managing the validity of rulebases.
 */
public interface RuleMonitorLocal {
    
    void startMonitor(long delay) throws Exception;
    void redeploy(String ruleBaseName);
    
}
