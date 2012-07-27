/*
 * File.java
 *
 * Created on June 19, 2012, 8:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

/**
 *
 * @author Elmo
 */
public abstract class AbstractFile implements Comparable {
    
    private FileManager fileManager;
    private int sortorder;
    private String ext;
    
    protected String title;
    protected String filePath;
    private String context;
    
    protected AbstractFile() {
    }
    
    public int getSortorder() {
        return sortorder;
    }
    
    public void setSortorder(int sortorder) {
        this.sortorder = sortorder;
    }
    
    public int compareTo(Object o) {
        AbstractFile comp = (AbstractFile)o;
        if( comp.getSortorder() < getSortorder() )
            return 1;
        else if( comp.getSortorder() > getSortorder()) {
            return -1;
        } else {
            return  getPagepath().compareTo( comp.getPagepath() ); 
        }
    }
    
    void setFilepath(String filePath) {
        this.filePath = filePath;
    }
    
    //package level only the FileManager can access this method
    void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    //package level only the FileManager can access this method
    void setExt(String ext) {
        this.ext = ext;
    }
    
    public String getFilepath() {
        return filePath;
    }
    
    public String getExt() {
        return ext;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public FileManager getFileManager() {
        return fileManager;
    }
    
    public String getContext() {
        return context;
    }
    
    public void setContext(String context) {
        this.context = context;
    }
    
    
    public String getId() {
        if( getContext()!=null ) {
            return this.filePath.substring( getContext().length() );
        } else {
            return filePath;
        }
    }
    
    
    public String getPagepath() {
        if( getContext()!=null ) {
            return this.filePath.substring( getContext().length(), filePath.indexOf(".") );
        } else {
            return filePath;
        }
    }
    
    
}
