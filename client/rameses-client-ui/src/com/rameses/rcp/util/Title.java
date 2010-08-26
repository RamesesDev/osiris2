package com.rameses.rcp.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Title extends JPanel
{
    private Logger logger;
    private JLabel label;
    private Color background1;
    private Color background2;
    
    public Title()
    {
        logger = Logger.getLogger(getClass().getName());
        initComponent();
    }

    private void initComponent()
    {        
        background1 = Color.decode("#D3E0F7");
        background2 = new Color(158,184,221);
        label = new JLabel("Title");
        super.setLayout(new CustomLayout());
        add(label);
        
        label.setBorder(BorderFactory.createEmptyBorder(2,7,2,5));
        setForeground(Color.decode("#022D7B"));
        
        Font f = UIManager.getFont("Label.font");
        setFont(new Font(f.getFontName(), Font.BOLD, 11));
        
        //override styles
        try
        {
            Object o = UIManager.get("Title.font");
            if (o != null && (o instanceof Font)) setFont((Font) o);

            o = UIManager.get("Title.background1");
            if (o != null && (o instanceof Color)) setBackground1((Color) o);
            
            o = UIManager.get("Title.background2");
            if (o != null && (o instanceof Color)) setBackground2((Color) o);

            o = UIManager.get("Title.foreground");
            if (o != null && (o instanceof Color)) setForeground((Color) o);
        }
        catch(Exception ign) {;}
    }

    public void setLayout(LayoutManager mgr) {;}

    public Color getBackground1() { return background1; }
    public void setBackground1(Color color1) { this.background1 = color1; }

    public Color getBackground2() { return background2; }
    public void setBackground2(Color color2) { this.background2 = color2; }
    
    public String getText() { return label.getText(); }
    public void setText(String text) { label.setText((text != null ? text : "")); }
    
    public void setFont(Font font)
    {
        super.setFont(font);
        if (label != null) label.setFont(font);
    }

    public void setForeground(Color fg) 
    {
        super.setForeground(fg);
        if (label != null) label.setForeground(fg);
    }

    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Insets ins = getInsets();
        int w = getWidth();
        int h = getHeight();
        Color c1 = getBackground1();
        Color c2 = getBackground2();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
        g2.setPaint(new GradientPaint(0,0,c1,0,h,c2,true));
        g2.fillRect(0,0,w,h);
    }
    
    //<editor-fold defaultstate="collapsed" desc=" CustomLayout (Class) ">
    private class CustomLayout implements LayoutManager
    {
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
                if (label != null && label.isVisible())
                {
                    Dimension dim = label.getPreferredSize();
                    label.setBounds(x, y, w, h);
                }
            }
        }
        
        Dimension getLayoutSize(Container parent)
        {
            synchronized(parent.getTreeLock())
            {
                int width = 0;
                int height = 0;
                if (label != null && label.isVisible())
                {
                    Dimension dim = label.getPreferredSize();
                    width = dim.width;
                    height = dim.height;
                }
                
                Insets margin = parent.getInsets();
                width += (margin.left + margin.right);
                height += (margin.top + margin.bottom);
                return new Dimension(width, height);
            }
        }
    }
    //</editor-fold>    



}
