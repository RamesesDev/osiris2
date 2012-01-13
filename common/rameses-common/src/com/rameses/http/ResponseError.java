/*
 * ResponseError.java
 * Created on September 19, 2011, 2:08 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.http;

/**
 *
 * @author jzamss
 */
public class ResponseError extends Exception {
    
    private int errno;
    

    public ResponseError(int errno, String msg) {
        super(msg);
        this.errno = errno;
    }
    
    public ResponseError(int errno, String msg, Exception orig) {
        super(msg, orig);
        this.errno = errno;
    }

    public int getErrno() {
        return errno;
    }

    public String toString() {
        return super.getMessage() + " [HTTP Response Code:"+errno+"]";
    }
    

}
