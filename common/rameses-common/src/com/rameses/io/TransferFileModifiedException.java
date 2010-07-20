/*
 * TransferFileModifiedException.java
 *
 * Created on July 20, 2010, 2:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

/**
 *
 * @author ms
 */
public class TransferFileModifiedException extends Exception {
    
    public TransferFileModifiedException() {
        super();
    }
    
     public TransferFileModifiedException(String msg) {
        super(msg);
    }
    
}
