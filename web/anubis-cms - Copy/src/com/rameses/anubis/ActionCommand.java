/*
 * ActionCommand.java
 *
 * Created on July 10, 2012, 10:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class ActionCommand {
    
    protected Project project;
    
    public void setProject(Project project) {
        this.project = project;
    }
    
    public abstract Object execute(Map params, Map env) throws Exception;
    
}
