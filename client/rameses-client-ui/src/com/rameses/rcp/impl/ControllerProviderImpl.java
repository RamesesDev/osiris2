package com.rameses.rcp.impl;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.UIController;

/**
 *
 * @author jaycverg
 */
public class ControllerProviderImpl implements ControllerProvider {
    
    public UIController getController(String name) {
        ClassLoader loader = ClientContext.getCurrentContext().getClassLoader();
        try {
            return (UIController) loader.loadClass(name).newInstance();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
}
