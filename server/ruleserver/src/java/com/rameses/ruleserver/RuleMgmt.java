package com.rameses.ruleserver;

import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.drools.KnowledgeBase;

public class RuleMgmt implements RuleMgmtMBean, Serializable {
    
    private final Map<String, KnowledgeSet> rulesets = new Hashtable();
    
    //enable this for unit testing
    private boolean unitTest = false;
    
    private DataSource ds;
    
    public RuleMgmt() {
        
    }
    
    public RuleMgmt(boolean testMode) {
        this.unitTest = testMode;
    }
  
    public void start() throws Exception {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        System.out.println("STARTING RULE SERVER @ " + new Date() );
        
        SqlContext sqlc = null;
        if(!unitTest) sqlc = createSqlContext();
        
        RuleSetScanner scanner = new RuleSetScanner(sqlc);
        List<KnowledgeSet> results = scanner.getResults();
        for(KnowledgeSet ks: results) {
            ks.load(sqlc);
            rulesets.put( ks.getKey(), ks);
        }
        
        //bind to Jndi
        if(!unitTest) {
            InitialContext ctx = new InitialContext();
            JndiUtil.bind(ctx,RuleMgmt.class.getSimpleName(), this);
            
            //run the RuleAgentMonitor
            //RuleUpdatesCheckerLocal updater = (RuleUpdatesCheckerLocal)ctx.lookup(RuleUpdatesChecker.class.getSimpleName()+"/local");
            //updater.start();
        }
        
    }
    
    private SqlContext createSqlContext() throws Exception {
        if(unitTest && ds==null) {
            return null;
        } else if(!unitTest && ds == null ) {
            InitialContext ctx = new InitialContext();
            ds =  (DataSource)ctx.lookup("java:DefaultDS");
        }
        SqlContext sqlc = null;
        if(ds!=null) {
            sqlc = SqlManager.getInstance().createContext(ds);
        }
        return sqlc;
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING RULE SERVER @ " + new Date() );
        if(!unitTest) {
            InitialContext ctx = new InitialContext();
            JndiUtil.unbind(ctx,RuleMgmt.class.getSimpleName());
            //run the RuleAgentMonitor
            //RuleUpdatesCheckerLocal updater = (RuleUpdatesCheckerLocal)ctx.lookup(RuleUpdatesChecker.class.getSimpleName()+"/local");
            //updater.stop();
        }
        rulesets.clear();
    }
    
    
    public void redeploy(String ruleset, String rulegroup) throws Exception {
        //create another
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        KnowledgeSet newSet = new KnowledgeSet(ruleset,rulegroup, null);       
        KnowledgeSet old = rulesets.get(newSet.getKey());
        
        SqlContext sqlc = null;
        if( !unitTest ) {
            sqlc = createSqlContext();
        }
        newSet.load( sqlc );
        rulesets.put( newSet.getKey(), newSet );
        if(old!=null) {
            old.destroy();
            old = null;
        }
    }
    
    //call only when necessary.
    public void redeployAll() throws Exception {
        Collection<KnowledgeSet> list = rulesets.values();
        for( KnowledgeSet ks : list) {
            redeploy( ks.getKey(), ks.getRulegroup() );
        }
    }
    
      
    public String showLoadedRules() {
        StringBuffer sb = new StringBuffer();
        Set<String> names = rulesets.keySet();
        for( KnowledgeSet ks : rulesets.values()) {
            sb.append( ks.getKey() + "\n" );
        }
        return sb.toString();
    }
    
    //name here in knowledge base must be separated with : if there is a rulegroup
    public KnowledgeBase getKnowledgeBase(String name) {
        return rulesets.get(name).getKnowledgeBase();
    }

    public void addRulePackage(String ruleset, String rulegroup, String pkgName, Object o) throws Exception {
        addRulePackage( ruleset, rulegroup, pkgName, o, true);
    }

    public void addRulePackage(String ruleset, String rulegroup, String packagename, Object o, boolean deploy) throws Exception {
        //adds package to the database
        SqlContext sqlc = createSqlContext();
        
        //force add rule set if it does not yet exist
        SqlExecutor se = sqlc.createNamedExecutor("ruleserver:add-rule-set");
        se.setParameter("ruleset", ruleset);
        se.setParameter("rulegroup", rulegroup);
        se.execute();
        
        se = sqlc.createNamedExecutor("ruleserver:add-rule-package");
        se.setParameter("ruleset", ruleset);
        se.setParameter("rulegroup", rulegroup);
        se.setParameter("packagename", packagename);
        se.setParameter("content", o);
        se.execute();
        if(deploy) {
            redeploy( ruleset, rulegroup );
        }
    }

    public void removeRulePackage(String ruleset, String rulegroup, String pkgName) throws Exception {
        removeRulePackage(ruleset, rulegroup, pkgName, true );
    }

    public void removeRulePackage(String ruleset, String rulegroup, String pkgName, boolean deploy) throws Exception {
         //removes package to the database.
        //adds package to the database
        SqlContext sqlc = createSqlContext();
        SqlExecutor se = sqlc.createNamedExecutor("ruleserver:remove-rule-package");
        se.setParameter("ruleset", ruleset);
        se.setParameter("rulegroup", rulegroup);
        se.setParameter("packagename", pkgName);
        se.execute();
        if(deploy) {
            redeploy( ruleset, rulegroup );
        }
    }

   
  
    
}
