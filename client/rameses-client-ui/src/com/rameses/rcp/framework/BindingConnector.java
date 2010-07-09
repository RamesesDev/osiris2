/*
 * BindingConnector.java
 *
 * Created on June 23, 2010, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.framework;

import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.ActionMessage;
import java.util.ArrayList;
import java.util.List;

public class BindingConnector {
    
    private Binding parentBinding;
    private List<Binding> subBindings = new ArrayList();
    private BindingListener listener = new ConnectorBindingListener();
    
    public BindingConnector() {
    }
    
    public Binding getParentBinding() {
        return parentBinding;
    }
    
    public void setParentBinding(Binding parentBinding) {
        this.parentBinding = parentBinding;
        parentBinding.addBindingListener(listener);
    }
    
    public List<Binding> getSubBindings() {
        return subBindings;
    }
    
    private class ConnectorBindingListener implements BindingListener {
        
        public void validate(ActionMessage actionMessage, Binding parent) {
            for (Binding sb : subBindings ) {
                sb.validate(actionMessage);
            }
        }
        
        public void notifyDepends(UIControl control, Binding parent) {
            for (Binding sb : subBindings) {
                sb.notifyDepends(control);
            }
        }
        
    }
    
}
