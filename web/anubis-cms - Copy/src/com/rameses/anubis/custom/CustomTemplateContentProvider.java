/*
 * CustomTemplatePageContentProvider.java
 *
 * Created on July 17, 2012, 10:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.Module;
import com.rameses.anubis.PageContentMap;
import com.rameses.anubis.PageFileInstance;
import com.rameses.anubis.Project;
import com.rameses.anubis.TemplateContentProvider;
import java.io.InputStream;

/**
 *
 * @author Elmo
 * there are two ways in accessing templates: without namespace or with namespace
 * without namespace :
 *   Locate the template in the ff. order:
 *   1. if page has module, check in module local folder else in module provider
 *   2. check in project
 *   3. check in system
 *
 * with namespace:
 *   1. locate in module local folder
 *   2. locate in provider
 */

public class CustomTemplateContentProvider extends TemplateContentProvider {
    
    private static final String TEMPLATE_DIR =  "/content/templates/";
    
    
    public String getContent(String name, PageFileInstance pi, PageContentMap map) {
        try {
            return super.getContent(name,pi,map);
        } catch(Exception ign) {
            return "<font color=red>template " + name + " not found</font>";
        }
    }
    
    protected InputStream getResource(String name, PageFileInstance page) {
        
        //CHECK GLOBAL / SHARED BLOCKS
        Project project = page.getProject();
        
        String namespace = null;
        String templateName = name;
        if( name.indexOf(":")> 0 ) {
            String[] arr = name.split(":");
            namespace = arr[0];
            templateName = arr[1];
        }
        
        InputStream is = null;
        if( namespace == null ) {
            Module mod = page.getModule();
            if( mod !=null ) {
                is = mod.getTemplateResource( templateName );
                if(is!=null) return is;
            }
            
            is = ContentUtil.findResource( project.getUrl() + TEMPLATE_DIR + templateName );
            //get default
            if(is==null) {
                Module defaultMod = project.getSystemModule();
                if( defaultMod!=null) {
                    is = defaultMod.getTemplateResource( templateName );
                }
            }
        } else {
            Module module = project.getModules().get(namespace);
            if(module!=null) {
                is =module.getTemplateResource(templateName);
            }
        }
        return is;
    }
    
    
}
