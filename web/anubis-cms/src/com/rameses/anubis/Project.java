/*
 * Project.java
 *
 * Created on July 1, 2012, 2:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class Project extends HashMap  {
    
    
    public abstract FileManager getFileManager();
    public abstract ServiceManager getServiceManager();
    public abstract ActionManager getActionManager();
    public abstract List<NameParser> getUrlMapping();
    
    private Module systemModule;
    private Theme systemTheme;
    
    public abstract Map getInfo();
    public abstract Map<String,Theme> getThemes();
    public abstract Map<String,Module> getModules();
    
    
    protected abstract LocaleSupport loadLocaleSupport(String name);
    
    /** Creates a new instance of Project */
    protected Project(String id, String url) {
        super.put("name", id);
        super.put("url", url);
        Map map = getInfo();
        super.putAll( map );
    }
    
    public String getName() {
        return (String)super.get("name");
    }
    
    public String getUrl() {
        return (String)super.get("url");
    }
    
    public Module getSystemModule() {
        return this.systemModule;
    }
    
    public void setSystemModule(Module defaultModule) {
        this.systemModule = defaultModule;
    }
    
    public Theme getSystemTheme() {
        return this.systemTheme;
    }
    
    public void setSystemTheme(Theme defaultTheme) {
        this.systemTheme = defaultTheme;
    }
    
    public String getTitle() {
        return (String)get("title");
    }
    
    public Theme getDefaultTheme() {
        String themeName = (String)super.get("theme");
        if(themeName==null) return null;
        return getThemes().get(themeName);
    }
    
    /**************************************************************************
     * LOCALE MANAGER
     **************************************************************************/
    private Map<String, LocaleSupport> locales = new Hashtable();
    
    public LocaleSupport getLocaleSupport(String lang) {
        if(locales.containsKey(lang)) return locales.get(lang);
        LocaleSupport locale = loadLocaleSupport(lang);
        locales.put(lang, locale);
        return locale;
    }
    
    //TRANSLATED GET TEXT
    private static String IGNORE_LANG_FIELDS = "name|url|defaultTheme";
    
    public Object get(Object key) {
        if(! key.toString().matches(IGNORE_LANG_FIELDS)) {
            LocaleSupport locale = AnubisContext.getCurrentContext().getCurrentLocale();
            if(locale!=null) {
                String _key = "project."+key;
                String val = (String)locale.getResourceFile().get(_key);
                if(val!=null && val.trim().length()>0) return val;
            }
        }
        return super.get(key);
    }
    
    
}
