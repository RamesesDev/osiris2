/*
 * ProjectResolver.java
 *
 * Created on July 16, 2012, 10:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import com.rameses.anubis.Module;
import com.rameses.anubis.NameParser;
import com.rameses.anubis.NameParser.MatchResult;
import com.rameses.anubis.Project;
import com.rameses.anubis.Theme;
import com.rameses.anubis.custom.CustomProject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class ProjectResolver {
    
    private boolean cached;
    private List<NameParser> nameParsers = new ArrayList();
    private Map<String, Project> projects = new Hashtable();
    
    private Module defaultModule;
    private Theme defaultTheme;
    
    /** Creates a new instance of ProjectResolver */
    public ProjectResolver(Map conf) {
        for(Object o : conf.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            NameParser np = new NameParser(me.getKey().toString(), me.getValue().toString());
            nameParsers.add( np );
        }
    }
    
    
    public Project findProject(String serverName) {
        NameParser np = findNameParser(serverName);
        MatchResult nr = np.buildResult( serverName );
        return getProjectFromUrl( nr.getResolvedPath());
        //return getProjectFromUrl( np.getResolvedTargetPath( serverName ));
    }
    //return only the first one that matches
    public NameParser findNameParser(String path) {
        for(NameParser np : nameParsers ) {
            if( np.matches(path)) return np;
        }
        throw new RuntimeException("Path does not match any registered patterns");
    }
    
    
    public Project getProjectFromUrl(String urlName) {
        if( projects.containsKey(urlName)) return projects.get(urlName);
        //project name is gotten from the last substring token
        String name = urlName.substring(urlName.lastIndexOf("/")+1);
        CustomProject project = new CustomProject(name, urlName);
        if(defaultModule!=null) project.setSystemModule( defaultModule );
        if( defaultTheme !=null) project.setSystemTheme( defaultTheme );
        if(cached) {
            projects.put( urlName, project );
        }
        return project;
    }
    
    public void setDefaultModule(Module defaultModule) {
        this.defaultModule = defaultModule;
    }
    
    public void setDefaultTheme(Theme defaultTheme) {
        this.defaultTheme = defaultTheme;
    }
      
    public boolean isCached() {
        return cached;
    }
    
    public void setCached(boolean cached) {
        this.cached = cached;
    }
    
}
