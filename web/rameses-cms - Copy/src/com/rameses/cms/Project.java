/*
 * ProjectManager.java
 *
 * Created on June 12, 2012, 9:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;


import com.rameses.cms.added.XPageManager;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class Project {
    
    private String name;
    private String url;
    private String title;
    
    private Map info = new HashMap();
    private Map<String,Theme> themes = new LinkedHashMap();
    private Map<String,WidgetPackage> packages = new LinkedHashMap();
    private Map<String,Module> modules = new LinkedHashMap();
    
    private String defaultTheme;
    
    private FileManager fileManager = new FileManager(this);
    private ContentManager contentManager = new ContentManager(this);
    private ServiceManager serviceManager = new ServiceManager(this);
    
    private Map vars = new HashMap();
    
    public Project(String name, String url) {
        this.name = name;
        this.url = url;
        this.title = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public Map getInfo() {
        return info;
    }
    
    public String getDefaultTheme() {
        return defaultTheme;
    }
    
    public void setDefaultTheme(String defaultTheme) {
        this.defaultTheme = defaultTheme;
    }
    /**************************************************************************
       INSTALLED THEMES
    **************************************************************************/
    public Map<String,Theme> getThemes() {
        return themes;
    }
    public Theme addTheme(String name, String url) {
        Theme theme = new Theme(name,url);
        return addTheme(theme);
    }

    public Theme addTheme(Theme theme) {
        if( !themes.containsKey(theme.getName())) {
            this.themes.put( theme.getName(), theme );
        }
        if(this.defaultTheme == null ) this.defaultTheme = theme.getName();
        return themes.get(theme.getName());
    }

    /**************************************************************************
      INSTALLED MODULES
    **************************************************************************/
    public Map<String,Module> getModules() {
        return modules;
    }
    
    public Module addModule(String name, String url) {
        Module mod = new Module(name, url);
        return addModule(mod);
    }
    
    public Module addModule(Module mod) {
        if(!this.modules.containsKey(mod.getName())) {
            this.modules.put(mod.getName(), mod);    
        }
        return this.modules.get( mod.getName() );
    }
    
    /**************************************************************************
      INSTALLED WIDGETS
    **************************************************************************/
    public Map<String,WidgetPackage> getWidgetPackages() {
        return packages;
    }
    
    public WidgetPackage addWidgetPackage(String name, String url) {
        WidgetPackage wp = new WidgetPackage(name, url);
        return addWidgetPackage(wp);
    }

    public WidgetPackage addWidgetPackage(WidgetPackage wp) {
        if(!this.packages.containsKey(wp.getName())) {
            this.packages.put(wp.getName(), wp );    
        }
        return this.packages.get(wp.getName());
    }
    
    
    
    
    public FileManager getFileManager() {
        return fileManager;
    }

    public ContentManager getContentManager() {
        return contentManager;
    }
    
     public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map getVars() {
        return vars;
    }
    
    
     /**************************************************************************
      Additional for test
    **************************************************************************/
    private XPageManager pageManager = new XPageManager(this);
    
    public XPageManager getPageManager() {
        return pageManager;
    }
    
    
    
    
}
