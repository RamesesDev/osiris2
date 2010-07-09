/*
 * RuleMonitor.java
 *
 * Created on December 28, 2009, 9:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
@Local(RuleMonitorLocal.class)
public class RuleMonitor implements RuleMonitorLocal{
    
    @Resource
    private SessionContext ctx;
    
    @Resource(mappedName="RuleBaseMgmt")
    private RuleBaseMgmtMBean mgmt;
    
    @PersistenceContext(unitName="ruleservicePU")
    private EntityManager em;
    
    public void startMonitor(long delay) throws Exception {
        mgmt.loadAllRules();
        
        for(Object k: mgmt.getRuleNames() ) {
            String key = (String)k;
            RuleMgmtBean rb = em.find( RuleMgmtBean.class, key );
            if( rb == null ) {
                rb = new RuleMgmtBean(key);
                em.persist(rb);
            }
        }
        if(delay > 0) {
            ctx.getTimerService().createTimer(delay, delay,"RULE_SERVICE");
        }
    }
    
    //persist the rule names to the database
   
    
    @Timeout
    public void checkUpdates(Timer t) throws Exception {
        if( mgmt.isUpdating() ) return;
        mgmt.setUpdating(true);
        
        //System.out.println("checking rule updates");
        
        //place code here
        Query q = em.createQuery("select o.name from " + RuleMgmtBean.class.getSimpleName() + " o WHERE o.dtrule != o.dtmodified");
        List<String> list = q.getResultList();
        
        List<String> forUpdate = new ArrayList<String>();
        for(String ruleName : list ) {
            mgmt.loadRules( ruleName );
            forUpdate.add( ruleName );
        }
        
        //update values in the rule bases
        for( String updatedRule: forUpdate ) {
            RuleMgmtBean b = em.find(RuleMgmtBean.class, updatedRule);
            if( b == null ) {
                b = new RuleMgmtBean( updatedRule );
                b.setDtmodified( new Date() );
                b.setDtrule( b.getDtmodified() );
                em.persist(b);
            } else {
                b.setDtrule(b.getDtmodified());
                em.merge( b );
            }
        }
        forUpdate.clear();
        mgmt.setUpdating(false);
    }
    
    public void redeploy(String ruleBaseName) {
        //after updating this, the timer will check for updates
        RuleMgmtBean b = em.find(RuleMgmtBean.class, ruleBaseName);
        b.setDtmodified(new Date());
    }

  
    
    
}
