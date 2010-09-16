package com.rameses.ruleserver;

import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import java.io.InputStream;
import java.io.Serializable;
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
        System.out.println("STARTING RULE SERVER @ " + new Date() );
        
        SqlContext sqlc = null;
        if(!unitTest) sqlc = createSqlContext();
        
        RuleSetScanner scanner = new RuleSetScanner(sqlc);
        List<KnowledgeSet> results = scanner.getResults();
        for(KnowledgeSet ks: results) {
            ks.load(sqlc);
            rulesets.put( ks.getName(), ks);
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
            RuleUpdatesCheckerLocal updater = (RuleUpdatesCheckerLocal)ctx.lookup(RuleUpdatesChecker.class.getSimpleName()+"/local");
            updater.stop();
        }
        rulesets.clear();
    }
    
    
    public void redeploy(String ruleset) throws Exception {
        //create another
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        KnowledgeSet old = rulesets.get(ruleset);
        KnowledgeSet newSet = old.clone();
        
        SqlContext sqlc = null;
        if( !unitTest ) {
            sqlc = createSqlContext();
        }
        newSet.load( sqlc );
        rulesets.put( ruleset, newSet );
        old.destroy();
        old = null;
    }
    
    //call only when necessary.
    public void redeployAll() throws Exception {
        Set<String> names = rulesets.keySet();
        for( String s: names) {
            redeploy( s );
        }
    }
    
    public void addPackage(String ruleset, Object o) throws Exception {
        KnowledgeSet kset = rulesets.get(ruleset);
        if( o instanceof InputStream ) {
            InputStream is = (InputStream)o;
            kset.addPackage( is );
        }
    }
    
    public String showLoadedRules() {
        StringBuffer sb = new StringBuffer();
        Set<String> names = rulesets.keySet();
        for( String s: names) sb.append( s );
        return sb.toString();
    }
    
    public KnowledgeBase getKnowledgeBase(String name) {
        return rulesets.get(name).getKnowledgeBase();
    }
    
}
