/*
 * SimpleQueueProcessor.java
 * Created on July 23, 2011, 3:06 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.messaging;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jzamss
 */
public class ListEventQueue extends AbstractEventQueue implements Runnable  {
    
    private LinkedBlockingQueue<QueueMessage> queue;
    private boolean cancelled;
    private boolean started;
    
    /** Creates a new instance of SimpleQueueProcessor */
    public ListEventQueue() {
        init();
    }
    
    public void init() {
        queue = new LinkedBlockingQueue();
    }
    
    public void start() {
        if(started) return;
        started = true;
        ExecutorService mainThread = Executors.newFixedThreadPool(1);
        mainThread.submit(this);
    }
    
    public void stop() {
        cancelled = true;
    }
    
    public void shutdown() {
        queue.clear();
        super.destroy();
        queue = null;
    }
    
    public void send(QueueMessage msg) {
        queue.add(msg);
    }
    
    public void run() {
        System.out.println("starting queue");
        while(!cancelled) {
            try {
                QueueMessage qm = queue.poll(1,TimeUnit.SECONDS);
                invoker.invoke(qm);
            } catch(Exception ign) {;}
        }
        System.out.println("stopped event queue");
        cancelled = false;
        started = false;
    }
}
