/*
 * TaskSchedulerLocal.java
 *
 * Created on October 19, 2010, 8:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;

import java.util.Map;

/**
 *
 * @author ms
 */
public interface TaskSchedulerServiceLocal {
    void scheduleTasks();
    void processTasks();
    void updateTask(Map map, boolean increment);
}
