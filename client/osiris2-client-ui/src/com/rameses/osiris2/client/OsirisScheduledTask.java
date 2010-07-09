/*
 * SysTask.java
 *
 * Created on June 12, 2010, 2:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.rcp.common.ScheduledTask;
import com.rameses.rcp.framework.ClientContext;

/**
 *
 * @author ms
 */
public abstract class OsirisScheduledTask extends ScheduledTask {
    
    /** Creates a new instance of SysTask */
    public OsirisScheduledTask() {
        super();
    }
    
    public void register() {
        ClientContext.getCurrentContext().getTaskManager().addTask(this);
    }
    
    public void unregister() {
        ClientContext.getCurrentContext().getTaskManager().removeTask(this);
    }
    
}
