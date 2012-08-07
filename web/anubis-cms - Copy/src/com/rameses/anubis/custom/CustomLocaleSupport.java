/*
 * CustomLocaleManager.java
 *
 * Created on July 16, 2012, 9:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.BlockContentProvider;
import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.FileDir;
import com.rameses.anubis.FileDir.FileFilter;
import com.rameses.anubis.LocaleSupport;
import com.rameses.anubis.PageFileInstance;
import com.rameses.anubis.Project;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class CustomLocaleSupport extends LocaleSupport {
    
    private final static String I18N_DIR = "/i18n/";
    private final static String RES_DIR = "/resources";
    private BlockContentProvider blockContentProvider;
    private Map translations;
    
    
    public CustomLocaleSupport(String lang, Project project) {
        super(lang, project);
        this.blockContentProvider = new LocaleBlockContentProvider();
    }
    
    public BlockContentProvider getBlockContentProvider() {
        return blockContentProvider;
    }
    
    protected Map getResourceFile() {
        if(translations!=null) return translations;
        translations = new HashMap();
        FileDir.scan( super.project.getUrl()+I18N_DIR+getLang()+RES_DIR, new FileFilter(){
            public void handle(FileDir.FileInfo f) {
                translations.putAll( ContentUtil.getProperties( f.getUrl() ));
            }
            
        });
        return translations;
    }
    
    private class LocaleBlockContentProvider extends BlockContentProvider {
        protected InputStream getResource(String name, PageFileInstance page) {
            return ContentUtil.findResource( project.getUrl() + I18N_DIR +getLang()+"/pages"+name );
        }
    }
}
