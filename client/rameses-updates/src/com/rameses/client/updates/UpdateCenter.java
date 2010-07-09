/*
 * UpdateCenter.java
 *
 * Created on November 24, 2009, 9:00 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.updates;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class UpdateCenter {
    
    private String appurl;
    private String appPath = System.getProperty("user.dir")+"/osiris2/modules";
    private Map env;
    private URL[] urls;
    
    public UpdateCenter(String appurl) {
        this.appurl = appurl;
    }
    
    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }
    
    public String getAppPath() {
        return appPath;
    }
    
    public void start() throws Exception {
        System.out.println("starting update");
        UpdateConf conf = new UpdateConf(appurl,appPath);
        conf.init();
        
        boolean needsUpdate = true;
        List<ModuleEntry> oldModules = new ArrayList<ModuleEntry>();
        if( conf.exists()) {
            if( conf.isIncomplete() ) {
                conf.revert();
            }
            conf.load();
            if( conf.hasUpdates() ) {
                oldModules = conf.getOldModules();
                conf.download();
            } 
            else {
                needsUpdate = false;
            }
        } else {
            conf.download();
        }
        
        List<ModuleEntry> newModules = conf.getModules();
        
        if( needsUpdate) {
            for(ModuleEntry me: newModules) {
                int idx = oldModules.indexOf(me);
                if(idx>=0) {
                    ModuleEntry oldEntry = oldModules.get(idx);
                    if(oldEntry.getVersion()<me.getVersion()) {
                        me.setState(Status.MODIFY);
                    }
                    oldModules.remove(idx);                    
                } 
                else {
                    me.setState(Status.CREATE);
                }
            }
            
            //REMOVE ALL OLD MODULES
            for(ModuleEntry old: oldModules) {
                old.delete();
            }
            
            for(ModuleEntry me: newModules) {
                me.update();
            }
            conf.complete();
        }
        
        env = conf.getEnv();
        urls = new URL[newModules.size()];
        int i = 0;
        for(ModuleEntry me: newModules) {
            urls[i++] = me.getURL();
        }
    }
    
    public ClassLoader getClassLoader( ClassLoader loader ) {
        if( urls == null ) {
            return loader;
        } else {
            return  new URLClassLoader(urls, loader);
        }
    }
    
    public Map getEnv() {
        return env;
    }
    
    
}
