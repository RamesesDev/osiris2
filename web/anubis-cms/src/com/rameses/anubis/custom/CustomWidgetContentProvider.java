/*
 * CustomWidgetPageContentProvider.java
 *
 * Created on July 17, 2012, 10:41 AM
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
import com.rameses.anubis.WidgetContentProvider;
import java.io.InputStream;

/**
 *
 * @author Elmo
 * there are two ways in accessing widgets: without namespace or with namespace
 * without namespace :
 *   Locate the widget in the ff. order:
 *   1. if page has module, check in module local folder
 *   2. if page has module, check in module provider
 *   3. check in project
 *   4. check in system
 *
 * with namespace:
 *   1. locate in module local folder
 *   2. locate in provider
 */
public class CustomWidgetContentProvider extends WidgetContentProvider {
    
    private static final String WIDGET_DIR =  "content/widgets";
    
    public String getContent(String name, PageFileInstance pi, PageContentMap map) {
        try {
            return super.getContent(name, pi, map);
        } catch(Exception ign){
            String msg = ign.getMessage();
            return "<font color=red>" + "error on widget:"+name + ":"+ msg + "</font>";
        }
    }
    
    protected InputStream getResource(String id, PageFileInstance page) {
        Project project = page.getProject();
        
        String namespace = null;
        String widgetName = id + ".wgt/code";
        if( id.indexOf(":")> 0 ) {
            String[] arr = widgetName.split(":");
            namespace = arr[0];
            widgetName = arr[1];
        }
        
        InputStream is = null;
        if( namespace == null ) {
            Module mod = page.getModule();
            if( mod !=null) {
                is = mod.getWidgetResource( widgetName );
                if(is!=null) return is;
            }
            
            is = ContentUtil.findResource( project.getUrl(), WIDGET_DIR, widgetName );
            //get default
            if(is==null) {
                Module defaultMod = project.getSystemModule();
                if( defaultMod !=null) {
                    is = defaultMod.getWidgetResource( widgetName );
                }
            }
        } else {
            Module module = project.getModules().get(namespace);
            if(module!=null) {
                is = module.getWidgetResource( widgetName );
            }
        }
        return is;
    }
    
    
}
