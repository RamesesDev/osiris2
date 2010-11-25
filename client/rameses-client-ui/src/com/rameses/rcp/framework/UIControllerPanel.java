package com.rameses.rcp.framework;

import com.rameses.platform.interfaces.SubWindow;
import com.rameses.platform.interfaces.ViewContext;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.Stack;
import javax.swing.JButton;
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
    
    private SubWindow parent;
    
    
    public UIControllerPanel() {
        super.setLayout(new BorderLayout());
        setName("root");
        
        //attach the default button when this panel is already
        //attached to its rootpane
        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                if ( getDefaultButton() != null && !defaultBtnAdded ) {
                    attachDefaultButton();
                }
            }
            
            public void ancestorMoved(AncestorEvent event) {}
            public void ancestorRemoved(AncestorEvent event) {}
        });
    }
    
    //visible in the package
    void attachDefaultButton() {
        JRootPane rp = getRootPane();
        JButton btn = getDefaultButton();
        if ( btn != null && rp != null && rp.getDefaultButton() != btn ) {
            rp.setDefaultButton( btn );
            defaultBtnAdded = true;
        } else {
            defaultBtnAdded = false;
        }
    }
    
    private JButton getDefaultButton() {
        UIControllerContext current = getCurrentController();
        if ( current == null ) return null;
        if ( current.getCurrentView() == null ) return null;
        
        return current.getCurrentView().getBinding().getDefaultButton();
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
            binding.setViewContext(this);
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
    
    public void setSubWindow(SubWindow subWindow) {
        this.parent = subWindow;
    }
    
    public SubWindow getSubWindow() {
        return parent;
    }
    
}
