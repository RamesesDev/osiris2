/*
 * DefaultProjectFactory.java
 *
 * Created on June 22, 2012, 10:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.impl;

import com.rameses.cms.ContentHandler;
import com.rameses.cms.FileHandler;
import com.rameses.cms.FileInfoProvider;
import com.rameses.cms.JsonUtil;

import com.rameses.cms.MediaFileHandler;
import com.rameses.cms.Module;
import com.rameses.cms.PageFileHandler;
import com.rameses.cms.Project;
import com.rameses.cms.ProjectFactory;
import com.rameses.cms.ServiceHandler;
import com.rameses.cms.Theme;
import com.rameses.cms.WidgetPackage;
import com.rameses.cms.service.HttpServiceHandler;
import com.rameses.cms.service.ScriptServiceHandler;
import com.rameses.io.StreamUtil;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class DefaultProjectFactory extends ProjectFactory {
    
    public void init(Project project) {
        InputStream is = null;
        try {
            is = new URL(project.getUrl()+ "/project.conf").openStream();
            Map map = JsonUtil.toMap( StreamUtil.toString(is) );
            
            if( map.containsKey("title")) {
                project.setTitle( (String)map.get("title") );
            }
            if( map.containsKey("defaultTheme")) {
                project.setDefaultTheme( (String)map.get("defaultTheme") );
            }
            
            //loading themes
            Map _themes = (Map)map.get("themes");
            if(_themes!=null) {
                for( Object m : _themes.entrySet() ) {
                    Map.Entry me = (Map.Entry)m;
                    Theme theme = new Theme( me.getKey().toString(), me.getValue().toString() );
                    project.addTheme( theme );
                }
            }
            
            //loading modules
            Map _modules = (Map)map.get("modules");
            if(_modules!=null) {
                for( Object m : _modules.entrySet() ) {
                    Map.Entry me = (Map.Entry)m;
                    Module module = new Module( me.getKey().toString(), me.getValue().toString() );
                    project.addModule( module );
                }
            }
            
            //loading widget packages
            Map _widgets = (Map)map.get("widgets");
            if(_widgets!=null) {
                for( Object m : _modules.entrySet() ) {
                    Map.Entry me = (Map.Entry)m;
                    WidgetPackage wp = new WidgetPackage( me.getKey().toString(), me.getValue().toString() );
                    project.addWidgetPackage( wp );
                }
            }
            
        } catch(Exception ign) {
            ign.printStackTrace();
        }
        
    }
    
    
    public FileInfoProvider getFileInfoProvider(Project project) {
        return new DefaultFileInfoProvider( project.getUrl()+"/files" );
    }
    
    public List<FileHandler> getFileHandlers() {
        List list = new ArrayList();
        list.add( new PageFileHandler() );
        list.add( new MediaFileHandler() );
        return list;
    }
    
    public List<ContentHandler> getContentHandlers() {
        List list = new ArrayList();
        list.add( new MasterLayoutContentHandler() );
        list.add( new BlockContentHandler() );
        list.add( new WidgetContentHandler() );
        list.add( new TemplateLayoutContentHandler() );
        list.add( new ServiceAdapterContentHandler() );
        return list;
    }
    
    public List<ServiceHandler> getServiceHandlers() {
        List list = new ArrayList();
        list.add( new ScriptServiceHandler( ) );
        list.add( new HttpServiceHandler( ) );
        return list;
    }
    
}
