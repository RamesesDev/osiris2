package com.rameses.rcp.impl;

import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.NavigationHandler;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.ui.UIControl;
import com.rameses.util.ValueUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.swing.JComponent;

/**
 *
 * @author jaycverg
 */
public class NavigationHandlerImpl implements NavigationHandler {
    
    
    public void navigate(NavigatablePanel panel, UIControl source, Object outcome) {
        if ( panel == null ) return;
        
        JComponent sourceComp = (JComponent) source;
        ClientContext ctx = ClientContext.getCurrentContext();
        Platform platform = ctx.getPlatform();
        
        Stack<UIController> conStack = panel.getControllers();
        UIController curController = conStack.peek();
        
        if ( ValueUtil.isEmpty(outcome) ) {
            curController.getCurrentView().refresh();
        } else {
            if( outcome instanceof Opener )  {
                Opener opener = (Opener) outcome;
                boolean self = ValueUtil.isEmpty(opener.getTarget());
                String id = opener.getId();
                String caption = opener.getCaption();
                
                if ( !self && platform.isWindowExists( id ) ) {
                    platform.activateWindow( id );
                    return;
                }
                
                ControllerProvider provider = ctx.getControllerProvider();
                UIController controller = provider.getController(opener.getName());
                controller.init(opener.getParams(), opener.getAction());
                
                if ( self ) {
                    conStack.push(controller);
                    
                } else {
                    UIControllerPanel uic = new UIControllerPanel(controller);
                    controller.setId( id );
                    controller.setTitle( ValueUtil.isEmpty(caption)? id: caption );
                    controller.init( opener.getParams(), opener.getAction() );
                    
                    Map props = new HashMap();
                    props.put("id", controller.getId());
                    props.put("title", controller.getTitle());
                    
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
                } else {
                    if ( oc.startsWith("_") ) oc = oc.substring(1);
                    curController.setCurrentView( oc );
                    
                    //update binding injections based on current view
                    curController.getCurrentView().getBinding().reinjectAnnotations();
                }
            }
            panel.renderView();
        }
    }
    
}
