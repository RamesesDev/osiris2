/*
 * KnowledgeHolder.java
 *
 * Created on September 15, 2010, 11:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.ruleserver;

import com.rameses.sql.SqlContext;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Properties;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;

/**
 *
 * @author elmo
 */
public final class KnowledgeSet implements Serializable {
    
    private String name;
    private KnowledgeBase kbase;
    private Properties properties;
    
    public KnowledgeSet( String name, Properties props ) {
        this.name = name;
        this.properties = props;
    }
    
    public String getName() {
        return name;
    }
    
    public void load(SqlContext ctx) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(" .... Loading rule set " + name );
        KnowledgeBuilder builder = createKnowledgeBuilder();
        DrlResourceLoader dl = new DrlResourceLoader(builder, name,ctx);
        dl.load();
        if ( builder.hasErrors() ) {
            System.err.println( builder.getErrors().toString() );
        }
        kbase = builder.newKnowledgeBase();
    }
    
    private KnowledgeBuilder createKnowledgeBuilder() {
        final Properties properties = new Properties();
        properties.setProperty( "drools.dialect.java.compiler", "JANINO" );
        KnowledgeBuilderConfiguration conf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(properties);
        return KnowledgeBuilderFactory.newKnowledgeBuilder(conf);
    }
    
    public KnowledgeBase getKnowledgeBase() {
        return kbase;
    }
    
    public void clear(KnowledgeBase kb) {
        Collection<KnowledgePackage> list = kb.getKnowledgePackages();
        for(KnowledgePackage kp : kb.getKnowledgePackages() ) {
            kb.removeKnowledgePackage(kp.getName());
        }
    }
    
    public void addPackage(InputStream is) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        KnowledgeBuilder builder = createKnowledgeBuilder();
        DrlResourceLoader dl = new DrlResourceLoader(builder,name,null);
        dl.load();
        builder.add( ResourceFactory.newInputStreamResource(is), ResourceType.DRL);
        kbase.addKnowledgePackages( builder.getKnowledgePackages() );
    }
    
    public void destroy() {
        clear(kbase);
    }
    
    public KnowledgeSet clone() {
        return new KnowledgeSet(name, properties);
    }

    public int hashCode() {
        return  name.hashCode();
    }

    public boolean equals(Object obj) {
        KnowledgeSet ks = (KnowledgeSet)obj;
        if(ks==null) return false;
        return hashCode() == ks.hashCode();
    }
 

    
}
