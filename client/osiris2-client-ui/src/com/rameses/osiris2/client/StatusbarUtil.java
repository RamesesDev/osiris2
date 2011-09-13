package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;

public final class StatusbarUtil 
{
    public static JComponent getStatusbar() 
    {
        SessionContext app = OsirisContext.getSession();
        List<Invoker> invokers = app.getInvokers("statusbar");
        StatusPanel panel = new StatusPanel(); 
        
        for (Invoker inv : invokers) 
        {
            JComponent viewcomp = (JComponent) getViewComponent(inv);   
            
            int preferredWidth = 0; 
            try {
                preferredWidth = Integer.parseInt(inv.getProperties().get("width").toString());
            } catch(Exception ign){;}
            
            viewcomp.putClientProperty(StatusConstraint.class, new StatusConstraint(preferredWidth)); 
            panel.add(viewcomp); 
        } 
        return panel; 
    } 
    
    public static Component getViewComponent(Invoker inv) 
    {
        ControllerProvider cp = ClientContext.getCurrentContext().getControllerProvider();
        UIController c = cp.getController(inv.getWorkunitid(), null);
        UIControllerContext uic = new UIControllerContext(c);
        
        String action = inv.getAction();
        if (action != null) 
        {
            String out = (String) c.init(new HashMap(), action);
            if (!ValueUtil.isEmpty(out)) uic.setCurrentView(out);
        } 
        
        return new UIControllerPanel(uic);
    }
       
    //<editor-fold defaultstate="collapsed" desc=" StatusPanel (Class) ">
    private static class StatusPanel extends JPanel 
    {
        public StatusPanel() { 
            setLayout(new Layout());
        } 

        /*
        public void add(String text, Icon icon, int width)
        {
            JLabel lbl = new JLabel(text);
            lbl.putClientProperty(StatusConstraint.class, new StatusConstraint(text, icon, width));
            lbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(2,3,2,3))); 
            add(lbl); 
        }
        */
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" StatusConstraint (Class) ">
    private static class StatusConstraint
    {
        //private String text;
        //private Icon icon;
        private int width;

        StatusConstraint(int width)
        {
            this.width = width; 
            //this.text = text; 
            //this.icon = icon; 
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" Layout (Class) ">
    private static class Layout implements LayoutManager
    {
        public void addLayoutComponent(String name, Component comp) {}
        public void removeLayoutComponent(Component comp) {}

        public Dimension preferredLayoutSize(Container parent) {
            return getLayoutSize(parent);
        }

        public Dimension minimumLayoutSize(Container parent) 
        {
            Dimension dim = getLayoutSize(parent);
            return new Dimension(100, dim.height); 
        }

        public void layoutContainer(Container parent) 
        {
            synchronized (parent.getTreeLock())
            {
                Insets margin = parent.getInsets();
                int x = margin.left;
                int y = margin.top;
                int w = parent.getWidth() - (margin.left + margin.right);
                int h = parent.getHeight() - (margin.top + margin.bottom);    

                List list = new ArrayList(); 
                Component[] comps = parent.getComponents(); 
                for (int i=comps.length-1; i>=0; i--)
                {
                    if (!(comps[i] instanceof JComponent)) continue;

                    JComponent jc = (JComponent) comps[i]; 
                    Dimension dim = jc.getPreferredSize();
                    StatusConstraint cons = (StatusConstraint) jc.getClientProperty(StatusConstraint.class); 
                    if (cons == null) continue;

                    int dw = dim.width;
                    if (cons.width > 0) dw = cons.width; 

                    int cw = w-dw;
                    if (cw < 0) 
                    {
                        cw = 0;
                        dw = w;
                    }

                    if (i-1 < 0)
                    {
                        cw = 0;
                        dw = w;
                    }

                    list.add(new Object[] {jc, new Rectangle(cw, y, dw, h)});
                    w = cw;                    
                }

                Iterator itr = list.iterator();
                while (itr.hasNext())
                {
                    Object[] arr = (Object[]) itr.next(); 
                    JComponent jc = (JComponent) arr[0];
                    Rectangle rect = (Rectangle) arr[1];
                    jc.setBounds(rect); 
                }
            }
        }

        public Dimension getLayoutSize(Container parent) 
        {
            synchronized (parent.getTreeLock())
            {
                int w=0, h=0;           

                Component[] comps = parent.getComponents(); 
                for (int i=0; i<comps.length; i++)
                {
                    if (comps[i] instanceof JComponent)
                    {
                        JComponent jc = (JComponent) comps[i]; 
                        Dimension dim = jc.getPreferredSize();
                        StatusConstraint cons = (StatusConstraint) jc.getClientProperty(StatusConstraint.class);
                        if (cons == null) continue;

                        if (cons.width > 0)
                            w += cons.width;
                        else
                            w += dim.width;

                        h = Math.max(h, dim.height); 
                    }
                }

                Insets margin = parent.getInsets();
                w += (margin.left + margin.right);
                h += (margin.top + margin.bottom);
                return new Dimension(w, h);
            }
        }
    }
    //</editor-fold>                
}
