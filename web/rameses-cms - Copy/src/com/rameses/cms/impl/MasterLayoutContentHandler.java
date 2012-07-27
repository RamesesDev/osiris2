/*
 * BlockContent.java
 *
 * Created on June 23, 2012, 11:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.impl;

import com.rameses.cms.ContentHandler;
import com.rameses.cms.ContentTypes;
import com.rameses.cms.PageInstance;
import com.rameses.cms.Theme;
import java.io.InputStream;



/**
 *
 * @author Elmo
 */
public class MasterLayoutContentHandler extends ContentHandler {
    
    protected String getName() {
        return ContentTypes.MASTER;
    }
    
    public InputStream getSource(String id, Object data) {
        //check first local files. if not exist, check themes
        InputStream is = null;
        PageInstance pageInstance = (PageInstance)data;
        is = findResource( project.getUrl()+"/content/masters/"+id );
        
        if( is == null ) {
            String themeName = (String)pageInstance.getPage().get("theme");
            Theme theme = project.getThemes().get( themeName );
            is = findResource( theme.getUrl()+"/masters/"+id );
        }
        /*
        String pageId = (String)pageInstance.getPage().get("id");
        String localPath = project.getUrl()+"/content/themes/" + themeName + "/masters/";
        is = findResource( localPath + id + "_" + pageId );
        if( is == null ) is = findResource( localPath + id );
         */
        return is;
    }
    
    
    
}
