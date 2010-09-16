package com.rameses.ruleserver;

import java.util.List;


public interface RuleServiceLocal {

    Object createFact(String ruleset, String factName) throws Exception;
    Object execute( String ruleset, List facts, Object globals, String agenda ) throws Exception;
    
}
