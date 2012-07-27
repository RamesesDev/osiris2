/*
 * XFileManager.java
 *
 * Created on June 30, 2012, 12:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.cms.added;

import com.rameses.cms.AbstractFile;
import com.rameses.cms.Project;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Elmo
 */
public abstract class XFileManager {
    
    protected Map<String,AbstractFile> files = new Hashtable();
    protected Project project;
    protected abstract AbstractFile loadFile(String name);
    
    public XFileManager(Project p) {
        this.project = p;
    }
    
    protected <T extends AbstractFile> T getFile(String name) {
        if(!files.containsKey(name)) {
            System.out.println("loading name is " + name);
            AbstractFile file = loadFile(name);
            files.put(name, file);
        }
        return (T) files.get(name);
    }
    
    
}
