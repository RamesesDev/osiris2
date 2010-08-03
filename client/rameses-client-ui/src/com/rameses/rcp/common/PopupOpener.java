/*
 * Opener.java
 *
 * Created on October 14, 2009, 7:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.rcp.common;

import java.rmi.server.UID;

public class PopupOpener extends Opener {
    
    public PopupOpener() {
        super();
        
        //opener id is used by the controller as its id
        //this is important when closing a window because
        //the platform needs a window id to determine what window to close
        setId("Popup" + new UID());
    }
    
    /**
     * This forces the target must be a popup
     */
    public String getTarget() {
        return "_popup";
    }
    
}
