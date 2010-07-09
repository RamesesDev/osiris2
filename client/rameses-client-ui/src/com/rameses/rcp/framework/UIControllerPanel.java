package com.rameses.rcp.framework;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author jaycverg
 */
public class UIControllerPanel extends JPanel implements NavigatablePanel {
    
    private Stack<UIController> controllers = new ControllerStack();
    
    
    public UIControllerPanel() {
        super.setLayout(new BorderLayout());
    }
    
    public UIControllerPanel(UIController controller) {
        this();
        controllers.push(controller);
        _build();
    }
    
    private void _build() {
        UIViewPanel p = getCurrentController().getCurrentView();
        removeAll();
        SwingUtilities.updateComponentTreeUI(this);
        add( p );
        p.refresh();
        
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    public Stack<UIController> getControllers() {
        return controllers;
    }
    
    public UIController getCurrentController() {
        if ( !controllers.empty() ) {
            return controllers.peek();
        }
        throw new IllegalStateException("No controller found.");
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
