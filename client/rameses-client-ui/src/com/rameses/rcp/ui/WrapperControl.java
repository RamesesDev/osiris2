/*
 * WrapperControl.java
 *
 * Created on November 5, 2010, 4:50 PM
 */

package com.rameses.rcp.ui;

import java.awt.Component;

/**
 *
 * @author jaycverg
 * 
 * @description
 *  this is implemented by UIControls that wrapped a component
 */
public interface WrapperControl {
    
    Component getWrappedComponent();
    
}
