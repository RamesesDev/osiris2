package com.rameses.rcp.util;

import com.rameses.rcp.common.ValidatorEvent;
import com.rameses.rcp.control.XButton;
import com.rameses.rcp.framework.*;
import com.rameses.rcp.ui.UICommand;
import com.rameses.common.MethodResolver;
import com.rameses.rcp.common.Action;
import com.rameses.util.BusinessException;
import com.rameses.util.ExceptionManager;
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

            binding.formCommit();
            validate(command, binding);
            
            String target = ValueUtil.isEmpty(command.getTarget())? "parent": command.getTarget();
            NavigatablePanel navPanel = UIControlUtil.getParentPanel((JComponent)command, target);
            if ( !"parent".equals(target) ) {
                UIControllerContext rootCon = (UIControllerContext) navPanel.getControllers().peek();
                Binding rootBinding = rootCon.getCurrentView().getBinding();
                validate(command, rootBinding);
            }
            
            //set parameters
            XButton btn = (XButton) command;
            ControlSupport.setProperties( binding.getBean(), btn.getParams());
            
            Object outcome = null;
            String action = command.getActionName();
            if ( btn.getClientProperty(Action.class.getName()) != null ) {
                Action a = (Action) btn.getClientProperty(Action.class.getName());
                outcome = a.execute();
                
            } else if ( action != null ) {
                if ( !action.startsWith("_")) {
                    outcome = resolver.invoke(binding.getBean(), action, null, null);
                } else {
                    outcome = action;
                }
                
                if ( command.isUpdate() ) {
                    binding.update();
                }
            }
            NavigationHandler handler = ctx.getNavigationHandler();
            if ( handler != null ) {
                handler.navigate(navPanel, command, outcome);
            }
        } catch(Exception ex) {
            Exception e = ExceptionManager.getOriginal(ex);
            
            if ( !ExceptionManager.getInstance().handleError(e) ) {
                ClientContext.getCurrentContext().getPlatform().showError((JComponent) command, ex);
            }
        }
    }
    
    private static void validate(UICommand command, Binding binding) throws BusinessException {
        if ( binding == null ) return;
        if ( !command.isUpdate() && command.isImmediate() ) return;
        
        ActionMessage am = new ActionMessage();
        binding.validate(am);
        if ( am.hasMessages() ) {
            if ( am.getSource() != null ) am.getSource().requestFocus();
            throw new BusinessException(am.toString());
        }
        
        ValidatorEvent evt = new ValidatorEvent(binding);
        binding.validateBean(evt);
        if ( evt.hasMessages() ) {
            if ( evt.getSource() != null ) evt.getSource().requestFocus();
            throw new BusinessException(evt.toString());
        }
    }
}
