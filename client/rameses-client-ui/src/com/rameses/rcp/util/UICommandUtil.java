package com.rameses.rcp.util;

import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.*;
import com.rameses.rcp.ui.UICommand;
import com.rameses.util.MethodResolver;
import com.rameses.util.ValueUtil;
import java.awt.Container;
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
            NavigatablePanel navPanel = getParentPanel(command, target);
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
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private static NavigatablePanel getParentPanel(UICommand command, String target) {
        JComponent comp = (JComponent) command;
        NavigatablePanel panel = null; //(NavigatablePanel) comp.getClientProperty(NavigatablePanel.class);
        if ( panel == null ) {
            Container parent = comp.getParent();
            while( parent != null ) {
                if ( parent instanceof NavigatablePanel ) {
                    panel = (NavigatablePanel) parent;
                }
                if ( (panel != null && "parent".equals(target)) || parent instanceof UIControllerPanel ) {
                    break;
                }
                parent = parent.getParent();
            }
            if ( panel != null ) {
                comp.putClientProperty(NavigatablePanel.class, panel);
            }
        }
        return panel;
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
    //</editor-fold>
}
