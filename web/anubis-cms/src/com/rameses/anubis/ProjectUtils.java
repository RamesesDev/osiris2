/*
 * ProjectUtils.java
 *
 * Created on July 19, 2012, 3:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.anubis;

import java.util.Iterator;

/**
 *
 * @author Elmo
 * This is a utility file for handling different routines related to the project
 */
public class ProjectUtils {
    
    /**
    This is called everytime you need to fix the prefix of the filename if
    it's coming from the project or from the modules. For example
    consider the path "/blogs/filename". blogs in this case is a possible module.
    To check, find out by looking at installed modules if blogs exist.
    if blogs does not exist in the modules, then null value is returned.
    If there is a module,  it returns String[] of length 2 where the first
    part is the module name and the second is the corrected filename
     */
    public static String[] getModuleNameFromFile( String fileName, Project project ) {
        if( fileName.indexOf("/",1) <= 0 ) return null;
        String test = fileName.substring(1, fileName.indexOf("/",1));
        if( project.getModules().containsKey(test)) {
            String[] arr = new String[2];
            arr[0] = test;
            arr[1] = fileName.substring( fileName.indexOf("/",1) );
            return arr;
        } else {
            return null;
        }
    }
    
    //finds the first file in the folder that is visible.
    public static File findFirstVisibleFile( Folder folder ) {
        if( folder.getChildren().size() > 0) {
            Iterator<File> iter = folder.getChildren().iterator();
            while(iter.hasNext()) {
                File firstFile = iter.next();
                if(!firstFile.isHidden() || firstFile.getPath().equals("/index")) {
                    return firstFile;
                }
            }
        }
        return null;
    }
    
    
}
