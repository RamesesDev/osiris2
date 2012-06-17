/*
 * SchedulerServiceMBean.java
 * Created on October 27, 2011, 8:52 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler2;

import java.util.Map;

/**
 * @author jzamss
 */
public interface SchedulerServiceMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    void addTask(Map task) throws Exception;
    void updateTask(Map task)  throws Exception;
    void suspend(String id) throws Exception;
    void resume(String id) throws Exception;
    
    void resumeError(String id) throws Exception;
    void restart(String id) throws Exception;
    void remove(String id) throws Exception;
    
}
