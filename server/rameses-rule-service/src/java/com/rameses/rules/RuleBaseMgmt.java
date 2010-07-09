/*
 * RuleBaseMgmt.java
 *
 * Created on December 28, 2009, 9:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import com.rameses.jndi.JndiUtil;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.naming.InitialContext;


public class RuleBaseMgmt implements RuleBaseMgmtMBean, Serializable {
    
    private final static String jndiName = "RuleBaseMgmt";
    
    private Map<String,RuleBaseHolder> ruleBases = new Hashtable<String,RuleBaseHolder>();
    
    //This is triggered by the timer
    private boolean updating;
    private List ruleNames;
    private boolean monitor;
    
    public void start() throws Exception {
        JndiUtil.bind(new InitialContext(),jndiName,this);
        //load all rules here
        System.out.println("*********************************");
        System.out.println("STARTING RULE SERVICES at" + new Date());
        System.out.println("*********************************");
        ruleBases.clear();
        
        //start the monitor service..
        InitialContext ctx =  new InitialContext();
        RuleMonitorLocal local = (RuleMonitorLocal)ctx.lookup("RuleMonitor/local");
        int delay = 10000;
        if( !monitor ) delay = -1;
        local.startMonitor(delay);
        
    }
    
    public List getRuleNames() {
        if(ruleNames == null) {
            ruleNames = new ArrayList();
            //loading all rule names
            //rules.conf file must exist to get the rule names.
            try {
                URL u = Thread.currentThread().getContextClassLoader().getResource("META-INF/rules.conf");
                if(u!=null) {
                    Scanner scanner = new Scanner(u.openStream());
                    while(scanner.hasNext()) {
                        String s = scanner.nextLine();
                        if(s!=null && !s.startsWith("#")) {
                            ruleNames.add(s);
                        }
                    }
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ruleNames;
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING RULE SERVICES.....");
        for(RuleBaseHolder rh: ruleBases.values()) {
            rh.destroy();
        }
        ruleBases.clear();
        JndiUtil.unbind(new InitialContext(),jndiName);
    }
    
    
    
    
    public void loadRules( String filterName ) throws Exception {
        if(filterName == null )
            throw new Exception("Load rules error. filterName must not be null");
        
        InitialContext ctx = new InitialContext();
        RuleBaseHolder ruleBaseHolder = new RuleBaseHolder(filterName);
        
        Iterator iter = com.sun.jmx.remote.util.Service.providers( RuleServiceProvider.class, Thread.currentThread().getContextClassLoader());
        while(iter.hasNext()) {
            RuleServiceProvider p = (RuleServiceProvider)iter.next();
            p.setContext(ctx);
            Enumeration<RuleSource> ruleSources = p.getRules(filterName);
            
            if( ruleSources != null ) {
                while(ruleSources.hasMoreElements()) {
                    RuleSource rs = ruleSources.nextElement();
                    ruleBaseHolder.addRules(rs);
                }
            }
        }
        
        if(ruleBases.containsKey(filterName)) {
            ruleBases.get(filterName).destroy();
            ruleBases.remove(filterName);
        }
        ruleBases.put(filterName, ruleBaseHolder);
    }
    
    public synchronized boolean isUpdating() {
        return updating;
    }
    
    public synchronized void setUpdating(boolean updating) {
        this.updating = updating;
    }
    
    public RuleBaseHolder getRuleBaseHolder(String ruleName) {
        if( ruleName == null )
            ruleName = "default";
        if( !ruleBases.containsKey(ruleName))
            throw new IllegalStateException("Rulebase " + ruleName + " does not exist");
        return ruleBases.get(ruleName);
    }

    public void loadAllRules() throws Exception {
        for(Object k: getRuleNames() ) {
            String key = (String)k;
            System.out.println("load rule " + key);
            loadRules(key);
        }        
    }

    public boolean isMonitor() {
        return monitor;
    }

    public void setMonitor(boolean monitor) {
        this.monitor = monitor;
    }
    
    
}
