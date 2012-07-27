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
import com.rameses.anubis.Theme;
import java.io.InputStream;
import java.util.Map;


public class CustomTheme extends Theme {
    
    private static String MASTER_DIR = "/masters/";
    
    
    public CustomTheme(String name, String url) {
        super(name, url);
    }
    
    protected Map getInfo() {
        return ContentUtil.getJsonMap( getUrl()+"/theme.conf" );
    }
    
    public InputStream getMasterResource(String name) {
        InputStream is = null;
        is = ContentUtil.findResource( getUrl()+MASTER_DIR+name );
        if(is!=null) return is;
        
        //check if there is an external url.
        if(getProvider()!=null) {
            is = ContentUtil.findResource( getProvider()+MASTER_DIR+name );
            if(is!=null) return is;
        }
        return null;
    }
    
    public InputStream getResource(String name) {
        InputStream is = null;
        is = ContentUtil.findResource(getUrl() + name );
        if(is!=null) return is;
        if( getProvider()!=null ) {
            is = ContentUtil.findResource(getProvider() + name );
            if(is!=null) return is;
        }
        return null;
    }

 

    
}
