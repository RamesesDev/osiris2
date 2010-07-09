package com.rameses.rules;

import java.io.Reader;
import java.io.Serializable;
import java.util.Map;
import org.drools.RuleBase;
import org.drools.RuleBaseConfiguration;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;


public class RuleBaseHolder implements Serializable {
    
    private String name;
    private RuleClassLoader ruleClassloader;
    private RuleBase rulebase;
    private PackageBuilderConfiguration conf;
    private RuleBaseConfiguration rbConf;
    
    public RuleBaseHolder(String name) {
        this.name = name;
        conf = new PackageBuilderConfiguration();
        conf.setCompiler( PackageBuilderConfiguration.JANINO );
        ruleClassloader = new RuleClassLoader( name, Thread.currentThread().getContextClassLoader() );
        conf.setClassLoader( ruleClassloader );
        Thread.currentThread().setContextClassLoader(ruleClassloader);
        
        /**----------------------------------------------------------------------------*
         * ===  suggested code as workaround to drools bug described below ===
         * ( Edson Tirelli - 29/Jul/07 11:33 AM )
         * [ https://jira.jboss.org/jira/browse/JBRULES-1034 ]
         * This was a problem in the alpha node hashing feature that was not correctly 
         * handling nulls. It only shows up with 3 or more rules because the default 
         * value for start hashing nodes is 3.
         *-----------------------------------------------------------------------------*/
        rbConf = new RuleBaseConfiguration();
        rbConf.setAlphaNodeHashingThreshold( 999 );
        
        rulebase = RuleBaseFactory.newRuleBase( RuleBase.RETEOO, rbConf );
    }
    
    public Object createFact(String c) {
        try {
            Class clazz = ruleClassloader.loadClass( c );
            return clazz.newInstance();
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }
    
    public void addRules( RuleSource rs ) {
        try {
            PackageBuilder builder = new PackageBuilder(conf);
            builder.addPackageFromDrl( rs.getSource() );
            org.drools.rule.Package pkg = builder.getPackage();
            rulebase.addPackage( pkg );
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public void destroy() {
        rulebase = null;
        conf = null;
        ruleClassloader.flushAll();
        ruleClassloader = null;
    }
    
    public void execute(Object facts[], String agenda) {
        execute( facts, agenda, null );
    }
    
    public void execute(Object facts[], String agenda, Map globals) {
        Thread.currentThread().setContextClassLoader(ruleClassloader);
        WorkingMemory wm = null;
        try {
            wm = rulebase.newWorkingMemory();
            if( agenda != null ) wm.setFocus(agenda);
            for(Object o : facts ) {
                wm.assertObject( o );
            }
            if(globals!=null) {
                for(Object o: globals.entrySet()) {
                    Map.Entry me = (Map.Entry)o;
                    wm.setGlobal(me.getKey()+"", me.getValue());
                }
            }
            
            
            wm.fireAllRules();
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        } finally {
            try { wm.dispose(); } catch(Exception ign){;}
        }
    }
    
    
    //this method checks for existing packages. loops existing and renews the rulebase
    public void addPackage( Reader rdr ) {
        try {
            PackageBuilder builder = new PackageBuilder(conf);
            builder.addPackageFromDrl(rdr);
            org.drools.rule.Package newPackage = builder.getPackage();
            
            RuleBase rb = RuleBaseFactory.newRuleBase( RuleBase.RETEOO, rbConf );
            rb.addPackage(newPackage);
            for( org.drools.rule.Package p : rulebase.getPackages() ) {
                if( !p.getName().equals(newPackage.getName()) ) {
                    rb.addPackage(p);
                }
            }
            //replace the rulebase
            rulebase = rb;
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    //this method checks for existing packages. loops existing and renews the rulebase
    public void removePackage( Reader rdr ) {
        try {
            PackageBuilder builder = new PackageBuilder(conf);
            builder.addPackageFromDrl(rdr);
            org.drools.rule.Package newPackage = builder.getPackage();
            
            RuleBase rb = RuleBaseFactory.newRuleBase( RuleBase.RETEOO, rbConf );
            for( org.drools.rule.Package p : rulebase.getPackages() ) {
                if( !p.getName().equals(newPackage.getName()) ) {
                    rb.addPackage(p);
                }
            }
            
            //replace the rulebase
            rulebase = rb;
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
}
