/*
 * ProjectFactory.java
 *
 * Created on June 22, 2012, 10:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.List;

/**
 *
 * @author Elmo
 */
public abstract class ProjectFactory {
    
    //configuration for the file manager.
    public abstract FileInfoProvider getFileInfoProvider(Project project);
    
    //file handlers for the files
    public abstract List<FileHandler> getFileHandlers();

    //content handlers 
    public abstract List<ContentHandler> getContentHandlers();

    
    //service handlers 
    public abstract List<ServiceHandler> getServiceHandlers();
    
    public abstract void init( Project project );
    
    
    
    public final Project create(String name, String url ) {
        Project project = new Project(name, url);
        init( project );
        
      
        FileManager fileManager = project.getFileManager();
        fileManager.setFileInfoProvider( getFileInfoProvider( project ) );
        for(FileHandler fileHandler : getFileHandlers()) {
            fileHandler.setProject(project);
            fileManager.addFileHandler( fileHandler );    
        }
        
        ContentManager contentManager = project.getContentManager();
        for(ContentHandler contentHandler : getContentHandlers()) {
            contentHandler.setProject( project );
            contentManager.addHandler( contentHandler );
        }
        
        ServiceManager serviceManager = project.getServiceManager();
        for(ServiceHandler serviceHandler : getServiceHandlers()) {
            serviceManager.addHandler( serviceHandler );
        }

        return project;
    }
    
}
