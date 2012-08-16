/*
 * FileInstance.java
 *
 * Created on July 4, 2012, 7:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class FileInstance {
    
    private File file;
    private Map params = new HashMap();
    private Project project;
    
    /** Creates a new instance of FileInstance */
    public FileInstance(File f) {
        this.file = f;
    }
    
    public File getFile() {
        return file;
    }
    
    
    public String getId() {
       return file.getId();
    }
    
    public String getTitle() {
        return file.getTitle();
    }
    
    public int getSortorder() {
        return file.getSortorder();
    }
    
    public boolean isSecured() {
        return file.isSecured();
    }
    
    public String getExt() {
        return file.getExt();
    }
    
    public String getHref() {
        return file.getHref();
    }
    
    public String getName() {
        return file.getName();
    }
      
    public String getPagename() {
        return file.getPagename();
    }
    public Map getParams() {
        return params;
    }
    
    public void setParams(Map params) {
        this.params = params;
    }

    public abstract InputStream getContent();
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }

    public Module getModule() {
        if( file.getModule() == null ) return null;
        return project.getModules().get( file.getModule() );
    }

    
    
}
