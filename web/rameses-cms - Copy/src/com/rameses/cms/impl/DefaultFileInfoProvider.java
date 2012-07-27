/*
 * DefaultFileManagerProvider.java
 *
 * Created on June 19, 2012, 9:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.impl;

import com.rameses.cms.*;
import com.rameses.io.StreamUtil;
import com.rameses.util.URLDirectory;
import com.rameses.util.URLDirectory.URLScanHandler;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class DefaultFileInfoProvider implements FileInfoProvider {
    
    private String rootUrl;
    
    public DefaultFileInfoProvider(String rurl) {
        if( rurl.endsWith("/")) rurl = rurl.substring(0, rurl.lastIndexOf("/"));
        this.rootUrl = rurl;
    }
    
    //source to get the folder information including content of folders
    public Map getFolderInfo(String name) {
        
        try {
            Map map = new HashMap();
             //retrieve also if there is a url mapping file
            InputStream is = null;
            try {
                URL _urlmapping = new URL(rootUrl + name + (!name.endsWith("/")?"/":"") + "folder.conf" );
                is = _urlmapping.openStream();
                Map folderConf = JsonUtil.toMap( StreamUtil.toString(is)); 
                map.putAll( folderConf );
            } catch(Exception ign){;} finally {
                try { is.close(); } catch(Exception ign){;}
            }
            
            //return a list of filenames under a folder;
            final URL _url = new URL(rootUrl + (name.equals("/") ? "": name));
            final List items = new ArrayList();
            URLDirectory.scanFiles( _url,1, new URLScanHandler(){
                public void start(){;}
                public void startChildren(int level){;}
                public void handle(URL u, int level){
                    if(level <= 0) return;
                    if(!u.getFile().endsWith(".pg")) return;
                    //
                    String s = u.getFile().replace(_url.getFile(), "");
                    if( s.endsWith("/")) s = s.substring(0, s.lastIndexOf("/"));
                    if( !s.startsWith("/")) s = "/" + s;
                    items.add( s );
                }
                public void endChildren( int level){;}
                public void end(){;}
            });
            map.put("items", items);
            
            
           
            return map;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    
    
    public Map getFileInfo(String name) {
        InputStream is = null;
        try {
            URL u = new URL(rootUrl + name);
            is = u.openStream();
            return JsonUtil.toMap( StreamUtil.toString(is) );
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try { is.close(); } catch(Exception ign){;}
        }
    }
    
    
    
}
