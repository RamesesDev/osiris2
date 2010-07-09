package com.rameses.rcp.framework;

import com.rameses.rcp.framework.UIController;
import java.util.Stack;

/**
 *
 * @author jaycverg
 */
public interface NavigatablePanel {
    
    /***
     * @description 
     *    returns the Stack of UIControllers in the NavigatablePanel 
     */
    Stack<UIController> getControllers();
    
    /**
     * @description
     *    this method is called by the navigation handler
     * to redraw or refresh the current page of a NavigatablePanel
     * during outcome navigation of a UICommand
     */
    void renderView();
    
}
