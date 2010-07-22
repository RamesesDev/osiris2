package com.rameses.rcp.ui;

/**
 *
 * @author jaycverg
 */
public interface UICommand extends UIControl {
    
    String getTarget();
    String getActionName();
    boolean isImmediate();
    boolean isUpdate();
    boolean isDefaultCommand();
    
}
