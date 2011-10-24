package com.rameses.server.session;

import com.rameses.client.session.SessionConstant;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
    private int timeout = 30000; //hold on to the poll for only 30 seconds
    private int expiryDuration = 120000; //expire the session after 2 minutes
    
    public void unregister(String tokenid) {
        tokens.remove(tokenid);
    }
    
    Session(String id, String username, Object info) {
        this.id = id;
        this.username = username;
        this.info = info;
        updateExpiry();
    }
    
    //when polling provide a token id
    public Object poll(String tokenid) {
        try {
            //if there is no token id yet, create one. and make sure to flush out all pending connections
            if (tokenid==null || tokenid.trim().length()==0) {
                tokenid = "TOKEN-" + (new UID()).hashCode();
                tokens.put( tokenid, new ArrayBlockingQueue(200) );
                //push("");
                return tokenid;
            } else if(!tokens.containsKey(tokenid)) {
                return SessionConstant.SESSION_DESTROYED;
            }
            
            //update the expiry time of the session everytime it is polled to keep it alive always
            this.updateExpiry();
            
            ArrayBlockingQueue q = tokens.get(tokenid);
            //set the next expiry date to check that if there is no response within this time,
            //the token queue should be removed.
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MILLISECOND, expiryDuration);
            expiryDates.put( tokenid, cal.getTime() );
            return q.poll( timeout, TimeUnit.MILLISECONDS );
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
        while (itr.hasNext()) {
            Entry me = (Map.Entry)itr.next();
            Date d = (Date)me.getValue();
            if (d.before(new Date())) {
                String tokenid = (String)me.getKey();
                //System.out.println("token " + tokenid + "  is expired ");
                tokens.remove(tokenid);
            }
        }
    }
    
    public Object getInfo() {
        updateExpiry();
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
    }
    
    public boolean canRemove() {
        Date d = new Date();
        return this.expirydate.before(d);
    }
    
}