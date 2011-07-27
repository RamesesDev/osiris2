/*
 * EventQueue.java
 * Created on July 23, 2011, 9:22 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.messaging;

import java.io.Serializable;

/**
 *
 * @author jzamss
 */
public abstract class AbstractEventQueue implements Serializable {
    
    protected MessageInvoker invoker = new MessageInvoker();
    
    public abstract void init();
    public abstract void send( QueueMessage msg );
    public abstract void start();
    public abstract void stop();
    public abstract void shutdown();
    
    public void addEventHandler(EventHandler h) {
        invoker.addHandler(h);
    }

    protected void destroy() {
        invoker.destroy();
        invoker = null;
    }
    
    
}
