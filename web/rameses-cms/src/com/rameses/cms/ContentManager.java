/*
 * ContentManager.java
 *
 * Created on June 23, 2012, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class ContentManager {
    
   
    
    private Map<String,ContentHandler> handlers = new Hashtable();
    private Project project;
    
    ContentManager(Project project) {
        this.project = project;
    }
    
    public void addHandler(ContentHandler handler) {
        handler.setProject( project );
        handlers.put( handler.getName(), handler );
    }
    
    public ContentHandler getHandler(String name ) {
        return handlers.get( name );
    }
    
}
