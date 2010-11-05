/*
 * XLineBorder.java
 *
 * Created on October 13, 2010, 8:15 PM
 * @author jaycverg
 */

package com.rameses.rcp.control.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;



public class XTitledBorder extends AbstractBorder {
    
    private String title = "Title";
    private Color background;
    private Color titleBackground;
    private Color titleForeground;
    private Insets titlePadding = new Insets(2,4,2,4);
    private Font font;
    private Color outline;
    private Color outlineShadow;
    
    
    public XTitledBorder() {
        font = new Font("Arial", Font.BOLD, 11);
        background = new Color(232,232,226);
        titleBackground = new Color(167,156,146);
        titleForeground = Color.WHITE;
        outline = new Color(204,204,204);
        outlineShadow = new Color(204,204,204);
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int x2 = 0, y2 = 0;
        if( background != null ) {
            g2.setColor(background);
            g2.fillRect(x, y, width - 3, height - 3);
        }
        g2.setColor(outline);
        g2.drawRect(x, y, width - 3, height - 3);
        
        //border shadow
        Color shadow = outlineShadow;
        for(int i=1; i < 3; ++i) {
            g2.setColor(new Color(shadow.getRed(), shadow.getGreen(), shadow.getBlue(), 40*i ));
            x2 = width - i;
            y2 = height - i;
            g2.drawLine(x+2, y2, x2, y2);
            g2.drawLine(x2, y+2, x2, y2);
        }
        
        //titlebar
        if ( title == null || title.equals("")) return;
        
        Insets padding = titlePadding;
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        
        int strWidth = fm.stringWidth(title);
        y2 = y + fm.getHeight() + padding.top + padding.bottom;
        x2 = x + strWidth + padding.left + padding.right;
        
        Polygon poly = new Polygon();
        poly.addPoint(x, y);
        poly.addPoint(x, y2);
        poly.addPoint(x2, y2);
        poly.addPoint(x2+10, y);
        
        //shadow (show size is 2)
        g2.setColor(new Color(shadow.getRed(), shadow.getGreen(), shadow.getBlue(), 60));
        g2.drawLine(x+1, y2+1, x2-2, y2+1);
        
        g2.setColor(new Color(shadow.getRed(), shadow.getGreen(), shadow.getBlue(), 40));
        g2.drawLine(x+1, y2+2, x2-2, y2+2);
        
        g2.setColor(titleBackground);
        g2.fillPolygon(poly);
        
        g2.setColor(outline);
        g2.drawPolygon(poly);
        
        //draw the title
        g2.setColor(titleForeground);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.drawString(title, x + padding.left, y + padding.top + (fm.getHeight() - fm.getDescent()));
        
        g2.dispose();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public Insets getBorderInsets(Component c) {
        FontMetrics fm = c.getFontMetrics(font);
        Insets p = titlePadding;
        
        //add the title drop shadow to the insets
        return new Insets(fm.getHeight()+p.top+p.bottom+3, 1,3,3);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Color getTitleBackground() {
        return titleBackground;
    }
    
    public void setTitleBackground(Color titleBackground) {
        this.titleBackground = titleBackground;
    }
    
    public Font getFont() {
        return font;
    }
    
    public void setFont(Font font) {
        this.font = font;
    }
    
    public Color getOutline() {
        return outline;
    }
    
    public void setOutline(Color outline) {
        this.outline = outline;
    }
    
    public Color getOutlineShadow() {
        return outlineShadow;
    }
    
    public void setOutlineShadow(Color outlineShadow) {
        this.outlineShadow = outlineShadow;
    }
    
    public Color getTitleForeground() {
        return titleForeground;
    }
    
    public void setTitleForeground(Color titleForeground) {
        this.titleForeground = titleForeground;
    }
    
    public Insets getTitlePadding() {
        return titlePadding;
    }
    
    public void setTitlePadding(Insets titlePadding) {
        this.titlePadding = titlePadding;
    }
    
    public Color getBackground() {
        return background;
    }
    
    public void setBackground(Color background) {
        this.background = background;
    }
    //</editor-fold>
}
