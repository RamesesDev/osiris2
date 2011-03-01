/*
 * ReportSheetHeader.java
 *
 * Created on February 17, 2011, 4:55 PM
 * @author jaycverg
 */

package com.rameses.bi.control.reportsheet;

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


public class ReportSheetHeader extends JTableHeader {
    
    public ReportSheetHeader() {
        setReorderingAllowed(false);
        setDefaultRenderer(new TableHeaderRenderer());
    }
    
    //<editor-fold defaultstate="collapsed" desc="  TableHeaderRenderer (class)  ">
    public static class TableHeaderRenderer implements TableCellRenderer {
        
        private JLabel label = new JLabel();
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if ( !Beans.isDesignTime() ) {
                ReportSheetTable xtable = (ReportSheetTable) table;
                Column c = ((ReportSheetTableModel)xtable.getModel()).getColumn(column);
                if ( isRightAligned(c, value) ) {
                    label.setHorizontalAlignment( SwingConstants.RIGHT );
                    
                } else if ( isCenterAligned(c, value) ) {
                    label.setHorizontalAlignment( SwingConstants.CENTER );
                    
                } else {
                    label.setHorizontalAlignment( SwingConstants.LEFT );
                }
            }
            
            label.setText( ValueUtil.isEmpty(value) ? " " : value + "");
            Border bb = new HeaderBorder();
            Border eb = BorderFactory.createEmptyBorder(2,5,2,5);
            label.setBorder( BorderFactory.createCompoundBorder(bb, eb) );
            return label;
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
        
    }
    //</editor-fold>
    
}
