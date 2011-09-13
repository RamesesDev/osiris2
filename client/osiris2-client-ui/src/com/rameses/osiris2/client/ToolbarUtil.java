/*
 * ToolbarUtil.java
 *
 * Created on June 12, 2010, 1:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.support.ResourceUtil;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 *
 * @author ms
 */
public final class ToolbarUtil {
    
    
    public static JToolBar getToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new ToolBarLayout());
        SessionContext app = OsirisContext.getSession();
        
        List<Invoker> invokers = app.getInvokers("toolbar");
        for(Invoker inv : invokers) {
            boolean isButton = true;
            try {
                String sButton = (String)inv.getProperties().get("button");
                if(sButton !=null ) isButton = Boolean.parseBoolean( sButton );
            } catch(Exception ign){;}
            if(isButton)
                toolbar.add(new InvokerAction(inv));
            else
                toolbar.add(getViewComponent(inv));
        }
        return toolbar;
    }
    
    public static Component getViewComponent(Invoker inv) {
        ControllerProvider cp = ClientContext.getCurrentContext().getControllerProvider();
        UIController c = cp.getController( inv.getWorkunitid(), null );
        String action = inv.getAction();
        UIControllerContext uic = new UIControllerContext( c );
        if(action!=null) {
            String out = (String)c.init(new HashMap(), action);
            if ( !ValueUtil.isEmpty(out) ) {
                uic.setCurrentView(out);
            }
        }
        
        UIControllerPanel p = new UIControllerPanel(uic);
        return p;
    }
    
    
    private static class InvokerAction extends JButton implements ActionListener {
        private Invoker invoker;
        public InvokerAction(Invoker inv) {
            this.invoker = inv;
            this.setText(inv.getCaption());
            this.addActionListener(this);
            try {
                String tooltip = (String)inv.getProperties().get("tooltip");
                if(tooltip!=null) this.setToolTipText(tooltip);
                String icn = (String)inv.getProperties().get("icon");
                if(icn!=null) this.setIcon( ResourceUtil.getImageIcon( icn ) );
            } catch(Exception e) {
                //do nothing
            }
        }
        
        public void actionPerformed(ActionEvent e) {
            try {
                InvokerUtil.invoke(invoker, null);
            } catch(Exception ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
        }
        
    }
    
    private static class ToolBarLayout implements LayoutManager {
        
        private static final int SPACING = 2;
        
        public void addLayoutComponent(String name, Component comp) {;}
        public void removeLayoutComponent(Component comp) {;}
        
        public Dimension getLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                int w=0, h=0;
                
                Component[] comps = parent.getComponents();
                for (int i=0; i<comps.length; i++) {
                    if (!comps[i].isVisible()) continue;
                    
                    Dimension dim = comps[i].getPreferredSize();
                    w += (dim.width + SPACING);
                    h = Math.max(h, dim.height);
                }
                return new Dimension(w,h);
            }
        }
        
        public Dimension preferredLayoutSize(Container parent) {
            return getLayoutSize(parent);
        }
        
        public Dimension minimumLayoutSize(Container parent) {
            return getLayoutSize(parent);
        }
        
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets margin = new Insets(2,2,2,2);
                
                int x = margin.left;
                int y = margin.top;
                int w = parent.getWidth() - (margin.left + margin.right);
                int h = parent.getHeight() - (margin.top + margin.bottom);
                
                Component[] comps = parent.getComponents();
                for (int i=0; i<comps.length; i++) {
                    Component comp = comps[i];
                    
                    if (!comp.isVisible()) continue;
                    
                    Dimension dim = comp.getPreferredSize();
                    comp.setBounds(x, y, dim.width, h);
                    x += dim.width + SPACING;
                }
            }
        }
    }
    
}
