package com.rameses.rcp.ui;

/**
 *
 * @author jaycverg
 */
public interface UIInput extends UIControl {
    
    Object getValue();
    void setValue(Object value);
    boolean isNullWhenEmpty();
    String getOnAfterUpdate();    
    
}
