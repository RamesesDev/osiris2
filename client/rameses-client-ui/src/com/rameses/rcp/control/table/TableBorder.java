/*
 * TableBorder.java
 *
 * Created on March 9, 2011, 4:34 PM
 * @author jaycverg
 */

package com.rameses.rcp.control.table;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;


public class TableBorder extends AbstractBorder {
    
    private static final Insets insets = new Insets(1, 1, 2, 2);
    
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        
        g.translate( x, y);
        
        g.setColor( MetalLookAndFeel.getControlDarkShadow() );
        g.drawRect( 0, 0, w-2, h-2 );
        g.setColor( MetalLookAndFeel.getControlHighlight() );
        
        g.drawLine( w-1, 1, w-1, h-1);
        g.drawLine( 1, h-1, w-1, h-1);
        
        g.setColor( MetalLookAndFeel.getControl() );
        g.drawLine( w-2, 2, w-2, 2 );
        g.drawLine( 1, h-2, 1, h-2 );
        
        g.translate( -x, -y);
        
    }
    
    public Insets getBorderInsets(Component c)       {
        return insets;
    }
}