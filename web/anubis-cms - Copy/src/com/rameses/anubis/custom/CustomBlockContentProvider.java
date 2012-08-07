/*
 * CustomBlockPageContentProvider.java
 *
 * Created on July 17, 2012, 10:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.BlockContentProvider;
import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.Module;
import com.rameses.anubis.PageFileInstance;
import com.rameses.anubis.Project;
import java.io.InputStream;

/**
 *
 * @author Elmo
 */
public class CustomBlockContentProvider extends BlockContentProvider {
    
    private static final String PAGE_BLOCK_DIR =  "/content/pages/";
    private static final String GLOBAL_BLOCK_DIR = "/content/global-blocks/";
    
    protected InputStream getResource(String pageBlockname, PageFileInstance page) {
        Project project = page.getProject();
        //check first local files. if not exist, check themes
        InputStream is = null;
        String lang = null;
        
        //check first if the page is coming from the module
        Module module = page.getModule();
        Module defaultModule = project.getSystemModule();
        
        /**********************************************************************
         * FIND NORMAL PAGE BLOCK. 
         * start:  (module or project)->default
         **********************************************************************/
        if( module ==null ) {
            //local page block
            is = ContentUtil.findResource( project.getUrl() + PAGE_BLOCK_DIR + pageBlockname );
            if(is!=null) return is;
        } 
        else {
            //remove the module name part in the blockname
            String moduleBlockname = pageBlockname.substring( pageBlockname.indexOf("/",1)+1 );
            is = module.getBlockResource( moduleBlockname );
            if(is!=null) return is;
        }

        if( defaultModule !=null ) {
            is = defaultModule.getBlockResource( pageBlockname );
            if(is!=null) return is;
        }
        
        /**********************************************************************
         * FIND SHARED GLOBAL BLOCK. 
         * start from module->project->default 
         **********************************************************************/
        String blockname = pageBlockname.substring( pageBlockname.lastIndexOf("/")+1 );
        if(module!=null) {
            is = module.getBlockResource( blockname );
            if(is!=null) return is;
        }

        is = ContentUtil.findResource( project.getUrl()+ GLOBAL_BLOCK_DIR+blockname );
        if(is!=null) return is;
        if( defaultModule !=null ) {
            is = defaultModule.getGlobalBlockResource( blockname );
            if(is!=null) return is;
        }
        
        return is;
    }
    
    
    
}
