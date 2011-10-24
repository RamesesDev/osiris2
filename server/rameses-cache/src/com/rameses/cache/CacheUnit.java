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
public abstract class CacheUnit implements Serializable {
    
    private int timeout;
    private Date expirydate = new Date();
    protected String name;
    
    public CacheUnit(String name) {
        this.name = name;
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
    
    public abstract Object getContent();
    public abstract void updateContent(Object info);
    public abstract boolean destroy(boolean timedout);
    
    public String toString() {
        return "name:" + name + " expires on:" + this.expirydate;
    }

    public String getName() {
        return name;
    }
    
}
