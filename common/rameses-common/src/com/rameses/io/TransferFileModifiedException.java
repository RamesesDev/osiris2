/*
 * TransferFileModifiedException.java
 *
 * Created on July 20, 2010, 2:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

import java.io.IOException;

/**
 *
 * @author ms
 */
public class TransferFileModifiedException extends IOException {
    
    public TransferFileModifiedException() {
        super();
    }
    
     public TransferFileModifiedException(String msg) {
        super(msg);
    }
    
}

