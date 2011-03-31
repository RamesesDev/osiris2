/*
 * TreeTableHeader.java
 *
 * Created on February 17, 2011, 4:55 PM
 * @author jaycverg
 */

package com.rameses.rcp.control.treetable;

import com.rameses.rcp.common.Column;
import com.rameses.util.ValueUtil;
import java.awt.Component;
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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;


public class TreeTableHeader extends JTableHeader {
    
    public TreeTableHeader(TableColumnModel colModel) {
        super(colModel);
        super.setReorderingAllowed(false);
        setUI(new TreeTableHeaderUI());
        setDefaultRenderer(new TableHeaderRenderer());
    }
    
    public void setReorderingAllowed(boolean allow) {;}
    
    //<editor-fold defaultstate="collapsed" desc="  TableHeaderRenderer (class)  ">
    public static class TableHeaderRenderer extends JLabel implements TableCellRenderer {
        
        public TableHeaderRenderer() {}
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if ( !Beans.isDesignTime() ) {
                TreeTableComponent xtable = (TreeTableComponent) table;
                Column c = ((TreeTableComponentModel)xtable.getModel()).getColumn(column);
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
    //</editor-fold>
    
}
