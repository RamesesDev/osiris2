/*
 * CustomTheme.java
 *
 * Created on July 16, 2012, 7:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.FileDir;
import com.rameses.anubis.FileDir.FileFilter;
import com.rameses.anubis.Module;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CustomModule extends Module {
    
    private static String ACTION_DIR = "/actions/";
    private static String ROOT_DIR = "/files";
    private static String SERVICE_DIR = "/services/";
    
    private static String PAGE_DIR = "content/pages/";
    private static String GLOBAL_BLOCK_DIR = "content/global-blocks/";
    
    private static String WIDGET_DIR = "/content/widgets/";
    private static String TEMPLATE_DIR = "/content/templates/";
    private static String LAYOUT_DIR = "/content/layouts/";
    
    private static String RES_DIR = "/res";
    
    public CustomModule(String name, String url) {
        super(name, url);
    }
    
    /**
     * check from local dir for the resource. If resource not found,
     * check in the provider
     */
    private InputStream findStream( String respath ) {
        InputStream is = ContentUtil.findResource(getUrl(), null, respath);
        if(is!=null) return is;
        if( getProvider()!=null ) {
            try {
                is = ContentUtil.findResource(getProvider() , null,  respath);
                return is;
            } catch(Exception e) {
                System.out.println("error findStrean in provider->"+e.getMessage());
            }
        }
        return null;
    }
    
    /**
     * find the map in the provider, if any.
     * find the module attached. Then consolidate the results
     * the local conf will always override the provider
     */
    private Map findJsonMap(String filename) {
        Map map = new HashMap();
        if( getProvider()!=null) {
            Map _map = ContentUtil.getJsonMap( getProvider(), null, filename );
            if(_map!=null) map.putAll( _map );
        }
        
        Map _map = ContentUtil.getJsonMap( getUrl() ,null, filename );
        if( _map!=null ) map.putAll( _map );
        return map;
    }
    
    private boolean checkFileExists(String filename) {
        boolean fileExist = ContentUtil.fileExists( getUrl() , null, filename );
        if(!fileExist && getProvider()!=null) {
            try {
                fileExist = ContentUtil.fileExists( getProvider() , null, filename );
            } catch(Exception e) {
                System.out.println("error checking file exist in provider->"+e.getMessage());
            }
        }
        return fileExist;
    }
    
    /***
     * find the module info and consolidate the results
     *
     */
    protected Map getInfo() {
        return findJsonMap( "/module.conf" );
    }
    
    //media resource, etc.
    public InputStream getResource(String name) {
        return findStream(RES_DIR + name);
    }
    
    //page block
    public InputStream getBlockResource(String name) {
        return findStream(PAGE_DIR+ name);
    }
    
    //global reusable blocks
    public InputStream getGlobalBlockResource(String name) {
        return findStream(GLOBAL_BLOCK_DIR+ name);
    }
    
    public InputStream getServiceAdapterSource(String name) {
        return findStream(SERVICE_DIR+name);
    }
    
    public InputStream getTemplateResource(String name) {
        return findStream(TEMPLATE_DIR + name);
    }
    
    public InputStream getWidgetResource(String name) {
        return findStream(WIDGET_DIR + name);
    }
    
    public InputStream getActionResource(String name) {
        return findStream( ACTION_DIR + name );
    }
    
    
    public InputStream getLayoutResource(String name) {
        return findStream(LAYOUT_DIR + name);
    }
    
    public Map getFileSource(String name) {
        String path = ROOT_DIR+name;
        Map result = findJsonMap(path);
        if(result!=null) {
            String folderPath = path.substring( 0, path.lastIndexOf(".") );
            result.put("haschildren", checkFileExists( folderPath ));
            return result;
        }
        return null;
    }
    
    public Set<String> getFolderItems(String sname) {
        //include in the super.getName();
        final String name = (sname.equals("/")) ? "" : sname;
        final Set items = new LinkedHashSet();
        
        final String prefixName = "/" + getName() + (( sname.equals("/")) ? "/" : (sname+"/"));
        
        FileFilter filter = new FileFilter() {
            public void handle(FileDir.FileInfo f) {
                if(!f.isDir() ) {
                    if(f.getExt()!=null && !f.getExt().equals("conf")) {
                        items.add( prefixName +  f.getFileName() );
                    }
                }
            }
        };
        FileDir.scan( getUrl()+ROOT_DIR + name, filter );
        if( getProvider() !=null ) {
            try {
                FileDir.scan( getProvider()+ROOT_DIR + name, filter );
            } catch(Exception ex) {
                System.out.println("error scan dir in provider->"+ex.getMessage());
            }
        }
        return items;
    }

    
    
    
    
}
