/*
 * ActiveTaskRunnable.java
 * Created on October 27, 2011, 9:11 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class PendingTaskProcessor extends AbstractTaskProcessor {
    
    PendingTaskProcessor(ScheduleManager s) {
        super(s);
    }
    public void run() {
        TaskBean t = null;
        List<TaskBean> poolBack = new ArrayList();
        ScheduleManager manager = super.getManager();
        try {
            while((t=queue.poll())!=null) {
                if(manager.getSuspendedTasks().checkForSuspesion(t.getId())) {
                    manager.getSuspendedTasks().suspend(t.getId(), t);
                }
                else if(manager.getErrorTasks().hasError(t.getId())) {
                    //do nothing
                }
                else if(t.isExpired()) {
                    manager.getActiveTasks().push( t );
                    
                }
                else {
                    //return to this pool
                    poolBack.add(t);
                }
            }
        } catch(Exception err) {
            System.out.println("error " + err.getMessage());
        }
        //poolback the tasks that were not processed for next scheduled run
        for(TaskBean tb : poolBack) {
            queue.add( tb );
        }
        poolBack.clear();
        poolBack = null;
    }
    
}
