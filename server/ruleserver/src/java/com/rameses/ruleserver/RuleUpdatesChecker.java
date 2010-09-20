/*
 * RuleAgentScanner.java
 *
 * Created on September 16, 2010, 8:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.ruleserver;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

@Stateless
@Local(RuleUpdatesCheckerLocal.class)
public class RuleUpdatesChecker implements RuleUpdatesCheckerLocal {
    
    @Resource
    private SessionContext sessCtx;
    
    public void start() {
        sessCtx.getTimerService().createTimer(0, 5000, "RuleAgent Timer");
    }

    @Timeout
    public void checkChanges(Timer t) {
        t.cancel();
        System.out.println("checking rule base for changes...");
    }

    public void stop() {
    }
    
}
