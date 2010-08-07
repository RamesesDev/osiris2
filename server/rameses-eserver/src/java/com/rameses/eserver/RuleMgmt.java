/*
 * RuleMgmt.java
 *
 * Created on July 26, 2010, 12:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.interfaces.RuleDomain;
import com.rameses.interfaces.RuleDomainProvider;
import com.sun.jmx.remote.util.Service;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;

/**
 *
 * @author elmo
 */
public class RuleMgmt implements RuleMgmtMBean, Serializable {
    
    private String jndiName;
    private Map<String, RuleDomain> ruleDomains = new Hashtable();
    
    public RuleMgmt() {
    }
    
    public void start() throws Exception {
        System.out.println("STARTING RULE MANAGEMENT [" + jndiName +"]" );
        InitialContext ctx = new InitialContext();
        JndiUtil.bind( ctx,jndiName,this );
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Iterator iter = Service.providers(RuleDomainProvider.class, classLoader);
        ruleDomains.clear();
        while(iter.hasNext()) {
            RuleDomainProvider dp = (RuleDomainProvider)iter.next();
            for(RuleDomain d: dp.getRuleDomains()) {
                ruleDomains.put( d.getName(), d );
                d.load();
            }
        }
    }

    public void stop() throws Exception {
        System.out.println("STOPPING RULE MANAGEMENT [" + jndiName +"]" );
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx,jndiName );
    }

    public void deploy() {
    }
    
    public void deploy(String ruleBaseName) {
        
    }
    
    public String getJndiName() {
        return jndiName;
    }
    
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public Object createObject(String name, String className) {
        RuleDomain rd = ruleDomains.get(name);
        return rd.createObject(className);
    }
    
    
    public void reload(String name) {
        try {
            RuleDomain rp = ruleDomains.get(name);
            rp.load();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void fireRule(String ruleBaseName, List facts) {
        try {
            RuleDomain ws = ruleDomains.get(ruleBaseName);
            ws.execute(facts,null,null);
        } catch(Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    
    
    
    
}
