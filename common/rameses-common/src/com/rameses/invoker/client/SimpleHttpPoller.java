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

/**
 * this class listens and long polls the server. reconnects everytime
 */
public class SimpleHttpPoller  {
    
    private boolean cancelled;
    private boolean started;
    private String id;
    private SimpleHttpClient client;
    private String host;
    private String method = "poll";
    private List<MessageHandler> handlers = new ArrayList();
    private long fixedDelay = 1000;
    
    public SimpleHttpPoller(String host, String id) {
        this.id = id;
        this.host = host;
    }
    
    public void start() {
        if(started) return;
        started = true;
        client = new SimpleHttpClient(host);
        Map map = new HashMap();
        map.put("id", id);
        while(!cancelled) {
            try {
                String s = client.post(method,map);
                if(s!=null && s.trim().length()>0) {
                    for(MessageHandler h: handlers) {
                        h.onMessage(s);
                    } 
                }
            }
            catch(Exception ex) {
                break;
            }
        }
        cancelled = false;
        started = false;
    }
    
    public static interface MessageHandler {
        void onMessage(String result);
    }
    
    public void addHandler(MessageHandler handler) {
        handlers.add(handler);
    }
    
    
    public void stop() {
        this.cancelled = true;
        this.started = false;
    }
    
    public long getFixedDelay() {
        return fixedDelay;
    }

    public void setFixedDelay(long fixedDelay) {
        this.fixedDelay = fixedDelay;
    }

}
