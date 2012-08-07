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
    
    private static String MASTER_DIR = "/theme/masters/";
    
    public AnubisDefaultTheme(String url) {
        super("system", url);
    }
    
    protected Map getInfo() {
        return new HashMap();
    }
    
    public InputStream getMasterResource(String name) {
        InputStream is = null;
        is = ContentUtil.findResource( getUrl()+MASTER_DIR+name );
        if(is!=null) return is;
        return null;
    }
    
    public InputStream getResource(String name) {
        InputStream is = null;
        is = ContentUtil.findResource(getUrl() +"/theme/" +  name );
        if(is!=null) return is;
        if( getProvider()!=null ) {
            is = ContentUtil.findResource(getProvider() + name );
            if(is!=null) return is;
        }
        return null;
    }

    
}
