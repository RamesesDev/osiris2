/*
 * CustomLayoutContentProvider.java
 *
 * Created on August 14, 2012, 4:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.LayoutContentProvider;
import com.rameses.anubis.Module;
import com.rameses.anubis.PageFileInstance;
import com.rameses.anubis.Project;
import java.io.InputStream;

/**
 *
 * @author Elmo
 */
public class CustomLayoutContentProvider extends LayoutContentProvider {
    
    private static final String LAYOUT_DIR =  "content/layouts/";
    
    protected InputStream getResource(String name, PageFileInstance page) {
        Project project = page.getProject();
        
        InputStream is = null;
        Module mod = page.getModule();
        if( mod !=null) {
            is = mod.getLayoutResource( name );
            if(is!=null) return is;
        }
        
        is = ContentUtil.findResource( project.getUrl(), LAYOUT_DIR, name );
        //get default
        if(is==null) {
            Module defaultMod = project.getSystemModule();
            if( defaultMod !=null) {
                is = defaultMod.getLayoutResource( name );
            }
        }
        return is;
    }
    
}
