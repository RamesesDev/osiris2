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
    
    private Stack controllers = new Stack();
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
    
    public UIControllerPanel(UIController controller) {
        this();
        controllers.push(controller);
        _build();
    }
    
    private void _build() {
        UIController current = getCurrentController();
        removeAll();
        if ( current != null ) {
            UIViewPanel p = current.getCurrentView();
            Binding binding = p.getBinding();
            defaultBtn = binding.getDefaultButton();
            if ( defaultBtn != null ) {
                defaultBtnAdded = false;
                //try to attach the default button
                attachDefaultButton();
            }
            
            add( p );
            p.refresh();
            binding.display();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    public Stack getControllers() {
        return controllers;
    }
    
    public void setControllers(Stack controllers) {
        this.controllers = controllers;
        _build();
    }
    
    public UIController getCurrentController() {
        if ( !controllers.empty() ) {
            return (UIController) controllers.peek();
        }
        return null;
    }
    
    public void renderView() {
        _build();
    }
    
    public boolean close() {
        return getCurrentController().getCurrentView().getBinding().close();
    }
    
    public void display() {
        UIController current = getCurrentController();
        if ( current != null ) {
            UIViewPanel p = current.getCurrentView();
            p.getBinding().display();
        }
    }
        
}
