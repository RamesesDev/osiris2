/*
 * CustomTheme.java
 *
 * Created on July 16, 2012, 7:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.Theme;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class AnubisDefaultTheme extends Theme {
    
    private static String MASTER_DIR = "theme/masters";
    private static String TEMPLATE_DIR = "theme/templates";
    
    public AnubisDefaultTheme(String url) {
        super("system", url);
    }
    
    protected Map getInfo() {
        return new HashMap();
    }
    
    public InputStream getMasterResource(String name) {
        InputStream is = null;
        is = ContentUtil.findResource( getUrl(), MASTER_DIR, name );
        if(is!=null) return is;
        return null;
    }
    
    public InputStream getTemplateResource(String name) {
        InputStream is = null;
        is = ContentUtil.findResource( getUrl(), TEMPLATE_DIR, name );
        if(is!=null) return is;
        return null;
    }
    
    public InputStream getResource(String name) {
        InputStream is = null;
        is = ContentUtil.findResource(getUrl() ,"theme",  name );
        if(is!=null) return is;
        if( getProvider()!=null ) {
            is = ContentUtil.findResource(getProvider(), null, name );
            if(is!=null) return is;
        }
        return null;
    }
    
    public boolean isResourceExist(String name) {
        InputStream is = null;
        try {
            is = ContentUtil.findResource(getUrl(), "theme",  name );
            if(is!=null) return true;
            if( getProvider()!=null ) {
                is = ContentUtil.findResource(getProvider(), null, name );
                if(is!=null) return true;
            }
            return false;
        } catch(Exception e) {
            return false;
        } finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
    
    
}
