package com.rameses.rcp.impl;

import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControlSupport;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.NavigationHandler;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.rcp.ui.UIControl;
import com.rameses.util.ValueUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.swing.JComponent;

/**
 * @author jaycverg
 * @description
 *   handles page navigation using the actions outcome
 */
public class NavigationHandlerImpl implements NavigationHandler {
    
    
    public void navigate(NavigatablePanel panel, UIControl source, Object outcome) {
        if ( panel == null ) return;
        
        JComponent sourceComp = (JComponent) source;
        ClientContext ctx = ClientContext.getCurrentContext();
        Platform platform = ctx.getPlatform();
        
        Stack<UIControllerContext> conStack = panel.getControllers();
        UIControllerContext curController = conStack.peek();
        
        if ( ValueUtil.isEmpty(outcome) ) {
            //if outcome is null or empty just refresh the current view
            curController.getCurrentView().refresh();
            
        } else {
            if( outcome instanceof Opener )  {
                Opener opener = (Opener) outcome;
                opener = ControlSupport.initOpener( opener, curController.getController() );
                boolean self = ValueUtil.isEmpty(opener.getTarget());
                String id = opener.getId();
                
                if ( !self && platform.isWindowExists( id ) ) {
                    platform.activateWindow( id );
                    return;
                }
                
                UIController opCon = opener.getController();
                String permission = opener.getPermission();
                
                //check permission(if allowed) when specified
                if ( !ValueUtil.isEmpty(permission) ) {
                    permission = opCon.getName() + "." + permission;
                    if( !ControlSupport.isPermitted(permission) ) {
                        MsgBox.err("You don't have permission to perform this transaction.");
                        return;
                    }
                    
                }
                
                UIControllerContext controller = new UIControllerContext(opCon);
                if ( !ValueUtil.isEmpty(opener.getOutcome()) ) {
                    controller.setCurrentView( opener.getOutcome() );
                }
                
                if ( self ) {
                    conStack.push(controller);
                    
                } else {
                    UIControllerPanel uic = new UIControllerPanel(controller);
                    
                    Map props = new HashMap();
                    props.put("id", opener.getId());
                    props.put("title", controller.getTitle());
                    props.put("modal", opener.isModal());
                    
                    if ( "_popup".equals(opener.getTarget()) ) {
                        platform.showPopup(sourceComp, uic, props);
                    } else {
                        platform.showWindow(sourceComp, uic, props);
                    }
                }
                
            } else if( outcome instanceof String ) {
                String oc = outcome+"";
                if ( oc.equals("_close") ) {
                    if ( !conStack.isEmpty() ) {
                        if ( conStack.size() > 1 ) {
                            conStack.pop();
                        } else {
                            String conId = curController.getId();
                            platform.closeWindow(conId);
                        }
                    }
                    
                } else if ( oc.equals("_exit")) {
                    //get the original owner of he window
                    while ( conStack.size() > 1 ) {
                        conStack.pop();
                    }
                    String conId = conStack.peek().getId();
                    platform.closeWindow(conId);
                    
                } else {
                    if ( oc.startsWith("_") ) oc = oc.substring(1);
                    curController.setCurrentView( oc );
                    
                    //update binding injections based on current view
                    curController.getCurrentView().getBinding().reinjectAnnotations();
                }
            }
            
            //refresh new view
            panel.renderView();
        }
    }
    
}
