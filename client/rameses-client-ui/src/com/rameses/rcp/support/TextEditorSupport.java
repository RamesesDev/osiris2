package com.rameses.rcp.support;

import com.rameses.rcp.ui.UIInput;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;


public class TextEditorSupport implements FocusListener, KeyListener {
    
    public void focusGained(FocusEvent focusEvent) {
        if( focusEvent.getComponent() instanceof JTextComponent ) {
            JTextComponent c = (JTextComponent)focusEvent.getComponent();
            if( c!=null) {
                c.selectAll();
            }
        }
    }
    
    public void focusLost(FocusEvent focusEvent) {}    
    public void keyTyped(KeyEvent keyEvent) {}    
    public void keyPressed(KeyEvent keyEvent) {}
    
    public void keyReleased(KeyEvent keyEvent) {
        if( keyEvent.getKeyCode() == keyEvent.VK_ESCAPE) {
            JComponent jc = (JComponent)keyEvent.getComponent();
            if( jc == null || !(jc instanceof UIInput) ) return;
            UIInput ui = (UIInput) jc;
            String name = ui.getName();
        }
    }
    
}
