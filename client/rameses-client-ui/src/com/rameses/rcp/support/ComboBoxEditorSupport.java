package com.rameses.rcp.support;

import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComboBox;

public class ComboBoxEditorSupport {
    
    public static ComboBoxEditorSupport install(JComboBox component) {
        ComboBoxEditorSupport s = new ComboBoxEditorSupport(component);
        component.putClientProperty(ComboBoxEditorSupport.class, s);
        return s;
    }
    
    private JComboBox component;
    private Color origBackground;
    private Color focusBackground;
    private FocusListener focusListener;
    
    private ComboBoxEditorSupport(JComboBox component) {
        this.component = component;
        
        focusListener = new SupportFocusListener();
        component.addFocusListener(focusListener);
        
        focusBackground = ThemeUI.getColor("XTextField.focusBackground");
    }
    
    private class SupportFocusListener implements FocusListener {
        public void focusGained(FocusEvent focusEvent) {
            try {
                origBackground = component.getBackground();
                component.setBackground( focusBackground );
                
                if ( component.isEditable() && component.getEditor() != null )
                    component.getEditor().selectAll();
                
            } catch(Exception ign) {;}
        }
        
        public void focusLost(FocusEvent focusEvent) {
            try {
                if ( !ValueUtil.isEqual(component.getBackground(), origBackground) ) {
                    component.setBackground( origBackground );
                }
            } catch(Exception ign) {;}
        }
    }
}
