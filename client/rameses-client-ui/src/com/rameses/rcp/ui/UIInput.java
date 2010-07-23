package com.rameses.rcp.ui;

/**
 *
 * @author jaycverg
 */
public interface UIInput extends UIOutput {
    
    void setValue(Object value);
    boolean isNullWhenEmpty();
    String getOnAfterUpdate();
    void setReadonly(boolean readonly);
    boolean isReadonly();
    
}
