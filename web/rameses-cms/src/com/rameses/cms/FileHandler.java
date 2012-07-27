/*
 * FileContentProvider.java
 *
 * Created on June 19, 2012, 8:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.io.InputStream;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class FileHandler {
    
    protected Project project;
    
    public abstract String getExt();
    public abstract AbstractFile createFile( String id, Map props );
    public abstract InputStream getContent( Object o );

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    
}
