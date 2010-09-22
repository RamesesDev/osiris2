package com.rameses.ruleserver;

import org.drools.KnowledgeBase;

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
    
    KnowledgeBase getKnowledgeBase(String name);
    
    
}
