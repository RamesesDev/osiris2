/*
 * MessageInvoker.java
 * Created on July 23, 2011, 9:23 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.messaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jzamss
 */
public class MessageInvoker implements Serializable {
    
    private List<EventHandler> handlers = new ArrayList();
    
    public MessageInvoker() {
    }
    
    public void addHandler(EventHandler handler) {
        handlers.add(handler);
    }
    
    public void invoke(QueueMessage qm) {
        for(EventHandler h: handlers) {
            final EventHandler handler = h;
            final QueueMessage msg = qm;
            ExecutorService svc = Executors.newCachedThreadPool();
            svc.submit( new Runnable() {
                public void run(){
                    handler.onMessage(msg);
                }
            });
        }
    }
    
    public void destroy() {
        handlers.clear();
    }
    
}
