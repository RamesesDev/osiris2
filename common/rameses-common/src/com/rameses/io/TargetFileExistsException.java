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
 * This exception is thrown if the permament target file already exists.
 * Permanent target file refers to files that are successfully transferred.
 * The exact attribute is true if the target file info and the target 
 * file has the same date modified signature. This will give the
 * implementor the decision on what to do in these cases, whether to delete
 * the current file
 */
public class TargetFileExistsException extends IOException {
    
    private boolean exact;
    
    public TargetFileExistsException(boolean exact) {
        super( "Target file already exists" + ( exact ? " having an exact match" : ""  )   );
        this.exact = exact;
    }
    
     public TargetFileExistsException(String msg, boolean exact) {
        super(msg);
        this.exact = exact;
    }

    public boolean isExact() {
        return exact;
    }
    
}
