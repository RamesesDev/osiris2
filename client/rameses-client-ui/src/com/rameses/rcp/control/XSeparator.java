/*
 * XSeparator.java
 *
 * Created on October 12, 2010, 5:34 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.constant.UIConstants;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.beans.Beans;
import javax.swing.JComponent;
import javax.swing.UIManager;

public class XSeparator extends JComponent implements UIControl, ActiveControl, UIConstants {
    
    private ControlProperty property = new ControlProperty();
    private String orientation = HORIZONTAL;
    private String orientationHPosition = CENTER;
    private String orientationVPosition = CENTER;
    private Insets padding = new Insets(0,0,0,0);
    
    private Color lineColor;
    private Color lineShadow;
    
    
    public XSeparator() {
        setLayout(null);
        property.setShowCaption(false);
        
        lineColor = UIManager.getColor("Separator.foreground");
        lineShadow = UIManager.getColor("Separator.highlight");
        
        if ( Beans.isDesignTime() ) {
            setPreferredSize(new Dimension(20,20));
        }
    }
    
    public void setLayout(LayoutManager layout) {}
    
    public void paint(Graphics graphics) {
        super.paint(graphics);
        
        Graphics2D g = (Graphics2D) graphics.create();
        
        Dimension d = getSize();
        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        int sx1 = 1, sy1 = 1, sx2 = 1, sy2 = 1;
        
        if ( VERTICAL.equals(orientation) ) {
            y1 = sy1 = padding.top;
            y2 = sy2 = d.height - 2 - padding.bottom;
            if ( RIGHT.equals(orientationVPosition) ) {
                x1 = x2 = d.width - 2;
                sx1 = sx2 = d.width - 1;
            } else if ( CENTER.equals(orientationVPosition) ) {
                x1 = x2 = ((d.width - 2) / 2);
                sx1 = sx2 = x1+1;
            }
            
        } else {
            x1 = sx1 = padding.left;
            x2 = sx2 = d.width - 2 - padding.right;
            if ( BOTTOM.equals(orientationHPosition) ) {
                y1 = y2 = d.height - 2;
                sy1 = sy2 = d.height - 1;
            } else if ( CENTER.equals(orientationHPosition) ) {
                y1 = y2 = ((d.height - 2) / 2);
                sy1 = sy2 = y1+1;
            }
        }
        
        g.setColor(lineColor);
        g.drawLine(x1, y1, x2, y2);
        
        g.setColor(lineShadow);
        g.drawLine(sx1, sy1, sx2, sy2);
        
        g.dispose();
    }
    
    public String[] getDepends() { return null; }
    
    public int getIndex() { return 0; }
    
    public void setBinding(Binding binding) {}
    public Binding getBinding() { return null; }
    public void refresh() {}
    public void load() {}
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public ControlProperty getControlProperty() {
        return property;
    }
    
    public boolean isShowCaption() {
        return property.isShowCaption();
    }
    
    public void setShowCaption(boolean show) {
        property.setShowCaption(show);
    }
    
    public String getCaption() {
        return property.getCaption();
    }
    
    public void setCaption(String caption) {
        property.setCaption(caption);
    }
    
    public int getCaptionWidth() {
        return property.getCaptionWidth();
    }
    
    public void setCaptionWidth(int width) {
        property.setCaptionWidth(width);
    }
    
    public Font getCaptionFont() {
        return property.getCaptionFont();
    }
    
    public void setCaptionFont(Font f) {
        property.setCaptionFont(f);
    }
    
    public Insets getCellPadding() {
        return property.getCellPadding();
    }
    
    public void setCellPadding(Insets padding) {
        property.setCellPadding(padding);
    }
    
    public String getOrientation() {
        return orientation;
    }
    
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    
    public String getOrientationHPosition() {
        return orientationHPosition;
    }
    
    public void setOrientationHPosition(String orientationHPosition) {
        this.orientationHPosition = orientationHPosition;
    }
    
    public String getOrientationVPosition() {
        return orientationVPosition;
    }
    
    public void setOrientationVPosition(String orientationVPosition) {
        this.orientationVPosition = orientationVPosition;
    }
    
    public Insets getPadding() {
        return padding;
    }
    
    public void setPadding(Insets padding) {
        if ( padding != null )
            this.padding = padding;
        else
            this.padding = new Insets(0,0,0,0);
    }
    
    public Color getLineColor() {
        return lineColor;
    }
    
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
    
    public Color getLineShadow() {
        return lineShadow;
    }
    
    public void setLineShadow(Color lineShadow) {
        this.lineShadow = lineShadow;
    }
    //</editor-fold>
    
}
