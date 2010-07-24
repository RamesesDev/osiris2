package com.rameses.scheduler;

import com.rameses.eserver.JndiUtil;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.InitialContext;

//@Service(objectName="rameses:service=TaskScheduler")
//@Local(TaskSchedulerLocal.class)
//@Management(TaskSchedulerMgmt.class)
public class TaskSchedulerMgmt implements TaskSchedulerMgmtMBean, Serializable {
    
    private final String jndiName = "TaskSchedulerMgmt";
    private Map<Long, TaskThread> busy = new Hashtable<Long, TaskThread>();
    private boolean processing;
    
    //TASK SERVICE MANAGEMENT
    public void start() throws Exception {
        System.out.println("*******************************");
        System.out.println("STARTING SCHEDULED TASK SERVICES");
        System.out.println("*******************************");
        JndiUtil.bind(new InitialContext(), jndiName, this );
        
        
        System.out.println("STARTING TASK SCHEDULER...");
        InitialContext ctx = new InitialContext();
        TaskSchedulerLocal taskService = (TaskSchedulerLocal)ctx.lookup("TaskScheduler/local");
        taskService.cleanUp();
        taskService.startTimer(5000);
    }
    
   
    public void stop() throws Exception {
        System.out.println("*******************************");
        System.out.println("STOPPING SCHEDULED TASK SERVICE");
        System.out.println("*******************************");
        JndiUtil.unbind(new InitialContext(), jndiName);
    }
    
    public Map getBusyTasks() {
        return busy;
    }

    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean b) {
        this.processing = b;
    }

    
}
