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
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.support.ResourceUtil;
import java.awt.Component;
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
        UIController uc = cp.getController( inv.getWorkunitid() );
        String action = inv.getAction();
        if(action!=null) {
            String out = (String)uc.init(new HashMap(), action);
            if ( out != null ) 
                uc.setCurrentView(out);
        } 
        
        UIControllerPanel p = new UIControllerPanel(uc);
        //p.display();
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
}
