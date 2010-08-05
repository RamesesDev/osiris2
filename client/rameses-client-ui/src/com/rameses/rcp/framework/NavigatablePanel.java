package com.rameses.rcp.framework;

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
    Stack<UIControllerContext> getControllers();
    void setControllers(Stack<UIControllerContext> controllers);
    /**
     * @description
     *    this method is called by the navigation handler
     * to redraw or refresh the current page of a NavigatablePanel
     * during outcome navigation of a UICommand
     */
    void renderView();
    
}
