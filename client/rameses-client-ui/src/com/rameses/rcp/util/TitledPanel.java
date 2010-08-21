package com.rameses.rcp.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;

public class TitledPanel extends JPanel 
{
    private Header header;
    private Color borderColor;
    private DropShadowBorder shadowBorder;
    
    public TitledPanel()
    {
        super.setLayout(new CustomLayout());
        
        header = new Header();
        borderColor = Color.decode("#ADC1DC");
        shadowBorder = new DropShadowBorder();
        setPreferredSize(new Dimension(100,50));
        setBorder(shadowBorder);
    }

    public void setLayout(LayoutManager mgr) {;}

    public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color borderColor) { this.borderColor = borderColor; }

    public String getTitle() { return header.getText(); }
    public void setTitle(String title) {
        header.setText((title != null ? title : ""));
    }
    
    public Font getTitleFont() { return header.getFont(); }
    public void setTitleFont(Font font) { header.setFont(font); }
    
    public Color getHeaderBackground() { return header.getBackground(); }
    public void setHeaderBackground(Color background) { header.setBackground(background); }
    
    public Component add(Component comp) 
    {
        if (getComponentCount() < 2) 
        {
            ensureHeaderIsAdded();
            setOpaque(comp, true);
            comp.setBackground(Color.WHITE);
            return super.add(comp);
        }
        else
            return comp;
    }

    public Component add(Component comp, int index) 
    {
        if (getComponentCount() < 2)
        {
            ensureHeaderIsAdded();
            setOpaque(comp, true);
            comp.setBackground(Color.WHITE);
            return super.add(comp, index);
        }
        else
            return comp;
    }
    
    private void ensureHeaderIsAdded()
    {
        if (header.getParent() == null) super.add(header);
    }
    
    private void setOpaque(Component comp, boolean opaque)
    {
        try
        {
            Method m = comp.getClass().getMethod("setOpaque", new Class[]{boolean.class});
            m.invoke(comp, new Object[]{opaque});
        }
        catch(Exception ign) {;}
    }
    

    //<editor-fold defaultstate="collapsed" desc=" Header (Class) ">
    private class Header extends Title
    {
        Header() 
        {
            setBackground(Color.decode("#BFD4F2"));
            setForeground(Color.decode("#022D7B"));
            setText("Titled Panel");
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" CustomLayout (Class) ">
    private class CustomLayout implements LayoutManager
    {
        private Logger logger = Logger.getLogger(getClass().getName());
        
        public void addLayoutComponent(String name, Component comp) {;}
        public void removeLayoutComponent(Component comp) {;}

        public Dimension preferredLayoutSize(Container parent) {
            return getLayoutSize(parent);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return getLayoutSize(parent);
        }

        public void layoutContainer(Container parent) 
        {
            synchronized(parent.getTreeLock())
            {
                Insets margin = parent.getInsets();
                int x = margin.left;
                int y = margin.top;
                int w = parent.getWidth() - (margin.left + margin.right);
                int h = parent.getHeight() - (margin.top + margin.bottom);
                Component header = findHeader(parent.getComponents());
                if (header != null && header.isVisible())
                {
                    Dimension dim = header.getPreferredSize();
                    header.setBounds(x, y, w, dim.height);
                    y += dim.height;
                    h -= dim.height;
                }
                
                Component body = findBody(parent.getComponents());
                if (body != null && body.isVisible())
                {
                    body.setBounds(x, y, w, h);
                }
            }
        }
        
        Dimension getLayoutSize(Container parent)
        {
            synchronized(parent.getTreeLock())
            {
                Component[] comps = parent.getComponents();
                int width = 0;
                int height = 0;
                Component header = findHeader(comps);
                if (header != null && header.isVisible())
                {
                    Dimension dim = header.getPreferredSize();
                    width = dim.width;
                    height = dim.height;
                }
                
                Component body = findBody(comps);
                if (body != null && body.isVisible())
                {
                    Dimension dim = body.getPreferredSize();
                    height += dim.height;
                }
                
                Insets margin = parent.getInsets();
                width += (margin.left + margin.right);
                height += (margin.top + margin.bottom);
                return new Dimension(width, height);
            }
        }
        
        Component findHeader(Component[] comps)
        {
            for (int i=0; i<comps.length; i++)
            {
                if (comps[i] instanceof Header) return comps[i];
            }
            return null;
        }
        
        Component findBody(Component[] comps)
        {
            for (int i=0; i<comps.length; i++)
            {
                if (!(comps[i] instanceof Header)) return comps[i];
            }
            return null;
        }        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" DropShadowBorder (Class) ">
    private class DropShadowBorder extends AbstractBorder
    {
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, new Insets(0,0,0,0));
        }
        
        public Insets getBorderInsets(Component c, Insets insets) 
        {
            insets.top = 1;
            insets.left = 1;
            insets.bottom = 3;
            insets.right = 3;
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) 
        {
            Color shadow = UIManager.getColor("controlShadow");
            Color oldColor = g.getColor();
            Color borderColor = getBorderColor();
            if (borderColor != null)
            {
                g.setColor(borderColor);
                g.drawRect(0, 0, w-3, h-3);
            }
            
            int opacity = 40;
            for (int i=1; i<=3; i++)
            {
                g.setColor(createOpacity(shadow, opacity));
                g.drawLine(5-i, h-i, w-i, h-i);
                opacity += 50;
            }
            opacity = 40;
            for (int i=1; i<=3; i++)
            {
                g.setColor(createOpacity(shadow, opacity));
                g.drawLine(w-i, 5-i, w-i, h-i);
                opacity += 50;
            }            
            g.setColor(oldColor);
        }
        
        private Color createOpacity(Color color, int opacity) {
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
        }        
    }
    //</editor-fold>    
}
