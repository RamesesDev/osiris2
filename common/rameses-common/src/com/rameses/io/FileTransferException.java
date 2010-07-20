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
 * This exception is thrown when writing to the FileTransferOutputStream
 * and occurs if the the byte position is unsynchronized. 
 * The current pos value indicates the current byte position of the
 * target file.   
 */
public class FileTransferException extends IOException {
    
    private long currentPos;
    
    public FileTransferException(long currentPos) {
        super();
        this.currentPos = currentPos;
    }

    public long getCurrentPos() {
        return currentPos;
    }
    
    
}
