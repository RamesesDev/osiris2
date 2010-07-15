package com.rameses.rcp.framework;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author jaycverg
 */
public class UIControllerPanel extends JPanel implements NavigatablePanel {
    
    private Stack controllers = new ControllerStack();
    
    
    public UIControllerPanel() {
        super.setLayout(new BorderLayout());
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
