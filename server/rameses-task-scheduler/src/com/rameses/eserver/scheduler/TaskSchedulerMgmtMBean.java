/*
 * TaskSchedulerMgmtMBean.java
 * Created on June 25, 2011, 2:05 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver.scheduler;

/**
 *
 * @author jzamss
 */
public interface TaskSchedulerMgmtMBean {
    
  void start() throws Exception;
  void stop() throws Exception;
  void setDebug(boolean debug);
  boolean isDebug();  
  
  void startTask(int taskid) throws Exception;
  void suspendTask(int taskid) throws Exception;
  void resumeTask(int taskid) throws Exception;
  void resumeFromError(int taskid) throws Exception;
  
}
