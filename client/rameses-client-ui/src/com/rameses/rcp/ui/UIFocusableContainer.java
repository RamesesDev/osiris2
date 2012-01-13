/*
 * UIFocusableContainer.java
 *
 * Created on October 19, 2010, 1:02 PM
 * @author jaycverg
 */

package com.rameses.rcp.ui;


/**
 * Interface that is implemented by container that contains
 * focusable UIControls
 */
public interface UIFocusableContainer extends UIControl {
    
    boolean focusFirstInput();
    
}
