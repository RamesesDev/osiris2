package com.rameses.rcp.ui;

/**
 *
 * @author jaycverg
 */
public interface UIInput extends UIOutput {
    
    void setValue(Object value);
    boolean isNullWhenEmpty();
    void setReadonly(boolean readonly);
    boolean isReadonly();
    void setRequestFocus(boolean focus);
    boolean isImmediate();
    
}
