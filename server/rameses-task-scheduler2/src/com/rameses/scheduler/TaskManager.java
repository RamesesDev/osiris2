/*
 * TaskManager.java
 * Created on August 26, 2011, 12:06 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author jzamss
 */
public class TaskManager implements Runnable  {
    
    private LinkedBlockingQueue<Task> queueList = new LinkedBlockingQueue();
    
    private boolean cancelled;
    
    public TaskManager() {
    }

    
    public void addTask( Task queue ) {
        queueList.add(  queue );
    }

    public void start() {
        if(cancelled) return;
        cancelled = false;
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(this);
    }
    
    public void stop() {
        cancelled = true;
    }

    public void run() {
        while(!cancelled) {
            Task t = queueList.poll();
            //if not yet expired place this back in the queue for later processing
            if(!t.isExpired()) queueList.add( t );
            
            if(t!=null) {
                ExecutorService thread = Executors.newCachedThreadPool();
                thread.submit( t );
            }
        }
        System.out.println("stopped the task");
    }
    
    
}
