/*
 * UIControlUtil.java
 *
 * Created on July 8, 2010, 9:40 AM
 * @author jaycverg
 */

package com.rameses.rcp.util;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.ui.UIControl;
import com.rameses.common.ExpressionResolver;
import com.rameses.common.PropertyResolver;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.Container;
import java.util.List;
import javax.swing.JComponent;


public class UIControlUtil {
    
    public static Object getBeanValue(UIControl control) {
        return getBeanValue(control, control.getName());
    }
    
    public static  Object getBeanValue(UIControl control, String property) {
        if ( ValueUtil.isEmpty(property) ) return null;
        
        PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
        Object bean = control.getBinding().getBean();
        return resolver.getProperty(bean, property);
    }
    
    public static Class getValueType(UIControl control, String property) {
        PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
        Object bean = control.getBinding().getBean();
        return resolver.getPropertyType(bean, property);
    }
    
    public static Object evaluateExpr(Object bean, String expression) {
        ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
        return er.evaluate(bean, expression);
    }
    
    public static int compare(UIControl control, Object control2) {
        if ( control2 == null || !(control2 instanceof UIControl)) return 0;
        return control.getIndex() - ((UIControl) control2).getIndex();
    }
    
    public static NavigatablePanel getParentPanel(JComponent comp, String target) {
        NavigatablePanel panel = null;
        if ( panel == null ) {
            Container parent = comp.getParent();
            while( parent != null ) {
                if ( parent instanceof NavigatablePanel ) {
                    panel = (NavigatablePanel) parent;
                }
                if ( (panel != null && "parent".equals(target)) || (parent instanceof UIControllerPanel && "root".equals(parent.getName())) ) {
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
    
    public static void validate(List<Validatable> validatables, ActionMessage actionMessage) {
        for ( Validatable vc: validatables ) {
            validate(vc, actionMessage);
        }
    }
    
    public static void validate(Validatable vc, ActionMessage actionMessage) {
        Component comp = (Component) vc;
        if ( !comp.isFocusable() || !comp.isEnabled() || !comp.isShowing() || comp.getParent() == null ) {
            //do not validate non-focusable, disabled, or hidden fields.
            return;
        }
        
        if ( vc instanceof UIInput ) {
            //do not validate readonly fields
            if ( ((UIInput)vc).isReadonly() ) return;
        }
        
        vc.validateInput();
        ActionMessage ac = vc.getActionMessage();
        if ( ac != null && ac.hasMessages() ) {
            if ( ValueUtil.isEmpty(actionMessage.getSource()) )
                actionMessage.setSource( comp );
            
            actionMessage.addMessage(ac);
        }
    }
    
}
