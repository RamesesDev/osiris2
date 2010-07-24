
package com.rameses.scheduler;

/**
 * This is the business interface for TaskService enterprise bean.
 */
public interface TaskSchedulerLocal {
    
    void startTimer(long delay);
    void cleanUp();
    
    TaskBean createTask(TaskBean t);
    void startTask(Long t);
    void suspend(Long id);
    void resume(Long id);
    
}
