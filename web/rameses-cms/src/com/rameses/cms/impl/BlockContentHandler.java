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
public class BlockContentHandler extends ContentHandler {
    
    protected String getName() {
        return ContentTypes.BLOCK;
    }
    
    public InputStream getSource(String pageBlockname, Object data) {
        PageInstance pageInstance = (PageInstance)data;
        String themeName = (String)pageInstance.getPage().get("theme");
        //check first local files. if not exist, check themes
        InputStream is = null;
        is = findResource( project.getUrl()+"/content/pages"+ pageBlockname );
        
        String blockname = pageBlockname.substring( pageBlockname.lastIndexOf("/")+1 );
        if( is == null ) is = findResource( project.getUrl()+"/content/global/"+blockname );
        Theme theme = project.getThemes().get( themeName );
        if( is == null ) is = findResource( theme.getUrl()+"/blocks/"+blockname );
        return is;
    }
    
    
    
}
