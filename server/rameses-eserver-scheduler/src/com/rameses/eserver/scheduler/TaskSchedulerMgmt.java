/*
 * TaskSchedulerMgmt.java
 *
 * Created on October 19, 2010, 8:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;

/**
 *
 * @author ms
 */
public class TaskSchedulerMgmt implements TaskSchedulerMgmtMBean {
    

    public void start() throws Exception {
        TaskManager.getInstance().start();
    }

    public void stop() throws Exception {
        TaskManager.getInstance().stop();
    }
    
}
