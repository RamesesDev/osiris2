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

public class XLineBorder extends AbstractBorder {
    
    private Color background;
    private Color lineColor;
    private int thickness;
    private int arc;
    
    
    public XLineBorder() {
        lineColor = Color.BLACK;
        thickness = 1;
        arc = 0;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if ( background != null ) {
            g2.setColor(background);
            g2.fillRoundRect(x, y, width-1, height-1, arc, arc);
        }
        
        g2.setStroke(new BasicStroke(thickness));
        g2.setColor(lineColor);
        
        g2.drawRoundRect(x, y, width-1, height-1, arc, arc);
        
        g2.dispose();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public Insets getBorderInsets(Component c) {
        Insets insets = new Insets(0,0,0,0);
        getBorderInsets(c, insets);
        return insets;
    }
    
    public Insets getBorderInsets(Component c, Insets insets) {
        int radius = (int) (arc / Math.PI);
        insets.top = insets.left = insets.bottom = insets.right = Math.max(radius, thickness);
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
    
    public int getArc() {
        return arc;
    }
    
    public void setArc(int arc) {
        this.arc = arc;
    }
    
    public Color getBackground() {
        return background;
    }
    
    public void setBackground(Color background) {
        this.background = background;
    }
    //</editor-fold>

}
