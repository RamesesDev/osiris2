/*
 * UISubControl.java
 *
 * Created on July 28, 2010, 10:46 AM
 * @author jaycverg
 */

package com.rameses.rcp.ui;

public interface UISubControl extends UIFocusableContainer {
 
    String getCaption();
    Object getHandlerObject();
    
}
