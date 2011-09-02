/*
 * Task.java
 * Created on August 26, 2011, 12:07 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler;

/**
 *
 * @author jzamss
 */
public class Task implements Runnable {
    
    /** Creates a new instance of Task */
    public Task() {
    }

    public void run() {
        //do the task here.
        //ScriptService fire. 
        //after executing, store back to queue.
    }
    
    public boolean isExpired() {
        return true;
    }
    
}
