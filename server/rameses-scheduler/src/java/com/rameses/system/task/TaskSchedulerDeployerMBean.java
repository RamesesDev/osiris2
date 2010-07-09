/*
 * TaskSchedulerDeployer.java
 *
 * Created on December 27, 2009, 6:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.system.task;

public interface TaskSchedulerDeployerMBean {
    void start() throws Exception;
    void stop() throws Exception;
}
