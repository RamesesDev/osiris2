package com.rameses.rcp.control.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;

public class TableCornerBorder extends AbstractBorder {
    
    public TableCornerBorder() {
    }
    
    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(0,0,0,0));
    }
    
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top += 2;
        insets.left += 2;
        insets.bottom += 2;
        insets.right += 2;
        return insets;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        Color oldColor = g.getColor();
        Color hilite = c.getBackground().brighter();
        Color shadow = c.getBackground().darker();
        g.setColor(hilite);
        g.drawRect(0,0,w,h);
        g.setColor(shadow);
        g.drawRect(-1,-1,w,h);
        g.setColor(oldColor);
    }
}
