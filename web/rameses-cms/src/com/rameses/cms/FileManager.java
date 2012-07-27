/*
 * FileManager.java
 *
 * Created on June 19, 2012, 8:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class FileManager {
    
    private Map<String, FileHandler> handlers = new HashMap();
    Map<String, AbstractFile> files = new Hashtable();
    
    private FileInfoProvider fileInfoProvider;
    private Project project;
    private PageManager pageManager = new PageManager(this);
    
    FileManager(Project project) {
        this.project = project;
        
    }
    
    public void addFileHandler( FileHandler handler ) {
        handlers.put(handler.getExt(), handler);
    }
    
    public FileHandler getFileHandler( String ext ) {
        return handlers.get( ext );
    }
    
    /***
     * This method does not do anything yet. It just gets
     * meta information of the file system. It also sends the
     * link to the FileManager
     */
    
    public AbstractFile find( String id ) {
        return find( id, null);
    }
    
    public AbstractFile find( String id,  String context ) {
        if(! id.startsWith("/")) id = "/" + id;
        
        
        AbstractFile file  = files.get(id);
        if( file == null ) {
            
            //it is a folder if there is no extension or it ends with /
            if( id.endsWith("/") || !id.contains(".")) {
                Map info = fileInfoProvider.getFolderInfo( id );
                Folder folder = new Folder(info);
                folder.setTitle( id );
                file = folder;
            }
            else {
                String ext = id.substring( id.lastIndexOf(".")+1 );
                FileHandler handler = handlers.get(ext);
                if( handler == null ) {
                    throw new RuntimeException("There is no file File handler for " + ext  );
                }
                Map props = fileInfoProvider.getFileInfo(id);
                file = handler.createFile( id, props );
                file.setExt(ext);
            }
            file.setContext( context );
            file.setFileManager(this);
            file.setFilepath( id );
            files.put(id, file);
        }
        return file;
    }
    
    public PageManager getPageManager() {
        return pageManager;
    }

    public void setFileInfoProvider(FileInfoProvider fileInfoProvider) {
        this.fileInfoProvider = fileInfoProvider;
    }

    public Project getProject() {
        return project;
    }
}
