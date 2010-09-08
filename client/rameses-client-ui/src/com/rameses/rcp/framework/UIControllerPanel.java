package com.rameses.rcp.framework;

import com.rameses.platform.interfaces.ViewContext;
import com.rameses.rcp.control.XButton;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author jaycverg
 */
public class UIControllerPanel extends JPanel implements NavigatablePanel, ViewContext {
    
    private Stack<UIControllerContext> controllers = new Stack();
    private boolean defaultBtnAdded;
    private XButton defaultBtn;
    
    public UIControllerPanel() {
        super.setLayout(new BorderLayout());
        
        //attach the default button when this panel is already
        //attached to its rootpane
        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                if ( defaultBtn != null && !defaultBtnAdded ) {
                    attachDefaultButton();
                }
            }
            
            public void ancestorMoved(AncestorEvent event) {}
            public void ancestorRemoved(AncestorEvent event) {}
        });
    }
    
    private void attachDefaultButton() {
        JRootPane rp = getRootPane();
        if ( rp != null ) {
            rp.setDefaultButton(defaultBtn);
            defaultBtnAdded = true;
        }
    }
    
    public UIControllerPanel(UIControllerContext controller) {
        this();
        controllers.push(controller);
        _build();
    }
    
    private void _build() {
        UIControllerContext current = getCurrentController();
        removeAll();
        if ( current != null ) {
            UIViewPanel p = current.getCurrentView();
            Binding binding = p.getBinding();
            binding.getProperties().put(UIViewPanel.class, p);
            defaultBtn = binding.getDefaultButton();
            if ( defaultBtn != null ) {
                defaultBtnAdded = false;
                //try to attach the default button
                attachDefaultButton();
            }
            
            add( p );
            p.refresh();
            binding.focusFirstInput();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    public Stack<UIControllerContext> getControllers() {
        return controllers;
    }
    
    public void setControllers(Stack<UIControllerContext> controllers) {
        this.controllers = controllers;
        _build();
    }
    
    public UIControllerContext getCurrentController() {
        if ( !controllers.empty() ) {
            return (UIControllerContext) controllers.peek();
        }
        return null;
    }
    
    public void renderView() {
        _build();
    }
    
    public boolean close() {
        try {
            return getCurrentController().getCurrentView().getBinding().close();
        } catch(Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    
    public void display() {
        UIControllerContext current = getCurrentController();
        if ( current != null ) {
            UIViewPanel p = current.getCurrentView();
            p.getBinding().focusFirstInput();
        }
    }
    
}
