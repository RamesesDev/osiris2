/*
 * XLineBorder.java
 *
 * Created on October 18, 2010, 10:18 AM
 * @author jaycverg
 */

package com.rameses.rcp.control.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;

public class XUnderlineBorder extends AbstractBorder {
    
    private Color lineColor;
    private int thickness;
    
    
    public XUnderlineBorder() {
        lineColor = Color.LIGHT_GRAY;
        thickness = 1;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setStroke(new BasicStroke(thickness));
        g2.setColor(lineColor);
        
        g2.drawLine(x, y+height-1, x+width-1, y+height-1);
        
        g2.dispose();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public Insets getBorderInsets(Component c) {
        Insets insets = new Insets(0,0,0,0);
        getBorderInsets(c, insets);
        return insets;
    }
    
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = insets.left = insets.right = 0;
        insets.bottom = thickness;
        return insets;
    }
    
    public Color getLineColor() {
        return lineColor;
    }
    
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
    
    public int getThickness() {
        return thickness;
    }
    
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }
    //</editor-fold>

}
