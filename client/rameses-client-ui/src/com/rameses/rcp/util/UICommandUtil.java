package com.rameses.rcp.util;

import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.*;
import com.rameses.rcp.ui.UICommand;
import com.rameses.util.MethodResolver;
import com.rameses.util.ValueUtil;
import java.beans.Beans;
import javax.swing.JComponent;

/**
 *
 * @author jaycverg
 */
public class UICommandUtil {
    
    public static void processAction(UICommand command) {
        if ( Beans.isDesignTime() ) return;
        
        try {
            ClientContext ctx = ClientContext.getCurrentContext();
            MethodResolver resolver = ctx.getMethodResolver();
            Binding binding = command.getBinding();
            validate(command, binding);
            
            String target = ValueUtil.isEmpty(command.getTarget())? "parent": command.getTarget();
            NavigatablePanel navPanel = UIControlUtil.getParentPanel(command, target);
            if ( "root".equals(target) ) {
                UIController rootCon = (UIController) navPanel.getControllers().peek();
                Binding rootBinding = rootCon.getCurrentView().getBinding();
                validate(command, rootBinding);
            }
            
            Object outcome = null;
            String action = command.getActionName();
            if ( action != null ) {
                if ( !action.startsWith("_")) {
                    outcome = resolver.invoke(binding.getBean(), command.getName(), null, null);
                } else {
                    outcome = action;
                }
                
                if ( command.isUpdate() ) {
                    binding.update();
                }
                NavigationHandler handler = ctx.getNavigationHandler();
                if ( handler != null ) {
                    handler.navigate(navPanel, command, outcome);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            ClientContext.getCurrentContext().getPlatform().showError((JComponent) command, e);
        }
    }
    
    private static void validate(UICommand command, Binding binding) {
        if ( binding == null ) return;
        if ( command.isImmediate() ) return;
        
        ActionMessage am = new ActionMessage();
        binding.validate(am);
        if ( am.hasMessages() ) {
            throw new IllegalStateException(am.toString());
        }
    }
}
