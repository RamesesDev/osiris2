package com.rameses.rcp.framework;

import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.ActionMessage;

/**
 *
 * @author jaycverg
 */
public interface BindingListener {
    
    public void validate(ActionMessage actionMessage, Binding parent);
    public void notifyDepends(UIControl control, Binding parent);
    public void refresh(String fieldRegEx);
    public void formCommit();
    public void update();
    
}
