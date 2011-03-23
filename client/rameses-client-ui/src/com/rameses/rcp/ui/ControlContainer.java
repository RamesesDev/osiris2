/*
 * ControlContainer.java
 *
 * Created on October 19, 2010, 1:24 PM
 * @author jaycverg
 */

package com.rameses.rcp.ui;


public interface ControlContainer {
    
    /**
     *  This is implemented by controls/components that can act as dynamic
     * control container.
     *
     *  If the container has non-dynamic contents, the non-dynamic controls 
     * will be binded normally to the page's Binding.
     */
    boolean isHasNonDynamicContents();
    
}
