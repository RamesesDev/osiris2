/*
 * UIControlUtil.java
 *
 * Created on July 8, 2010, 9:40 AM
 * @author jaycverg
 */

package com.rameses.rcp.util;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UIControl;
import com.rameses.util.PropertyResolver;


public class UIControlUtil {
    
    public static synchronized Object getBeanValue(UIControl control) {
        return getBeanValue(control, control.getName());
    }
    
    public static synchronized  Object getBeanValue(UIControl control, String property) {
        PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
        Object bean = control.getBinding().getBean();
        return resolver.getProperty(bean, property);
    }
    
    public static synchronized Class getValueType(UIControl control, String property) {
        PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
        Object bean = control.getBinding().getBean();
        return resolver.getPropertyType(bean, property);
    }
}
