package com.rameses.rcp.framework;

import com.rameses.rcp.ui.UICommand;

/**
 *
 * @author jaycverg
 */
public interface NavigationHandler {
    
    void navigate(NavigatablePanel panel, UICommand source, Object outcome);
    
}
