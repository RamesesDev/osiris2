/*
 * TaskSchedulerMgmtMBean.java
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
public interface TaskSchedulerMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    
}
