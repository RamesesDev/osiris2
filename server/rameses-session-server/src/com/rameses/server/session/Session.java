package com.rameses.server.session;

import java.io.Serializable;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;


public class Session implements Serializable {
    
    
    
    private Object info;
    private String id;
    private String username;
    private Date expirydate;
    
    private Map<String, ArrayBlockingQueue> tokens = new HashMap();
    private Map<String, Date> expiryDates = new HashMap();
    private int timeout; 
    private int pollTimeout; //hold on to the poll for only 30 seconds;
    
    public void unregister(String tokenid) {
        tokens.remove(tokenid);
    }
    
    Session(String id, String username, Object info, int timeout,int pollTimeout) {
        this.id = id;
        this.username = username;
        this.info = info;
        this.timeout = timeout;
        this.pollTimeout = pollTimeout;
        updateExpiry();
    }
    
    private void updateTokenTimeout(String tokenid) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MILLISECOND, pollTimeout);
        expiryDates.put( tokenid, cal.getTime() );
    }
    
    //when polling provide a token id
    public Object poll(String tokenid) {
        try {
            this.updateExpiry();
            //if there is no token id yet, create one. and make sure to flush out all pending connections
            if (tokenid==null || tokenid.trim().length()==0) {
                tokenid = "TOKEN-" + (new UID()).hashCode();
                tokens.put( tokenid, new ArrayBlockingQueue(200) );
                //push("");
                updateTokenTimeout(tokenid);
                return tokenid;
            } 
            else if(!tokens.containsKey(tokenid)) {
                tokens.put( tokenid, new ArrayBlockingQueue(200) );
                updateTokenTimeout(tokenid);
                return tokenid;
            }
            
            //update the expiry time of the session everytime it is polled to keep it alive always
            updateTokenTimeout(tokenid);
            ArrayBlockingQueue q = tokens.get(tokenid);
            //set the next expiry date to check that if there is no response within this time,
            //the token queue should be removed.
            return q.poll( pollTimeout, TimeUnit.MILLISECONDS );
            
        }  catch (Exception ex) {
            return null;
        }
    }
    
    public void push(Object message) {
        Iterator<String> iter = tokens.keySet().iterator();
        while (iter.hasNext()){
            String k = iter.next();
            try {
                tokens.get(k).add( message );
            } catch (Exception e) {
                System.out.println("exceed message queue size");
            }
        }
    }
    
    /*
     * destroy messages are:
     *  expired
     *  end
     */
    public void destroy(String message) {
        push(message);
        tokens.clear();
        tokens = null;
    }
    
    public void cleanUnusedSessions() {
        Iterator itr = expiryDates.entrySet().iterator();
        List<String> forRemoval = new ArrayList();
        while (itr.hasNext()) {
            Entry me = (Map.Entry)itr.next();
            Date d = (Date)me.getValue();
            if (d.before(new Date())) {
                String tokenid = (String)me.getKey();
                //System.out.println("token " + tokenid + "  is expired ");
                tokens.remove(tokenid);
                forRemoval.add( tokenid);
            }
        }
        for(String k : forRemoval) {
            expiryDates.remove(k);
        }        
    }
    
    public Object getInfo() {
        return info;
    }
    
    public String getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    private void updateExpiry() {
        Calendar cal = Calendar.getInstance();
        Date d = new Date();
        cal.setTime(d);
        cal.add(Calendar.MILLISECOND, this.timeout);
        this.expirydate = cal.getTime();
        //System.out.println("updating expiry "+ this.expirydate);
    }
    
    public boolean isExpired() {
        //if timeout is -1 or lower, there is no session timoeut  
        if(this.timeout<0) return false;
        Date d = new Date();
        return this.expirydate.before(d);
    }
    
    public Date getExpirydate() {
        return expirydate;
    }
    
}