package com.rameses.rcp.framework;

import com.rameses.rcp.ui.UIControl;

/**
 *
 * @author jaycverg
 */
public interface NavigationHandler {
    
    void navigate(NavigatablePanel panel, UIControl source, Object outcome);
    
}
