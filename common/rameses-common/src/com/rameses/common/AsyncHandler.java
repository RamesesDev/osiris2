/*
 * AsyncHandler.java
 *
 * Created on October 24, 2010, 1:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.common;


/**
 *
 * @author ms
 */
public interface AsyncHandler {
    
    static final long serialVersionUID = 1L;
    
    public abstract void onMessage(AsyncResult o);
    
}
