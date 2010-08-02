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
import com.rameses.util.ExpressionResolver;
import com.rameses.util.PropertyResolver;
import com.rameses.util.ValueUtil;
import java.awt.Container;
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
}
