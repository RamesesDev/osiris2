/*
 * AsyncResult.java
 *
 * Created on December 2, 2010, 9:56 AM
 * @author jaycverg
 */

package com.rameses.common;


public class AsyncResult {
    
    private static final long serialVersionUID = 1L;

    public static final int PROCESSING = 1;
    public static final int COMPLETED = 2;
    
    private int status;
    private Object value;

    
    public AsyncResult(Object value, int status) {
        this.value = value;
        this.status = status;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
