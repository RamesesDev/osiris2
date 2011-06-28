/*
 * TaskManager.java
 *
 * Created on October 19, 2010, 8:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ms
 */
public final class TaskManager {
    
    private static TaskManager instance;
    
    public static TaskManager getInstance() {
        if(instance==null) {
            instance = new TaskManager();
        }
        return instance;
    }
    
    public static void setInstance(TaskManager aInstance) {
        instance = aInstance;
    }
    
    //just arbitrarily assign five even if we dont use it
    private ScheduledExecutorService mainExecutor;
    private boolean debug = false;
    private boolean started = false;
    private long delay = 2000;
    
    private ScheduledFuture tracker;
    
    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public synchronized  void start() {
        if(!started) {
            started = true;
            System.out.println("          STARTING SCHEDULER @"+ new Date());
            mainExecutor = Executors.newScheduledThreadPool(1);
            tracker = mainExecutor.scheduleWithFixedDelay(new TaskSchedulerWorker(this), delay, delay,TimeUnit.MILLISECONDS);
        }
    }
    
    public  synchronized  void stop() {
        if( started ) {
            started = false;
            System.out.println("          STOPPING SCHEDULER @"+new Date());
            tracker.cancel(true);
            mainExecutor.shutdown();
        }
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }


    
}
