package com.rameses.rcp.ui;

import com.rameses.rcp.framework.*;

/**
 *
 * @author jaycverg
 */
public interface UIControl extends Comparable {
    
    String getName();
    
    /**
     * @description
     *    specify the names or pattern of names of the controls when updated
     * can affect the value of this control
     */
    String[] getDepends();
    int getIndex();
    
    /**
     * @description
     *    happens the control is added to a UIViewPanel occurs one time only per page
     */
    void setBinding(Binding binding);
    Binding getBinding();
    
    /**
     * @description
     *    fires when binding.refesh is invoked
     */
    void refresh();
    
    /**
     * @description
     *    fires when binding changes the bean
     */
    void load();
    
}
