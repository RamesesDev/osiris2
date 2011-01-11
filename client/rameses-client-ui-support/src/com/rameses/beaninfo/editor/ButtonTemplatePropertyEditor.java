package com.rameses.beaninfo.editor;

import com.rameses.rcp.control.XButton;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyEditorSupport;

public class ButtonTemplatePropertyEditor extends PropertyEditorSupport {
    
    private BeanPropertyEditorPage panel;
    
    public ButtonTemplatePropertyEditor() {
        panel = new BeanPropertyEditorPage();
        panel.setBean(new XButton());
    }
    
    public boolean isPaintable() { return false; }    
    public void paintValue( Graphics g, Rectangle r) {}

    public Component getCustomEditor() {
        return panel;
    }
    
    public boolean supportsCustomEditor() {
        return true;
    }
    
}

