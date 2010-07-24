package com.rameses.scheduler;

import java.util.Map;


public interface TaskSchedulerMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    
    boolean isProcessing();
    void setProcessing(boolean b);
    
    Map getBusyTasks();
    
    
}
