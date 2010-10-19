/*
 * RuleMgmt.java
 *
 * Created on October 18, 2010, 2:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.ruleserver;

import java.io.Serializable;
import javax.naming.InitialContext;

/**
 *
 * @author ms
 */
public class RuleMgmt implements RuleMgmtMBean, Serializable {
    
    
    public RuleMgmt() {
    }

    public void start() throws Exception {
        System.out.println("STARTING RULE SERVICES");
        RuleManager.getInstance().setDataSource( AppContext.getSystemDs() );
        RuleManager.getInstance().loadAll();
        InitialContext ctx = new InitialContext();
        JndiUtil.bind( ctx, AppContext.getPath()+RuleMgmt.class.getSimpleName(), this );
    }

    public void stop() throws Exception {
        RuleManager.getInstance().destroy();
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx, AppContext.getPath()+RuleMgmt.class.getSimpleName() );
    }

    public void redeploy(String ruleset, String rulegroup) throws Exception {
        RuleManager.getInstance().redeploy(ruleset, rulegroup);
    }

    public void redeployAll() throws Exception {
        RuleManager.getInstance().redeployAll();
    }

    public void addRulePackage(String ruleset, String rulegroup, String pkgName, Object o) throws Exception {
        RuleManager.getInstance().addRulePackage( ruleset,rulegroup,pkgName,o );
    }

    public void addRulePackage(String ruleset, String rulegroup, String pkgName, Object o, boolean deploy) throws Exception {
        RuleManager.getInstance().addRulePackage( ruleset,rulegroup,pkgName,o,deploy );
    }

    public void removeRulePackage(String ruleset, String rulegroup, String pkgName) throws Exception {
        RuleManager.getInstance().removeRulePackage( ruleset,rulegroup,pkgName );        
    }

    public void removeRulePackage(String ruleset, String rulegroup, String pkgName, boolean deploy) throws Exception {
        RuleManager.getInstance().removeRulePackage( ruleset,rulegroup,pkgName,deploy );        
    }

    public String showLoadedRules() {
        return RuleManager.getInstance().showLoadedRules();
    }
    
}
