/*
 * BindingConnector.java
 *
 * Created on June 23, 2010, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.ui;

import com.rameses.rcp.common.ValidatorEvent;
import com.rameses.rcp.framework.*;
import com.rameses.rcp.util.ActionMessage;
import java.awt.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BindingConnector implements BindingListener {
    
    private Binding parentBinding;
    private Set<Binding> subBindings = new HashSet();
    private UISubControl parent;
    
    
    public BindingConnector(UISubControl parent) {
        this.parent = parent;
    }
    
    public Binding getParentBinding() {
        return parentBinding;
    }
    
    public void setParentBinding(Binding parentBinding) {
        if ( parentBinding == this.parentBinding ) return;
        
        if ( this.parentBinding != null )
            this.parentBinding.removeListener(this);
        
        if( parentBinding != null )
            parentBinding.addBindingListener(this);
        
        this.parentBinding = parentBinding;
    }
    
    public Set<Binding> getSubBindings() {
        return subBindings;
    }
    
    public void validate(ActionMessage actionMessage, Binding pbinding) {
        //do not participate in validation
        //when the subform is not attched to a Component
        if( ((Component) parent).getParent() == null ) return;
        
        ActionMessage subMessages = new ActionMessage();
        for (Binding sb : subBindings ) {
            sb.validate(subMessages);
        }
        if ( subMessages.hasMessages() ) {
            actionMessage.addMessage(subMessages, parent.getCaption());
        }
    }
    
    public void validateBean(ValidatorEvent evt) {
        for(Binding sb: subBindings) {
            ValidatorEvent subEvt = new ValidatorEvent(sb);
            sb.validateBean(subEvt);
            evt.add(subEvt);
        }
    }
    
    public void notifyDepends(UIControl control, Binding parent) {
        for (Binding sb : subBindings) {
            sb.notifyDepends(control);
        }
    }
    
    public void refresh(String fieldRegEx) {
        for (Binding sb : subBindings) {
            sb.refresh(fieldRegEx);
        }
    }
    
    public void formCommit() {
        for (Binding sb : subBindings) {
            sb.formCommit();
        }
    }
    
    public void update() {
        for (Binding sb : subBindings) {
            sb.update();
        }
    }
    
    public UIControl getParent() {
        return parent;
    }
    
    public void setParent(UISubControl parent) {
        this.parent = parent;
    }
    
}
