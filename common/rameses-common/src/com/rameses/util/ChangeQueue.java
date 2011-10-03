/*
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.util;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Changes are sent to the queue.
 * the update is fired to apply the changes in the queue.
 *
 */
public class ChangeQueue implements Runnable,Serializable {
    
    private Queue queue = new LinkedBlockingQueue();
    private AtomicBoolean updating = new AtomicBoolean(false);
    private ExecutorService thread = Executors.newFixedThreadPool(1);
    private Handler handler;
    
    public ChangeQueue(Handler h) {
        this.handler = h;
    }
    
    public void push(Object info) {
        queue.add( info );
    }
    
    public void applyChanges() {
        if(updating.compareAndSet(false,true)) {
            thread.submit( this );
        }
    }
    
    public boolean isUpdating() {
        return updating.get();
    }
    
    public void run() {
        Object v = null;
        while((v=queue.poll())!=null) {
             handler.update(v);
        }
        handler.onAfterUpdate();
        updating.set(false);
    }
    
    public static interface Handler {
        void update(Object data);
        void onAfterUpdate();
    }
}
