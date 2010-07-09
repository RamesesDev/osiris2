package com.rameses.rcp.ui;

import com.rameses.rcp.framework.*;

/**
 *
 * @author jaycverg
 */
public interface UIInput extends UIControl {
    
    Object getValue();
    void setValue(Object value);
    boolean isNullWhenEmpty();
    
}
