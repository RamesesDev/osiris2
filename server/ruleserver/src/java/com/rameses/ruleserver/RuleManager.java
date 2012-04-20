/*
 * RuleManager.java
 *
 * Created on October 18, 2010, 8:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.ruleserver;

import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.drools.KnowledgeBase;

public final class RuleManager {
    
    private static final RuleManager instance = new RuleManager();
    
    public static RuleManager getInstance() {
        return instance;
    }
    
    private final Map<String, KnowledgeSet> rulesets = Collections.synchronizedMap(new Hashtable());
    
    private DataSource dataSource;
    
    public RuleManager() {}
    
    public void loadAll() throws Exception {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        System.out.println("STARTING RULE SERVER @ " + new Date() );
        SqlContext sqlc = SqlManager.getInstance().createContext(dataSource);
        
        RuleSetScanner scanner = new RuleSetScanner(sqlc);
        List<KnowledgeSet> results = scanner.getResults();
        for(KnowledgeSet ks: results) {
            ks.load(sqlc);
            rulesets.put( ks.getKey(), ks);
        }
    }
    
    public void destroy() throws Exception {
        rulesets.clear();
        dataSource = null;
    }
    
    public void reload( String ruleName ) {
        try {
            loadAll();
        } catch(Exception e) {;}
    }
    
    public void redeploy(String ruleset, String rulegroup) throws Exception {
        //create another
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        KnowledgeSet newSet = new KnowledgeSet(ruleset,rulegroup, null);
        KnowledgeSet old = rulesets.get(newSet.getKey());
        SqlContext sqlc = SqlManager.getInstance().createContext( dataSource );
        newSet.load( sqlc );
        rulesets.put( newSet.getKey(), newSet );
        if(old!=null) {
            old.destroy();
            old = null;
        }
    }
    
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void redeployAll() throws Exception {
        synchronized(rulesets) {
            Collection<KnowledgeSet> list = rulesets.values();
            for( KnowledgeSet ks : list) {
                redeploy( ks.getKey(), ks.getRulegroup() );
            }
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
    
    private KnowledgeSet findKnowledgeSet(String ruleset, String rulegroup) {
        KnowledgeSet newSet = new KnowledgeSet(ruleset,rulegroup, null);
        return rulesets.get(newSet.getKey());
    }
    
    public void addRulePackage(String ruleset, String rulegroup, String packagename, Object o, boolean deploy) throws Exception {
        //adds package to the database
        SqlContext sqlc = SqlManager.getInstance().createContext( dataSource );
        KnowledgeSet ks = findKnowledgeSet( ruleset, rulegroup );
        ks.deployPackage( packagename, o, sqlc );
    }
    
    public void removeRulePackage(String ruleset, String rulegroup, String pkgName) throws Exception {
        removeRulePackage(ruleset, rulegroup, pkgName, true );
    }
    
    public void removeRulePackage(String ruleset, String rulegroup, String pkgName, boolean deploy) throws Exception {
        SqlContext sqlc = SqlManager.getInstance().createContext( dataSource );
        KnowledgeSet ks = findKnowledgeSet( ruleset, rulegroup );
        ks.undeployPackage( pkgName, sqlc );
    }
    
}
