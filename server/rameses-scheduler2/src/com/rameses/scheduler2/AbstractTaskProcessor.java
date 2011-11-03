/*
 * AbstractTaskProcessor.java
 * Created on October 27, 2011, 10:22 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler2;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author jzamss
 */
public abstract class AbstractTaskProcessor implements Serializable, Runnable  {
    
    private ScheduleManager manager;
    protected LinkedBlockingQueue<TaskBean> queue = new LinkedBlockingQueue();
    
    AbstractTaskProcessor(ScheduleManager sm) {
        this.manager = sm;
    }
    
    
    public void push(TaskBean task){
        try {
            queue.put(task);
        } catch(Exception ign){
            System.out.println("not added to queue due to->"+ign.getMessage());
        }
    }
    
    /** Creates a new instance of AbstractTaskProcessor */
    public AbstractTaskProcessor() {
        
    }
    
    
    public ScheduleManager getManager() {
        return manager;
    }
    
    
}
