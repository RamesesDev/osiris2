package com.rameses.rcp.framework;

import com.rameses.platform.interfaces.ViewContext;
import com.rameses.rcp.control.XButton;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.Stack;
import javax.swing.JComponent;
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
    
    private Stack controllers = new ControllerStack();
    private boolean defaultBtnAdded;
    private XButton defaultBtn;
    
    public UIControllerPanel() {
        super.setLayout(new BorderLayout());
        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                if ( defaultBtn != null && !defaultBtnAdded ) {
                    JComponent comp = event.getComponent();
                    JRootPane rp = comp.getRootPane();
                    if ( rp != null ) {
                        rp.setDefaultButton(defaultBtn);
                        defaultBtnAdded = true;
                    }
                }
            }
            public void ancestorMoved(AncestorEvent event) {}
            public void ancestorRemoved(AncestorEvent event) {}
        });
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
            defaultBtn = p.getBinding().getDefaultButton();
            if ( defaultBtn != null ) defaultBtnAdded = false;
            
            add( p );
            p.refresh();
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
    
    
    //<editor-fold defaultstate="collapsed" desc="  ControllerStack (class)  ">
    public class ControllerStack extends Stack {
        
        public Object push(Object item) {
            UIController con = (UIController) item;
            con.initialize();
            return super.push(con);
        }
        
    }
    //</editor-fold>
    
}
