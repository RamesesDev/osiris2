package com.rameses.rcp.support;

import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;


public class TextEditorSupport 
{
    public static TextEditorSupport install(JTextComponent component)
    {
        TextEditorSupport s = new TextEditorSupport(component); 
        component.putClientProperty(TextEditorSupport.class, s); 
        return s; 
    } 
    
    private JTextComponent component;
    private FocusListener focusListener; 
    
    private TextEditorSupport(JTextComponent component) 
    {
        this.component = component; 
        
        focusListener = new SupportFocusListener(); 
        component.addFocusListener(focusListener); 
        
        Insets margin = UIManager.getInsets("TextField.margin");
        if (margin != null) 
        {
            Insets ins = new Insets(margin.top, margin.left, margin.bottom, margin.right);
            component.setMargin(ins);
        } 
    }
    
    private class SupportFocusListener implements FocusListener  
    {
        public void focusGained(FocusEvent focusEvent) 
        {
            try {
                component.selectAll();
            } catch(Exception ign) {;} 
        } 

        public void focusLost(FocusEvent focusEvent) {}    
    }    
}
