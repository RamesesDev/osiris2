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
import com.rameses.common.PropertyResolver;
import com.rameses.util.ExceptionManager;
import com.rameses.util.ValueUtil;
import java.beans.Beans;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

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
            if ( control.isReadonly() || !input.isEnabled() ) return true;
            if ( input instanceof JTextComponent && !((JTextComponent) input).isEditable() ) return true;
            if ( input.getParent() == null ) return true;
            
            if ( control instanceof Validatable ) {
                ((Validatable) control).validateInput();
            }
            
            updateBeanValue(control);
            return true;
        }
        
    }
    
    public static synchronized void updateBeanValue(UIInput control) {
        updateBeanValue(control, true, true);
    }
    
    public static synchronized void updateBeanValue(UIInput control, boolean addLog, boolean refresh) {
        try {
            Binding binding = control.getBinding();
            if ( binding == null ) return;
            
            Object bean = binding.getBean();
            if ( bean == null ) return;
            
            ClientContext ctx = ClientContext.getCurrentContext();
            PropertyResolver resolver = ctx.getPropertyResolver();
            String name = control.getName();
            if ( ValueUtil.isEmpty(name) ) return;
            
            Object inputValue = control.getValue();
            Object beanValue = resolver.getProperty(bean, name);
            boolean forceUpdate = false;
            if ( control instanceof JComponent ) {
                //if the input is a JTable check for the flag
                Object value = ((JComponent) control).getClientProperty(JTable.class);
                forceUpdate = (value != null);
            }
            
            if ( forceUpdate || !ValueUtil.isEqual(inputValue, beanValue) ) {
                resolver.setProperty(bean, name, inputValue);
                if ( addLog ) {
                    binding.getChangeLog().addEntry(bean, name, beanValue, inputValue);
                }
                if ( refresh && control instanceof JTextComponent ) {
                    control.refresh();
                }
                
                binding.notifyDepends(control);
            }
            
        } catch(Exception e) {
            Exception src = ExceptionManager.getOriginal(e);
            if ( !ExceptionManager.getInstance().handleError(src) ) {
                ClientContext.getCurrentContext().getPlatform().showError((JComponent) control, e);
            }
        }
    }
    
}
