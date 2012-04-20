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
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private String rulegroup;
    private KnowledgeBase kbase;
    private Properties properties;
    
    public KnowledgeSet( String name, String rulegroup, Properties props ) {
        this.name = name;
        this.rulegroup = rulegroup;
        this.properties = props;
    }
    
    public String getName() {
        return name;
    }
    
    public void load(SqlContext ctx) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(" .... Loading rule set " + getKey() );
        KnowledgeBuilder builder = createKnowledgeBuilder();
        DrlResourceLoader dl = new DrlResourceLoader(builder, name, rulegroup, ctx);
        dl.load();
        if ( builder.hasErrors() ) {
            System.err.println( builder.getErrors().toString() );
        }
        kbase = builder.newKnowledgeBase();
    }
    
    public void deployPackage(String packageName, Object o, SqlContext ctx) throws Exception {
        String content = o.toString();
        //insert the ruleset if not yet exists
        SqlExecutor se = ctx.createNamedExecutor("ruleserver:add-rule-set");
        se.setParameter("ruleset", name);
        se.setParameter("rulegroup", rulegroup);
        se.execute();
        
        //insert the rule package
        se = ctx.createNamedExecutor("ruleserver:add-rule-package");
        se.setParameter("ruleset", name);
        se.setParameter("rulegroup", rulegroup);
        se.setParameter("packagename", packageName);
        se.setParameter("content", content);
        se.execute();
        
        //retrieve the facts
        Map params = new HashMap();
        params.put("ruleset", name );
        params.put( "rulegroup", rulegroup );
        params.put("packagename", packageName);
        SqlQuery qry = ctx.createNamedQuery("ruleserver:get-facts");
        List<Map> result = qry.setParameters( params ).getResultList();
        
        //compile the package. Must add the fact rules first.
        KnowledgeBuilder builder = createKnowledgeBuilder();
        for( Map m : result) {
            String _fact = (String)m.get("content");
            builder.add( ResourceFactory.newByteArrayResource(  _fact.getBytes() ), ResourceType.DRL);
        }
        builder.add( ResourceFactory.newByteArrayResource(  content.getBytes() ), ResourceType.DRL);

        //find the last package and add it to the current knowledge base.
        Iterator<KnowledgePackage> iter =  builder.getKnowledgePackages().iterator();
        KnowledgePackage kp = null;
        while( iter.hasNext() ) {kp=iter.next();}
        Collection<KnowledgePackage> list = new ArrayList();
        list.add( kp );
        kbase.addKnowledgePackages( list );
    }
    
    public void undeployPackage(String packageName, SqlContext ctx) throws Exception {
        //removes package to the database and remove from current knowledge base.
        //adds package to the database
        SqlExecutor se = ctx.createNamedExecutor("ruleserver:remove-rule-package");
        se.setParameter("ruleset", name);
        se.setParameter("rulegroup", rulegroup);
        se.setParameter("packagename", packageName);
        se.execute();
        kbase.removeKnowledgePackage( packageName );
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
    
    public void destroy() {
        clear(kbase);
    }
    
    
    public int hashCode() {
        return  getKey().hashCode();
    }
    
    public boolean equals(Object obj) {
        KnowledgeSet ks = (KnowledgeSet)obj;
        if(ks==null) return false;
        return hashCode() == ks.hashCode();
    }
    
    public String getRulegroup() {
        return rulegroup;
    }
    
    public String getKey() {
        if( rulegroup == null || rulegroup.trim().length()== 0 )
            return getName();
        else
            return getName() + ":" + getRulegroup();
    }
    
}
