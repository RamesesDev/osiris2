/*
 * CacheServiceListener.java
 * Created on September 2, 2011, 7:47 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import java.io.Serializable;

/**
 *
 * @author jzamss
 */
public interface CacheListener extends Serializable {

    //passes the id and object that timed out
    void timeout(String id, Object info);
    void updated(String id, Object newinfo);
    void removed(String id, Object info);
    
}
