/*
 * RuleMgmtMBean.java
 *
 * Created on December 28, 2009, 9:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import java.util.List;

public interface RuleBaseMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    boolean isUpdating();
    void setUpdating(boolean updating);
    
    List getRuleNames();
    void loadRules(String name) throws Exception ;
    void loadAllRules() throws Exception;
    
    RuleBaseHolder getRuleBaseHolder(String name);
    
    boolean isMonitor();
    void setMonitor(boolean monitor);
}
