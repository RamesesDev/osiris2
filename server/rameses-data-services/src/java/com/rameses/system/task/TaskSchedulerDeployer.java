package com.rameses.system.task;

import javax.naming.InitialContext;

public class TaskSchedulerDeployer implements TaskSchedulerDeployerMBean {
    
    //TASK SERVICE MANAGEMENT
    public void start() throws Exception {
        System.out.println("STARTING TASK SCHEDULER...");
        InitialContext ctx = new InitialContext();
        TaskSchedulerLocal taskService = (TaskSchedulerLocal)ctx.lookup("TaskScheduler/local");
        taskService.cleanUp();
        taskService.startTimer(5000);
    }
   
    public void stop() throws Exception {
    }
    
    
}
