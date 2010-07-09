package com.rameses.rules;

import com.rameses.interfaces.RuleServiceLocal;
import java.io.Reader;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;

/*
@Service(objectName="rameses:service=RuleService")
@Local(RuleServiceLocal.class)
@Management(RuleServiceMgmt.class)
 */
@Stateless
@Local(RuleServiceLocal.class)
public class RuleService implements RuleServiceLocal {
    
    @Resource(mappedName="RuleBaseMgmt")
    private RuleBaseMgmtMBean mgmt;
    
    @Resource(mappedName="RuleMonitor/local")
    private RuleMonitorLocal monitor;
    
    //RULE EXECUTION
    public void execute(String ruleName, Object[] facts) {
        execute( ruleName, facts, null, null );
    }

    public void execute(String ruleName, Object[] facts, String agenda) {
        execute(ruleName, facts, agenda, null);
    }
    
    public void execute(String ruleName, Object[] facts, String agenda, Map globals) {
        RuleBaseHolder rh = mgmt.getRuleBaseHolder(ruleName);    
        rh.execute(facts,agenda,globals);
    }
    
    public Object createFact(String name) {
        try {
            int i = name.indexOf(".");
            String ruleBaseName = name.substring(i+1, name.indexOf(".", i+1));
            RuleBaseHolder rh = mgmt.getRuleBaseHolder(ruleBaseName); 
            return rh.createFact( name );
        }
        catch(Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }

   public Object createFact(String ruleName, String factName) {
       return createFact("facts." + ruleName + "." + factName);
   }
     
     
    public void flush(String name) {
        try {
            mgmt.loadRules(name);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //monitor.redeploy(name);
    }
    
    //This should force the building of rules. necesary for development.
    public void flushAll() {
        try {
            mgmt.loadAllRules();
        }
        catch(Exception e) {;}
    }

    public void addPackage( String ruleName, Reader rdr ) {
        RuleBaseHolder rh = mgmt.getRuleBaseHolder(ruleName);
        rh.addPackage(rdr);
    }
    
    public void removePackage( String ruleName, Reader rdr ) {
        RuleBaseHolder rh = mgmt.getRuleBaseHolder(ruleName);
        rh.removePackage(rdr);
    }

    public void redeploy(String name) {
        flush(name);
    }

    
    
}


