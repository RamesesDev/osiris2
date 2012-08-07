/*
 * CustomProject.java
 *
 * Created on July 4, 2012, 1:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.ActionManager;
import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.FileDir;
import com.rameses.anubis.FileDir.FileFilter;
import com.rameses.anubis.FileManager;
import com.rameses.anubis.LocaleSupport;
import com.rameses.anubis.Module;
import com.rameses.anubis.NameParser;
import com.rameses.anubis.Project;
import com.rameses.anubis.ServiceManager;
import com.rameses.anubis.Theme;
import com.rameses.io.LineReader;
import com.rameses.io.LineReader.Handler;
import java.io.InputStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class CustomProject extends Project {
    
    private ServiceManager serviceManager;
    private FileManager fileManager;
    private ActionManager actionManager;
    private Map<String,Theme> themes;
    private Map<String,Module> modules;
    
    
    private List<NameParser> urlMapping;
    
    public CustomProject(String id, String url) {
        super(id, url);
        fileManager = new CustomFileManager(this);
        serviceManager = new CustomServiceManager(this);
        actionManager = new CustomActionManager(this);
    }
    
    public Map getInfo() {
        String path = getUrl() +"/project.conf";
        return  ContentUtil.getJsonMap(path);
    }
    
    public FileManager getFileManager() {
        return fileManager;
    }
    
    public ServiceManager getServiceManager() {
        return serviceManager;
    }
    
    public ActionManager getActionManager() {
        return actionManager;
    }
    
    public Map<String, Theme> getThemes() {
        if( themes == null) {
            themes = new LinkedHashMap();
            FileDir.scan(super.getUrl() + "/themes", new FileFilter(){
                public void handle(FileDir.FileInfo f) {
                    URL conf = f.getSubfile("theme.conf");
                    Theme theme = new CustomTheme(f.getName(), f.getUrl().toString());
                    theme.setProject( CustomProject.this );
                    themes.put(theme.getName(), theme);
                }
            });
        }
        return themes;
    }
    
    public Map<String, Module> getModules() {
        if( modules == null ) {
            modules = new LinkedHashMap();
            FileDir.scan(super.getUrl() + "/modules", new FileFilter(){
                public void handle(FileDir.FileInfo f) {
                    URL conf = f.getSubfile("module.conf");
                    if(conf!=null) {
                        Module module = new CustomModule(f.getName(), f.getUrl().toString());
                        module.setProject(CustomProject.this);
                        modules.put(module.getName(), module);
                    }
                }
            });
        }
        return modules;
    }
    
    protected LocaleSupport loadLocaleSupport(String lang) {
        return new CustomLocaleSupport(lang, this);
    }
    
    public List<NameParser> getUrlMapping() {
        if(urlMapping!=null) return urlMapping;
        urlMapping = new ArrayList();
        FileDir.scan(super.getUrl() + "/urlmapping", new FileFilter(){
            public void handle(FileDir.FileInfo f) {
                LineReader rd=new LineReader();
                final String fileName = "/" + f.getFileName().replace(".", "/") + ".pg";
                InputStream is = null;
                try {
                    is = ContentUtil.findResource( f.getUrl().toString() );
                    rd.read( is, new Handler(){ 
                        public void read(String text) {
                            urlMapping.add( new NameParser(text.trim(),fileName) );
                        }
                    });
                }
                catch(Exception ign){;}
                finally {try{is.close();}catch(Exception e){;}}
            }
        });
        return urlMapping;
    }
    
    
    
}
