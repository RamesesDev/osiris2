/*
 * WebFileManager.java
 *
 * Created on June 21, 2012, 11:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 *
 * if the requested path is single only
 *
 */
public class PageManager {
    
    private static String WEBROOT = "/pages";
    private FileManager fileManager;
    
    PageManager(FileManager f) {
        this.fileManager = f;
    }
    
    
    /**
     * when parsing name, check first the path if it has subfolders for ex:
     *    1) /about/company/mission
     *    2) /blogs/2012/01/01/my-first-article
     *  the first example may be based on heirarchy arrangement or url mapping.
     *  To differentiate if it is heirarchical or based on a url mapping,
     *
     */
    public PageInstance getPage( String name  ) {
        return getPage(name, null);
    }
    
    public PageInstance getPage( String name, Map params  ) {
        if(params==null) params = new HashMap();
        //correct first the name
        if( name.equals("/")) name = "/index";
        if( name.endsWith("/")) name = name.substring(0, name.length()-1);
        if( !name.startsWith("/")) name = "/" + name;
        
        String pageName = name + ".pg";
        if( fileManager.files.containsKey(pageName) ) {
            return new PageInstance( (Page)fileManager.files.get(pageName), params );
        }
        
        boolean hasSubItemPath = (pageName.substring(1).indexOf("/") > 0);
        if( hasSubItemPath ) {
            String contextPath = name.substring(0, name.indexOf("/",1));
            Folder folder = (Folder) fileManager.find(WEBROOT + contextPath, WEBROOT );
            if( folder!=null) {
                for( UrlMapping um : folder.getUrlMappings() ) {
                    if(um.matches(name)) {
                        pageName = um.getPageName();
                        params.putAll( um.getTokens(name) );
                        break;
                    }
                }
            }
        }
        
        
        Page page = (Page) fileManager.find( WEBROOT + pageName, WEBROOT );
        if(page==null) {
            throw new RuntimeException("Page " + pageName + " not found");
        }
        
        return new PageInstance(page, params);
    }
    
    public Folder getFolder(String name) {
        String n = WEBROOT + name;
        if( n.endsWith("/")) n = n.substring(0, n.length()-1);
        return (Folder)fileManager.find( n, WEBROOT );
    }
    
}
