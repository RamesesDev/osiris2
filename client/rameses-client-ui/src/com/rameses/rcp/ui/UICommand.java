package com.rameses.rcp.ui;

/**
 *
 * @author jaycverg
 */
public interface UICommand extends UIControl {
    
    boolean isUpdate();
    boolean isImmediate();
    String getTarget();
    String getActionName();
    
}
