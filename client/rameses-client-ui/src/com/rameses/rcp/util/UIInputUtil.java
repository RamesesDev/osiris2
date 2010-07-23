/*
 * UIInputUtil.java
 *
 * Created on June 21, 2010, 3:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.util;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.util.MethodResolver;
import com.rameses.util.PropertyResolver;
import com.rameses.util.ValueUtil;
import java.beans.Beans;
import javax.swing.InputVerifier;
import javax.swing.JComponent;

/**
 *
 * @author jaycverg
 */
public class UIInputUtil {
    
    public static UIInputVerifier VERIFIER = new UIInputVerifier();
    
    public static class UIInputVerifier extends InputVerifier {
        
        public boolean verify(JComponent input) {
            if ( Beans.isDesignTime() ) return true;
            
            if ( !(input instanceof UIInput) )
                throw new IllegalStateException("UIInputVerifier should be used for UIInput controls only.");
            
            UIInput control = (UIInput) input;
            if ( control instanceof Validatable ) {
                ((Validatable) control).validateInput();
            }
            
            updateBeanValue(control);
            return true;
        }
        
    }
    
    public static synchronized void updateBeanValue(UIInput control) {
        updateBeanValue(control, true);
    }
    
    public static synchronized void updateBeanValue(UIInput control, boolean addLog) {
        try {
            Binding binding = control.getBinding();
            if ( binding == null ) return;
            
            Object bean = binding.getBean();
            if ( bean == null ) return;
            
            ClientContext ctx = ClientContext.getCurrentContext();
            PropertyResolver resolver = ctx.getPropertyResolver();
            String name = control.getName();
            Object inputValue = control.getValue();
            Object beanValue = resolver.getProperty(bean, name);
            resolver.setProperty(bean, name, inputValue);
            
            if ( addLog ) {
                binding.getChangeLog().addEntry(bean, name, beanValue, inputValue);
            }
            
            String method = control.getOnAfterUpdate();
            if ( !ValueUtil.isEmpty(method) ) {
                MethodResolver mr = ctx.getMethodResolver();
                mr.invoke(bean, method, null, null);
            }
            
            binding.notifyDepends(control);
            
        } catch(Exception e) {
            throw new IllegalStateException("UIInputUtil.updateBeanValue: ", e);
        }
    }
    
}
