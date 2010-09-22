/*
 * ResourceLoader.java
 *
 * Created on September 15, 2010, 6:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.ruleserver;

import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlQuery;
import com.rameses.util.URLDirectory;
import com.rameses.util.URLDirectory.URLFilter;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

/**
 *
 * @author elmo
 */
public class DrlResourceLoader implements URLFilter {
    
    private static final String ROOT_URL = "META-INF/rules/";
    private KnowledgeBuilder builder;
    private String name;
    private String rulegroup;
    private SqlContext sqlContext;
    
    public DrlResourceLoader(KnowledgeBuilder b,  String name, String rulegroup, SqlContext ctx) {
        this.builder = b;
        this.name = name;
        this.rulegroup = rulegroup;
        this.sqlContext = ctx;
    }
    
    private void loadFromClasspath(String type) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> e = classLoader.getResources( ROOT_URL + name + "/" + type + "/" );
        while(e.hasMoreElements()) {
            URL subUrl = e.nextElement();
            URLDirectory ud = new URLDirectory(subUrl);
            ud.list( this, classLoader );
        }
    }
    
    public boolean accept(URL u, String filter) {
        if(filter.endsWith(".drl")) {
            try {
                builder.add(ResourceFactory.newInputStreamResource(u.openStream()), ResourceType.DRL);
            } catch(Exception ex) {
                System.out.println("ERROR LOADING RULE RESOURCE. " + filter + ". " + ex.getMessage());
            }
        }
        return false;
    }
    
    public void loadFromDb(  String type ) throws Exception {
        if(sqlContext!=null) {
            SqlQuery qry = sqlContext.createNamedQuery("ruleserver:list-by-type");
            qry.setParameter("name", name);
            qry.setParameter("type",type);
            qry.setParameter("rulegroup",rulegroup);
            List<Map> list = qry.getResultList();
            for(Map o: list) {
                String str = (String)o.get("content");
                builder.add( ResourceFactory.newByteArrayResource(  str.getBytes() ), ResourceType.DRL);
            }
        }
    }
    
    
    public void load() throws Exception {
        loadFromDb("facts");
        loadFromClasspath( "facts" );
        loadFromDb( "rules" );
        loadFromClasspath( "rules" );
    }
    
}
