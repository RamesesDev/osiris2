package com.rameses.rcp.util;

import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.NavigationHandler;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.ui.UICommand;
import com.rameses.rcp.framework.UIController;
import com.rameses.util.ValueUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.swing.JComponent;

/**
 *
 * @author jaycverg
 */
public class DefaultNavigationHandler implements NavigationHandler {

    
    public void navigate(NavigatablePanel panel, UICommand source, Object outcome) {
        if ( panel == null ) return;
        
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
                
                if ( self ) {
                    conStack.push(controller);
                } else {
                    UIControllerPanel uic = new UIControllerPanel(controller);
                    controller.setId( id );
                    controller.setTitle( ValueUtil.isEmpty(caption)? id: caption );                    
                    
                    Map props = new HashMap();
                    props.put("id", controller.getId());
                    props.put("title", controller.getTitle());
                    
                    JComponent sourceComp = (JComponent) source;
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
                            boolean exists = platform.isWindowExists(conId);
                            if ( !exists ) {
                                conStack.pop(); //remove if successfully closed.
                            }
                        }
                    }
                } else {
                    if ( oc.startsWith("_") ) oc = oc.substring(1);
                    curController.setCurrentView( oc );
                }
            }
            panel.renderView();
        }
    }
    
}
