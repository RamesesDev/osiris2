/*
 * TaskManager.java
 *
 * Created on October 19, 2010, 8:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;

import java.util.Date;

/**
 *
 * @author ms
 */
public class TaskManager {
    
    private static TaskManager instance;
    
    public static TaskManager getInstance() {
        if(instance==null) {
            instance = new TaskManager();
        }
        return instance;
    }
    
    public static void setInstance(TaskManager aInstance) {
        instance = aInstance;
    }
    
    private TaskSchedulerThread thread;
    
    public synchronized  void start() {
        if(thread == null ) {
            System.out.println("          STARTING SCHEDULER @"+ new Date());
            thread = new TaskSchedulerThread();
            thread.setDelay(2000);
            thread.start();
        }
    }
    
    public  synchronized  void stop() {
        if( thread !=null ) {
            System.out.println("          STOPPING SCHEDULER @"+new Date());
            thread.cancel();
            thread = null;
        }
    }
    
    
    
}
