/*
 * Opener.java
 *
 * Created on October 14, 2009, 7:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.rcp.common;

public class WindowOpener extends Opener {
    
    private String frame = "default";
    
    public WindowOpener() {
        super();
    }
    
    /**
     * This forces the target must be a window
     */
    public String getTarget() {
        return "_window";
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }
    
}
