/*
 * CustomFileManager.java
 *
 * Created on July 4, 2012, 1:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.FileDir;
import com.rameses.anubis.FileDir.FileFilter;
import com.rameses.anubis.FileManager;
import com.rameses.anubis.Module;
import com.rameses.anubis.Project;
import com.rameses.anubis.ProjectUtils;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Elmo
 *
 */
public class CustomFileManager extends FileManager {
    
    private static String ROOT_DIR = "/files";
    
    private String getDir() {
        return ROOT_DIR;
    }
    
    public CustomFileManager(Project project) {
        super(project);
        super.addFileHandler( new CustomPageFileHandler());
        super.addFileHandler( new CustomMediaFileHandler() );
    }
    
    public Set getFolderItems(String sname) {
        try {
            String[] arr = ProjectUtils.getModuleNameFromFile( sname, project );
            if(arr!=null) {
                Module m = project.getModules().get(arr[0]);
                return m.getFolderItems( arr[1] );
            }
            final String name = (sname.equals("/")) ? "" : sname;
            String rootUrl = project.getUrl()+getDir()+name ;

            //return a list of filenames under a folder;
            final Set<String> items = new LinkedHashSet();
            
            final String prefixName = (( sname.equals("/")) ? "/" : (sname+"/"));
            FileDir.scan( rootUrl, new FileFilter() {
                public void handle(FileDir.FileInfo f) {
                    if(!f.isDir() ) {
                        if(f.getExt()!=null && f.getExt().equals("conf")) return;
                        items.add( prefixName +  f.getFileName() );
                    }
                }
            });
            
            //add also the default module
            if(project.getSystemModule()!=null) {
                Set subset = project.getSystemModule().getFolderItems(sname);
                items.addAll(subset);
            }
            
            //find folders inside other modules except for
            for( Module m : project.getModules().values() ) {
                Set subset = m.getFolderItems( sname );
                items.addAll( subset );
            }
            return items;
            
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Map getFileSource(String name, String moduleName ) {
        //if seek not from the module
        if( moduleName ==null ) {
            String path = project.getUrl()+getDir() + name;
            //check also if a folder exists. if exists, mark this as haschildren
            Map result = ContentUtil.getJsonMap(path);
            if(result!=null) {
                String folderPath = path.substring( 0, path.lastIndexOf(".") );
                result.put("haschildren",ContentUtil.fileExists( folderPath ));                
                return result;
            }
            
            if(project.getSystemModule()!=null) {
                return project.getSystemModule().getFileSource( name );
            }
            return null;
        } 
        else {
            return project.getModules().get(moduleName).getFileSource( name );
        }
    }

    
}
