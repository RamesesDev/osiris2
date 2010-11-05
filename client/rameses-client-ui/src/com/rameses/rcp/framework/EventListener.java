/*
 * EventListener.java
 *
 * Created on November 5, 2010, 1:37 PM
 */

package com.rameses.rcp.framework;

import com.rameses.rcp.framework.ControlEvent;

/**
 *
 * @author jaycverg
 */
public interface EventListener {
    
    void onEvent(ControlEvent evt);
    
}
