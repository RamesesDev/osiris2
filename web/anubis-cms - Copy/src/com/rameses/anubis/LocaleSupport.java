/*
 * LocaleManager.java
 *
 * Created on July 16, 2012, 9:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class LocaleSupport {
    
    protected Project project;
    private String lang;
    
    protected abstract Map getResourceFile();
    public abstract  BlockContentProvider getBlockContentProvider();
    
    public LocaleSupport(String lang, Project project) {
        this.project = project;
        this.lang = lang;
    }
   

    public String getLang() {
        return lang;
    }
    
    //called by labels, etc.
    public String translate(String key, String value) {
        String _v = (String) getResourceFile().get(key);
        
        if( _v !=null && _v.trim().length()>0) {
            return _v;
        }
        else if(_v==null && value!=null) {
            //try lowercased values where spaces are replaced with "_"
            String skey = value.replaceAll("\\s{1,}", "_").toLowerCase();
            _v = (String)getResourceFile().get(skey);
        }
        return null;
    }
    
}
