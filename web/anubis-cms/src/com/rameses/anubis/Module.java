/*
 * ThemeManager.java
 *
 * Created on June 12, 2012, 9:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Elmo
 */
public abstract class Module extends HashMap {

    private Project project;
    
    protected abstract Map getInfo();
    public abstract Map getFileSource(String name);
    public abstract Set<String> getFolderItems(String name);
    
    public abstract InputStream getBlockResource(String name);
    public abstract InputStream getGlobalBlockResource(String name);
    public abstract InputStream getResource(String name);
    public abstract InputStream getActionResource(String name);
   
    public abstract InputStream getWidgetResource(String name);
    public abstract InputStream getLayoutResource(String name);
    
    public abstract InputStream getServiceAdapterSource(String name);
    
    public Module(String name, String url) {
       super.put("name",name );
        super.put("url", url);
        Map m = getInfo();
        if(m!=null) super.putAll(m);
    }
    
    public String getName() {
        return (String)super.get("name");
    }
    
    public String getUrl() {
        return (String)super.get("url");
    }
    
    public String getProvider() {
        return  (String)super.get("provider");
    }
    
    public String getLicenseKey() {
        return (String)super.get("licenseKey");
    }
    
    public String getDefaultMaster() {
        String s =  (String)super.get("master");
        if(s==null) return "default";
        return s;
    }
    
    public Theme getDefaultTheme() {
        String s= (String)super.get("theme");
        if(s==null) return null;
        return project.getThemes().get(s);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    
}
