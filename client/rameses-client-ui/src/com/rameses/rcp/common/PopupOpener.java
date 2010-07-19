/*
 * Opener.java
 *
 * Created on October 14, 2009, 7:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.rcp.common;

public class PopupOpener extends Opener {
    
    public PopupOpener() {
        super();
    }
    
    /**
     * This forces the target must be a popup
     */
    public String getTarget() {
        return "_popup";
    }
    
}
