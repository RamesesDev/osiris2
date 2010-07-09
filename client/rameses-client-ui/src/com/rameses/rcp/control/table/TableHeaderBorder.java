/*
 * TableHeaderBorder.java
 *
 * Created on February 6, 2010, 10:47 AM
 * @author jaycverg
 */

package com.rameses.rcp.control.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;


public class TableHeaderBorder extends AbstractBorder {
    private Insets margin = new Insets(0,0,0,0);
    
    public TableHeaderBorder() {;}
    
    public TableHeaderBorder(Insets margin) {
        if (margin != null) this.margin = margin;
    }
    
    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(0,0,0,0));
    }
    
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = margin.top;
        insets.left = margin.left;
        insets.bottom = margin.bottom;
        insets.right = margin.right;
        return insets;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        Color shadow = UIManager.getColor("controlDkShadow");
        Color oldColor = g.getColor();
        
        g.setColor(brighter(shadow,20));
        g.drawRect(0,0,w-1,h-1);
        g.setColor(brighter(shadow,40));
        g.drawLine(1,h-2,w-2,h-2);
        g.drawLine(w-2,1,w-2,h-2);
        
        g.setColor(brighter(shadow,55));
        g.drawRect(0,0,w,h);
        g.setColor(oldColor);
    }
    
    private Color brighter(Color c, int value) {
        if (value < 0) return c;
        
        float[] hsb = Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),new float[3]);
        int h = (int) (hsb[0] * 360);
        int s = (int) (hsb[1] * 100);
        int b = (int) (hsb[2] * 100);
        
        int rm = 0;
        b += value;
        if (b > 100) {
            rm = b - 100;
            b = 100;
        }
        s -= rm;
        if (s < 0) s = 0;
        
        int rgb = Color.HSBtoRGB(h/360.0f, s/100.0f, b/100.0f);
        return new Color(rgb);
    }
    
    private Color darker(Color c, int value) {
        if (value < 0) return c;
        
        float[] hsb = Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),new float[3]);
        int h = (int) (hsb[0] * 360);
        int s = (int) (hsb[1] * 100);
        int b = (int) (hsb[2] * 100);
        
        int rm = 0;
        b -= value;
        if (b < 0) {
            rm = b * (-1);
            b = 0;
        }
        s += rm;
        if (s > 100) s = 100;
        
        int rgb = Color.HSBtoRGB(h/360.0f, s/100.0f, b/100.0f);
        return new Color(rgb);
    }
    
}
