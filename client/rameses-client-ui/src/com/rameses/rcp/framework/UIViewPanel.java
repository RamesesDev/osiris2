package com.rameses.rcp.framework;

import com.rameses.rcp.ui.UIControl;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.Beans;
import javax.swing.JPanel;

/**
 *
 * @author jaycverg
 */
public class UIViewPanel extends JPanel implements ContainerListener {
    
    protected Binding binding;
    
    public UIViewPanel() {
        super();
        super.setLayout(new BorderLayout());
        this.binding = new Binding();
        initComponents();
    }

    public void setLayout(LayoutManager mgr) {;}
    
    private void initComponents() {
        if( !Beans.isDesignTime()) {
            addContainerListener(this);
        }
    }
    
    
    public void bindComponents( Container cont ) {
        for( Component c: cont.getComponents()) {
            if( c instanceof UIControl ) {
                UIControl uic = (UIControl)c;
                uic.setBinding(binding);
                binding.register(uic);
            } else if( c instanceof Container ) {
                bindComponents( (Container)c);
            }
        }
    }
    
    public void componentAdded(ContainerEvent e) {
        Component comp = e.getChild();
        if( comp instanceof UIControl ) {
            UIControl uic = (UIControl)comp;
            uic.setBinding(binding);
            binding.register(uic);
        } else if( comp instanceof Container ) {
            bindComponents( (Container)comp );
        }
    }
    
    public void componentRemoved(ContainerEvent e) {
        Component comp = e.getChild();
        if( comp instanceof UIControl ) {
            UIControl c = (UIControl)comp;
            c.setBinding(null);
        }
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public void refresh() {
        binding.refresh();
    }
    
}
