/*
 * CustomTemplatePageContentProvider.java
 *
 * Created on July 17, 2012, 10:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.PageContentMap;
import com.rameses.anubis.PageFileInstance;
import com.rameses.anubis.Project;
import com.rameses.anubis.TemplateContentProvider;
import com.rameses.anubis.Theme;
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
    
    public String getContent(String name, PageFileInstance pi, PageContentMap map) {
        try {
            return super.getContent(name,pi,map);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected InputStream getResource(String name, PageFileInstance page) {
        
        //CHECK GLOBAL / SHARED BLOCKS
        Project project = page.getProject();
        
        
        InputStream is = null;
        Theme theme = page.getTheme();
        is = theme.getTemplateResource( name );
        if( is != null ) return is;
        
        is = project.getSystemTheme().getTemplateResource( name );
        return is;
    }
    
    
}
