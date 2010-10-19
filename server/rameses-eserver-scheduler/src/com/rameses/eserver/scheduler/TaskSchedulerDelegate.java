/*
 * TaskDelegate.java
 *
 * Created on October 19, 2010, 8:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;


import javax.naming.InitialContext;

/**
 *
 * @author ms
 */
public final class TaskSchedulerDelegate {
    
    private static TaskSchedulerServiceLocal taskService;
    
    public static TaskSchedulerServiceLocal getTaskSchedulerService()  {
        try {
            if(taskService==null ) {
                InitialContext ctx = new InitialContext();
                taskService = (TaskSchedulerServiceLocal)ctx.lookup(AppContext.getPath()+"TaskSchedulerService/local");
            }
            return taskService;
        } catch(Exception e) {
            System.out.println("cannot lookup TaskSchedulerServiceLocal.");
            return null;
        }
    }
    
    
}
