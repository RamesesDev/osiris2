/*
 * CustomMasterPageContentProvider.java
 *
 * Created on July 17, 2012, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.MasterContentProvider;
import com.rameses.anubis.PageFileInstance;
import com.rameses.anubis.Project;
import com.rameses.anubis.Theme;
import java.io.InputStream;

/**
 *
 * @author Elmo
 */
public class CustomMasterContentProvider extends MasterContentProvider {
    
    protected InputStream getResource(String name, PageFileInstance page) {
        Project project = page.getProject();
        InputStream is = null;
        //find master from the theme
        Theme theme = page.getTheme();
        is = theme.getMasterResource( name );
        if(is!=null) return is;
        Theme defaultTheme = page.getProject().getSystemTheme();
        if( defaultTheme !=null ) {
            return defaultTheme.getMasterResource( name );
        }
        
        
        return is;
    }
    
    
}
