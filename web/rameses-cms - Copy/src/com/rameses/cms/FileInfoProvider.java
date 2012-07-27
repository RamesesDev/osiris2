/*
 * FileManagerProvider.java
 *
 * Created on June 19, 2012, 9:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.Map;

/**
 *
 * @author Elmo
 * This must be customized depending on the application 
 */
public interface FileInfoProvider {
    
    //returns a single file
    Map getFolderInfo( String name );
    Map getFileInfo( String name );
    
}
