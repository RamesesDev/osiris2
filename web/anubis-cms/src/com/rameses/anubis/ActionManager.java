/*
 * ActionManager.java
 *
 * Created on July 10, 2012, 10:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class ActionManager {
    
    protected Project project;
    
    private Map<String, ActionCommand> actions = new Hashtable();
    
    protected abstract ActionCommand createActionCommand(String name) throws Exception;
    
    /** Creates a new instance of ActionManager */
    public ActionManager(Project project) {
        this.project = project;
    }
    
    public ActionCommand getActionCommand(String name) throws Exception {
        if( !actions.containsKey(name)) {
            ActionCommand c = createActionCommand(name);
            c.setProject(project);
            actions.put(name, c);
        }
        return actions.get(name);
    }
    
    
}
