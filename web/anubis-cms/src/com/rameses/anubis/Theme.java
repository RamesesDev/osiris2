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

/**
 *
 * @author Elmo
 */
public abstract class Theme extends HashMap {
    
    private Project project;
    
    protected abstract Map getInfo();
    public abstract InputStream getMasterResource(String name);
    public abstract InputStream getTemplateResource(String name);
    public abstract InputStream getResource(String name);
    
    public Theme(String name, String url) {
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
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    
}
