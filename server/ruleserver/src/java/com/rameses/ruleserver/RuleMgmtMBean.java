package com.rameses.ruleserver;

import org.drools.KnowledgeBase;

public interface RuleMgmtMBean {
    
    
    void start() throws Exception;
    void stop() throws Exception;
    
    void redeploy( String ruleset ) throws Exception;
    void redeployAll() throws Exception;
    
    void addPackage( String ruleset, Object o ) throws Exception;
    String showLoadedRules();
    
    KnowledgeBase getKnowledgeBase(String name);
    
    
}
