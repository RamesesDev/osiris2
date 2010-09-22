package com.rameses.ruleserver;

import com.rameses.rules.common.RuleAction;
import java.util.List;
import java.util.Map;


public interface RuleServiceLocal {

    Object createFact(String ruleset, String factName) throws Exception;
    Object createFact(String ruleset, String factName, Map data) throws Exception;
    void execute( String ruleset, List facts, Object globals, String agenda ) throws Exception;
    void execute( String ruleset, List facts ) throws Exception;
    void execute( String ruleset, List facts, Object globals ) throws Exception;
    RuleAction createRuleAction();
    
    
}
