/*
 * UIActionSource.java
 *
 * Created on July 16, 2010, 11:09 AM
 * @author jaycverg
 */

package com.rameses.rcp.ui;


public interface UIActionSource extends UIControl {
    
    boolean isImmediate();
    String getTarget();
    String getActionName();
    
}
