/*
 * TableHeaderRenderer.java
 *
 * Created on February 22, 2011, 2:38 PM
 * @author jaycverg
 */

package com.rameses.rcp.control.table;

import com.rameses.rcp.common.Column;
import com.rameses.rcp.support.ColorUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.Beans;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;


public class TableHeaderRenderer extends JLabel implements TableCellRenderer {
    
    public TableHeaderRenderer() {}
    
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Color bg = Color.LIGHT_GRAY;
        Dimension d = getSize();
        GradientPaint gp = new GradientPaint(0, 0, ColorUtil.brighter(bg, 30), 0, (d.height-1)/2, ColorUtil.brighter(bg, 15));
        g2.setPaint(gp);
        g2.fillRect(0,0,d.width-1,d.height-1);
        g2.dispose();
        
        super.paint(g);
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if ( !Beans.isDesignTime() ) {
            TableControl xtable = (TableControl) table;
            Column c = ((TableControlModel)xtable.getModel()).getColumn(column);
            if ( isRightAligned(c, value) ) {
                setHorizontalAlignment( SwingConstants.RIGHT );
                
            } else if ( isCenterAligned(c, value) ) {
                setHorizontalAlignment( SwingConstants.CENTER );
                
            } else {
                setHorizontalAlignment( SwingConstants.LEFT );
            }
        }
        
        setText( ValueUtil.isEmpty(value) ? " " : value + "");
        Border bb = BorderFactory.createLineBorder(table.getGridColor());
        Border eb = BorderFactory.createEmptyBorder(2,5,2,5);
        setBorder( BorderFactory.createCompoundBorder(bb, eb) );
        
        return this;
    }
    
    private boolean isRightAligned(Column c, Object value) {
        return (c.getType()+"").matches("decimal|double") ||
                value instanceof Double || value instanceof BigDecimal;
    }
    
    private boolean isCenterAligned(Column c, Object value) {
        return (c.getType()+"").matches("date|integer|boolean|checkbox") ||
                value instanceof Number || value instanceof Date ||
                value instanceof Time || value instanceof Timestamp;
    }
    
    // The following methods override the defaults for performance reasons
    public void validate() {}
    public void revalidate() {}
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
    
}