/*
 * HttpNotifier.java
 * Created on July 27, 2011, 11:16 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.invoker.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this class listens and long polls the server. reconnects everytime
 */
public class SimpleHttpNotifier implements Runnable {
    
    private String path;
    private SimpleHttpClient client;
    private AtomicBoolean started = new AtomicBoolean(false);
    private AtomicBoolean cancelled = new AtomicBoolean(false);
    private Map params = new HashMap();
    private LinkedBlockingQueue errs = new LinkedBlockingQueue();
    private List<MessageHandler> handlers = new ArrayList();
    
    //this value is in milliseconds
    private long timeBetweenCalls = 0;
    
    public SimpleHttpNotifier(String host,String path) {
        client = new SimpleHttpClient(host);
        this.path = path;
    }

    public void start() {
        if(started.compareAndSet(false,true)) {
            ExecutorService service = Executors.newFixedThreadPool(1);
            service.submit(this);
        }
    }
    
    public void stop() {
        cancelled.compareAndSet(false,true);
    }
    
    public void run() {
        System.out.println("starting code");
        while(!cancelled.get()) {
            try {
                String value = client.post(path, params);
                if(value!=null && value.trim().length() > 0) {
                    for(MessageHandler listener: handlers) {
                        listener.onMessage(value);
                    }
                }
            } catch(Exception ign) {
                errs.add( ign.getMessage() );
            }
        }
        System.out.println("stopping");
        started.compareAndSet(true,false);
        cancelled.compareAndSet(true,false);
    }
    
    public static interface MessageHandler {
        void onMessage(String message);
    }

    public int getTimeout() {
        return client.getTimeout();
    }

    public void setTimeout(int timeout) {
        client.setTimeout( timeout );
    }

    public int getReadTimeout() {
        return client.getReadTimeout();
    }

    public void setReadTimeout(int readTimeout) {
        client.setReadTimeout( readTimeout );
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }
    
    public void addHandler(MessageHandler h) {
        this.handlers.add(h);
    }
}
