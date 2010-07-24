/*
 * RuleServiceDeployer.java
 *
 * Created on July 14, 2010, 8:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import java.io.Serializable;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * This class checks for rule package changes.
 * If there are changes, it will update by itself.  
 */

@Stateless
@Local(RuleServiceMgmtLocal.class)
public class RuleServiceMgmt implements Serializable, RuleServiceMgmtLocal, RuleServiceMgmtMBean {
    
    @PersistenceContext(unitName="systemPU")
    private EntityManager em;
    
    @Resource
    private SessionContext context;
    
    public void start() throws Exception {
        System.out.println("*********************************");
        System.out.println("STARTING RULE SERVICE MANAGEMENT");
        System.out.println("*********************************");
        InitialContext ctx = new InitialContext();
        RuleServiceMgmtLocal local = (RuleServiceMgmtLocal) ctx.lookup("RuleServiceMgmt/local");
        local.startManager();
    }

    public void stop() throws Exception {
        System.out.println("****************************");
        System.out.println("STOPPING RULE SERVICE");
        System.out.println("****************************");
    }

    public void startManager() {
        System.out.println("starting manager");
        context.getTimerService().createTimer(2000,2000,"RULE_SERVICE");
    }

    @Timeout
    public void timeout(Timer t) {
        System.out.println("Timeout Rule Mgmt->" + em);
    }

    
}
