/*
 * RuleMgmtMBean.java
 *
 * Created on October 18, 2010, 2:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.ruleserver;

public interface RuleMgmtMBean {
    
    
    void start() throws Exception;
    void stop() throws Exception;
    
    void redeploy( String ruleset, String rulegroup ) throws Exception;
    void redeployAll() throws Exception;
    
    /***
     * adds the package to the database.
     */
    void addRulePackage( String ruleset, String rulegroup, String pkgName, Object o ) throws Exception;
    void addRulePackage( String ruleset, String rulegroup, String pkgName, Object o, boolean deploy ) throws Exception;
    void removeRulePackage(String ruleset, String rulegroup, String pkgName ) throws Exception;
    void removeRulePackage(String ruleset, String rulegroup, String pkgName, boolean deploy ) throws Exception;
    String showLoadedRules();
    
    
}