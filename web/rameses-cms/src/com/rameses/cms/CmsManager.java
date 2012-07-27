/*
 * CmsManager.java
 *
 * Created on June 28, 2012, 8:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class CmsManager {
    
    private Map conf;
    private ProjectFactory factory;
    private Map<String,Project> projects = new Hashtable();
    private boolean cached = true;
    
    public CmsManager(Map c, ClassLoader loader) {
        try {
            this.conf = c;
            String cms_class = (String)conf.get("cms_factory_class");
            if( cms_class == null ) cms_class = "com.rameses.cms.impl.DefaultProjectFactory";
            if(loader==null) loader = getClass().getClassLoader();
            factory = (ProjectFactory)loader.loadClass(cms_class).newInstance();
            if( conf.containsKey("cached")) {
                try {
                    cached = Boolean.parseBoolean( conf.get("cached").toString() );
                } catch(Exception ign){;}
            }
            
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    public Project getProject(String name) {
        if( name == null ) {
            name = (String) conf.get("default_project");
        }
        if( projects.containsKey(name) ) return projects.get(name);
        String projectUrl = (String)conf.get("cms_root_url");
        Project project = factory.create( name, projectUrl + "/" + name );
        if( cached ) projects.put( name, project );
        return project;
    }
    
    //finds the project given the name path. for example project1.mysite.com
    /*
    public Project findProject(String name, ProjectNamingScheme scheme) {
        if(scheme == null ) {
            String domain = (String)conf.get("project_domain");
            if(domain==null) 
                    throw new RuntimeException("Please provide a project_domain entry in conf or provide a naming scheme");
            scheme = new DefaultProjectNamingScheme( domain );
        }
        String correctedName = scheme.extract( name );
        return getProject(name);
    }
    
    public static interface ProjectNamingScheme {
        String extract(String name);
    }
    
    public static class DefaultProjectNamingScheme implements ProjectNamingScheme  {
        private String domainName;
        
        public DefaultProjectNamingScheme(String n) {
            this.domainName = n;
        }
        public String extract(String name) {
            if( name.endsWith(domainName) ) {
                
            }
        }
    }
    */
}
