package com.rameses.rcp.util;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.NavigationHandler;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.ui.UICommand;
import com.rameses.rcp.framework.UIController;
import java.util.Stack;

/**
 *
 * @author jaycverg
 */
public class DefaultNavigationHandler implements NavigationHandler {
    
    public DefaultNavigationHandler() {
    }
    
    public void navigate(NavigatablePanel panel, UICommand source, Object outcome) {
        if ( panel == null ) return;
        
        Stack<UIController> conStack = panel.getControllers();
        UIController curController = conStack.peek();
        if ( outcome == null ) {
            curController.getCurrentView().refresh();
        } else {
            if( outcome instanceof Opener )  {
                Opener opnr = (Opener) outcome;
                ControllerProvider provider = ClientContext.getCurrentContext().getControllerProvider();
                UIController controller = provider.getController(opnr.getName());
                conStack.push(controller);
                
            } else if( outcome instanceof String ) {
                String oc = outcome+"";
                if ( outcome.equals("_close") ) {
                    if ( !conStack.isEmpty() ) {
                        UIController con = conStack.pop();
                    }
                    
                    if ( conStack.isEmpty() ) {
                        //close TopWindow
                        return;
                    }
                } else {
                   curController.setCurrentView( outcome+"" );
                }
            }
            panel.renderView();
        }
    }
    
    
}
