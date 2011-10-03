/*
 * SessionClientConnection.java
 * Created on September 15, 2011, 8:12 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.client.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jzamss
 */
public class Notifier implements Serializable,Runnable {
    
    private boolean polling;
    private ScheduledExecutorService poller;
    private List<MessageListener> listeners = new ArrayList();
    private String tokenid;
    private String sessionid;
    private ConnectionListener connectionListener;
    private NotificationServiceProvider serviceProvider;
    
    public Notifier(String sessionId, NotificationServiceProvider svc) {
        this.sessionid = sessionId;
        this.serviceProvider = svc;
    }
    
    public Notifier(String sessionId) {
        this.sessionid = sessionid;
    }

    public void setSessionServiceProvider(NotificationServiceProvider svc) {
        this.serviceProvider = svc;
    }
    
    public void setConnectionListener(ConnectionListener cl ) {
        this.connectionListener = cl;
    }
    
    public ConnectionListener getConnectionListener() {
        return this.connectionListener;
    }
    
    public void addListener(MessageListener handler) {
        this.listeners.add( handler );
    }
    
    public void removeListener(MessageListener handler) {
        this.listeners.remove(handler);
    }
    
    public void stop() {
        if(poller!=null)poller.shutdown();
        this.polling = false;
        this.sessionid = null;
    }
    
    private void cleanUp() {
        this.sessionid = null;
        this.listeners.clear();
    }
    
    public void connect() {
        if(this.serviceProvider==null)
            throw new RuntimeException("Please provide a NotificationServiceProvider");
        if(this.sessionid==null)
            throw new RuntimeException("The session has been destroyed. Please try to reconnect");
        
        if(polling) return;
        polling = true;
        poller = Executors.newScheduledThreadPool(1);
        poller.scheduleWithFixedDelay(this,50,50,TimeUnit.MILLISECONDS);
    }
    
    //poller.
    public void run() {
        try {
            Object data = serviceProvider.poll(this.sessionid, this.tokenid);
            if(data!=null) {
                if( data instanceof String ) {
                    String d = (String)data;
                    
                    //first time connected
                    if( d.startsWith("TOKEN")) {
                        tokenid = d;
                        //only signal a connection start if token id successfully gotten.
                        if(this.connectionListener!=null) {
                            this.connectionListener.started();
                        }
                        return;
                    }
                    
                    //server was shutdown
                    else if(d.equals(SessionConstant.SESSION_ABORTED)
                    || d.equals(SessionConstant.SESSION_ENDED)
                    || d.equals(SessionConstant.SESSION_EXPIRED)
                    ||  d.equals(SessionConstant.SESSION_DESTROYED)
                    ) {
                        stop();
                        if(this.connectionListener!=null) {
                            this.connectionListener.ended(d);
                        }
                        cleanUp();
                        return;
                    }
                    
                    //no message found on this run. retry polling
                    else if(d.trim().length()==0) {
                        return;
                    }
                }
                for(MessageListener h: this.listeners) {
                    h.onMessage(data);
                }
            }
        } catch(Exception ign) {
            System.out.println("on message error ignored->"+ign.getMessage());
        }
    }
    
    public String toString() {
        return this.sessionid;
    }
    
}
