/*
 * RuleLoader.java
 *
 * Created on September 15, 2010, 6:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.ruleserver;

import com.rameses.sql.SqlContext;
import com.rameses.util.URLDirectory;
import com.rameses.util.URLDirectory.URLFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * this scans for all rules in the path and in DB
 */
public class RuleSetScanner implements URLFilter {
    
    private static final String ROOT_URL = "META-INF/rulesets/";
    private SqlContext sqlContext;
    private List<KnowledgeSet> results;
    
    public RuleSetScanner(SqlContext c) {
        this.sqlContext = c;
    }
    
    private void loadNamesFromClasspath() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> e = classLoader.getResources(ROOT_URL);
        while(e.hasMoreElements()) {
            URL u = e.nextElement();
            URLDirectory ud = new URLDirectory(u);
            ud.list( this,classLoader );
        }
    }
    
    public boolean accept(URL u, String filter) {
        if( filter.endsWith("/")) {
            filter = filter.substring(0, filter.length()-1);
            String ruleName = filter.substring(filter.lastIndexOf("/")+1);
            KnowledgeSet ks = new KnowledgeSet(ruleName,null,null);
            if(results.indexOf(ks)<0) results.add(ks);
        }
        return false;
    }
    
    private void loadNamesFromDb() {
        try{
            if( sqlContext!=null ) {
                List<Map> list = sqlContext.createNamedQuery( "ruleserver:list-rulenames" ).getResultList();
                for(Map map : list) {
                    String name = (String)map.get("name");
                    String rulegroup = (String)map.get("rulegroup");
                    if(name!=null && name.trim().length() > 0 ) {
                        KnowledgeSet ks = new KnowledgeSet(name,rulegroup,null);
                        if(results.indexOf(ks)<0) results.add(ks);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("ERROR LOADING NAMES FROM DB->"+e.getMessage());
        }
    }
    
    public List getResults() throws Exception {
        results = new ArrayList();
        loadNamesFromDb();
        loadNamesFromClasspath();
        return  results;
    }
    
    
    
    
}
