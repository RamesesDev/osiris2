/*
 * CacheContent.java
 * Created on September 28, 2011, 10:30 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author jzamss
 */
public class CacheUnit implements Serializable {
    
    private int timeout;
    private Date expirydate = new Date();
    protected String name;
    private Object content;
    private CacheListener listener;
    
    public CacheUnit(String name) {
        this.name = name;
    }
    public void setListener(CacheListener listener) {
        this.listener = listener;
    }
    
    public boolean isExpired() {
        Date d = new Date();
        return this.expirydate.before(d);
    }
    
    public void updateExpiry() {
        Calendar cal = Calendar.getInstance();
        Date d = new Date();
        cal.setTime(d);
        cal.add(Calendar.MILLISECOND, this.timeout);
        this.expirydate = cal.getTime();
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
        updateExpiry();
    }
    
    public Object getContent() {
        return content;
    }
    
    public void updateContent(Object info) {
        this.content = info;
    }
    
    public boolean destroy(boolean timedout) {
        if(this.listener!=null) {
            if(timedout)
                this.listener.timeout(name, content);
            else
                this.listener.removed(name, content);
        }
        content = null;
        this.listener = null;
        return true;
    }
    
    public String toString() {
        return "name:" + name + " expires on:" + this.expirydate;
    }
    
    public String getName() {
        return name;
    }

    public void setContent(Object content) {
        this.content = content;
    }
    
}
