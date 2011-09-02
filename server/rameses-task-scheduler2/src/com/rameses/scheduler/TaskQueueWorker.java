/*
 * TaskQueue.java
 * Created on August 26, 2011, 12:02 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * this class looks up tasks in the queue and launches it.
 */
public class TaskQueueWorker implements Runnable {
    
    private LinkedBlockingQueue queue = new LinkedBlockingQueue();
    
    
    
    public TaskQueueWorker() {
    }

    public void run() {
        
        
    }
    
}
