/*
 * WebContentModule.java
 *
 * Created on July 17, 2012, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.FileDir;
import com.rameses.anubis.FileDir.FileFilter;
import com.rameses.anubis.Module;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Elmo
 */
public class AnubisDefaultModule extends Module {
    
    private static String ACTION_DIR = "/actions/";
    private static String ROOT_DIR = "/files";
    private static String SERVICE_DIR = "/services";
    
    private static String PAGE_DIR = "/content/pages/";
    private static String GLOBAL_BLOCK_DIR = "/content/global-blocks/";
    
    private static String WIDGET_DIR = "/content/widgets/";
    private static String TEMPLATE_DIR = "/content/templates/";
    
    /** Creates a new instance of WebContentModule */
    public AnubisDefaultModule( String url) {
        super("system", url);
    }
    
    protected Map getInfo() {
        return new HashMap();
    }
    
    //FILE MANAGER REQUEST FILE AND FOLDER
    public Map getFileSource(String name) {
        String path = getUrl()+ROOT_DIR+name;
        return ContentUtil.getJsonMap(path);
    }
    
    public Set<String> getFolderItems(String sname) {
        //include in the super.getName();
        final String name = (sname.equals("/")) ? "" : sname;
        String rootUrl = getUrl()+ROOT_DIR + name ;
        final Set items = new LinkedHashSet();
        final String prefixName = (( sname.equals("/")) ? "/" : (sname+"/"));
        FileDir.scan( rootUrl, new FileFilter() {
            public void handle(FileDir.FileInfo f) {
                if(!f.isDir() ) {
                    if(f.getExt()!=null && !f.getExt().equals("conf")) {
                        items.add( prefixName +  f.getFileName() );
                    }
                }
            }
        });
        return items;
    }
    
    public InputStream getBlockResource(String name) {
        return ContentUtil.findResource(getUrl()+ PAGE_DIR+ name);
    }
    
    public InputStream getGlobalBlockResource(String name) {
        return ContentUtil.findResource(getUrl()+ GLOBAL_BLOCK_DIR + name);
    }
    
    
    public InputStream getTemplateResource(String name) {
        return ContentUtil.findResource(getUrl()+ TEMPLATE_DIR+ name);
    }
    
    public InputStream getWidgetResource(String name) {
        return ContentUtil.findResource(getUrl()+ WIDGET_DIR+ name);
    }
    
    public InputStream getResource(String name) {
        return ContentUtil.findResource(getUrl()+ name);
    }
    
    
    public InputStream getActionResource(String name) {
        return ContentUtil.findResource(getUrl()+  ACTION_DIR + "/"+ name);
    }
    
    public InputStream getServiceAdapterSource(String name) {
        return ContentUtil.findResource(getUrl()+  SERVICE_DIR + "/"+ name);
    }
    
    
}
