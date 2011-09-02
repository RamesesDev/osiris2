/*
 * CacheServiceListener.java
 * Created on September 2, 2011, 7:47 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver;

/**
 *
 * @author jzamss
 */
public interface CacheServiceListener {

    //passes the id and object that timed out
    void timeout(String id, Object info);
    
}
