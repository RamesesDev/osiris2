/*
 * CacheUpdateParam.java
 * Created on September 30, 2011, 10:22 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import java.io.Serializable;

/**
 * @author jzamss
 * 
 */
public class CacheUpdateParam implements Serializable {
    
    private Object data;
    private String statement;
    
    public CacheUpdateParam(String statement, Object data) {
        this.statement = statement;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getStatement() {
        return statement;
    }
    
    
    
}
