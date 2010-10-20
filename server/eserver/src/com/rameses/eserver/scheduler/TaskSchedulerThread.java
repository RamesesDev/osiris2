/*
 * TaskSchedulerThread.java
 *
 * Created on October 19, 2010, 8:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;

/**
 *
 * @author ms
 */
public class TaskSchedulerThread extends Thread {
    
    private boolean cancelled;
    private int delay = 2000;
    private int fetchSize = 3;
    
    public TaskSchedulerThread() {
    }
    
    public int getDelay() {
        return delay;
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public void cancel() {
        cancelled = true;
    }
    
    public void run() {
        //there should only be 1 connection for the isolation level to work.
        while(!cancelled) {
            try {
                TaskSchedulerServiceLocal l = TaskSchedulerDelegate.getTaskSchedulerService();
                if(l!=null) {
                    l.fireActiveQueue(fetchSize);
                }
            }
            catch(Exception e) {
                System.out.println("ERROR LOOKUP SCRIPT");
            }
            try {
                sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
}
