/*
 * ReportSheetHelper.java
 *
 * Created on June 26, 2010, 10:53 AM
 * @author jaycverg
 */

package com.rameses.bi.control.reportsheet;

import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.Column;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.beans.Beans;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

/**
 * @description
 *   This class handles TableComponents cell renderer and editor management
 * Default Alignments:
 *  1. decimal/double - right
 *  2. integer/boolean/checkbox/date - center
 *  3. string - left
 */

public final class ReportSheetHelper {
    
    public static final Insets CELL_MARGIN = new Insets(1, 5, 1, 5);
    public static final Color FOCUS_BG = new Color(254, 255, 208);
    
    private static Map<String, TableCellRenderer> renderers = new HashMap();
    
    
    //<editor-fold defaultstate="collapsed" desc="  static initializer  ">
    static {
        //map of renderers
        TableCellRenderer renderer = new StringRenderer();
        renderers.put("string", renderer);
        renderers.put("integer", renderer);
        renderers.put("decimal", renderer);
        renderers.put("double", renderer);
        renderers.put("date", renderer);
        renderers.put("combo", renderer);
        renderers.put("lookup", renderer);
        
        renderer = new BooleanRenderer();
        renderers.put("boolean", renderer);
        renderers.put("checkbox", renderer);
    }
    //</editor-fold>
    
    public static TableCellRenderer getCellRenderer(String type) {
        return renderers.get(type);
    }
        
    public static JComponent getTableCornerComponent() {
        JLabel label = new JLabel(" ");
        Border bb = new HeaderBorder();
        Border eb = BorderFactory.createEmptyBorder(2,5,2,1);
        label.setBorder( BorderFactory.createCompoundBorder(bb, eb) );
        return label;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  AbstractRenderer (class)  ">
    private static abstract class AbstractRenderer implements TableCellRenderer {
        
        public abstract JComponent getComponent(JTable table, int row, int column);
        
        public abstract void refresh(JTable table, Object value, boolean selected, boolean focus, int row, int column);
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            ReportSheetComponent xtable = (ReportSheetComponent) table;
            JComponent comp = getComponent(table, row, column);
            comp.setBorder(BorderFactory.createEmptyBorder(CELL_MARGIN.top, CELL_MARGIN.left, CELL_MARGIN.bottom, CELL_MARGIN.right));
            comp.setFont(table.getFont());
            
            if (isSelected) {
                comp.setBackground(table.getSelectionBackground());
                comp.setForeground(table.getSelectionForeground());
                comp.setOpaque(true);
                
                if (hasFocus) {
                    comp.setBackground(FOCUS_BG);
                    comp.setForeground(table.getForeground());
                }
                
            } else {
                comp.setForeground(table.getForeground());
                comp.setOpaque(false);
                
                boolean even = (row % 2 == 0);
                if ( even ) {
                    if ( xtable.getEvenBackground() != null ) {
                        comp.setBackground( xtable.getEvenBackground() );
                        comp.setOpaque(true);
                    }
                    if ( xtable.getEvenForeground() != null ) {
                        comp.setForeground( xtable.getEvenForeground() );
                    }
                    
                } else {
                    if ( xtable.getOddBackground() != null ) {
                        comp.setBackground( xtable.getOddBackground() );
                        comp.setOpaque(true);
                    }
                    if ( xtable.getOddForeground() != null ) {
                        comp.setForeground( xtable.getOddForeground() );
                    }
                }
            }
            
            AbstractListModel lm = xtable.getListModel();
            String errmsg = lm.getErrorMessage(row);
            
            if (errmsg != null) {
                if (!hasFocus) {
                    comp.setBackground( xtable.getErrorBackground() );
                    comp.setForeground( xtable.getErrorForeground() );
                    comp.setOpaque(true);
                }
            }
            
            //border support
            Border inner = BorderFactory.createEmptyBorder(CELL_MARGIN.top, CELL_MARGIN.left, CELL_MARGIN.bottom, CELL_MARGIN.right);
            Border border = BorderFactory.createEmptyBorder(1,1,1,1);
            if (hasFocus) {
                if (isSelected) {
                    border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
                }
                if (border == null) {
                    border = UIManager.getBorder("Table.focusCellHighlightBorder");
                }
            }
            comp.setBorder(BorderFactory.createCompoundBorder(border, inner));
            
            refresh(table, value, isSelected, hasFocus, row, column);
            return comp;
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  StringRenderer (class)  ">
    private static class StringRenderer extends AbstractRenderer {
        
        private JLabel label;
        
        StringRenderer() {
            label = new JLabel();
            label.setVerticalAlignment(SwingConstants.CENTER);
        }
        
        public JComponent getComponent(JTable table, int row, int column) {
            return label;
        }
        
        public void refresh(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
            ReportSheetComponent tc = (ReportSheetComponent) table;
            Column c = ((ReportSheetModel) tc.getModel()).getColumn(column);
            String format = c.getFormat();
            String type = c.getType();
            if ( "decimal".equals(type) || "double".equals(type) || value instanceof BigDecimal || value instanceof Double ) {
                label.setHorizontalAlignment( SwingConstants.RIGHT );
                label.setText((value == null ? "" : format(value, format, "#,##0.00")));
                
            } else if ( "integer".equals(type) || value instanceof Number ) {
                label.setHorizontalAlignment( SwingConstants.CENTER );
                label.setText((value == null ? "" : format(value, format, "#,##0")));
                
            } else if ( "date".equals(type) || value instanceof Date ||
                    value instanceof Time || value instanceof Timestamp ) {
                
                label.setHorizontalAlignment( SwingConstants.CENTER );
                SimpleDateFormat formatter = null;
                if ( format != null )
                    formatter = new SimpleDateFormat(format);
                else
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                
                label.setText((value == null ? "" : formatter.format(value)));
                
            } else {
                label.setHorizontalAlignment( SwingConstants.LEFT );
                if ( value != null && c.isHtmlDisplay() ) {
                    value = "<html>" + value + "</html>";
                }
                label.setText((value == null ? "" : value.toString()));
            }
            
            //set alignment if it is specified in the Column model
            if ( c.getAlignment() != null ) {
                if ( "right".equals(c.getAlignment().toLowerCase()) )
                    label.setHorizontalAlignment(SwingConstants.RIGHT);
                else if ( "center".equals(c.getAlignment().toLowerCase()))
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                else if ( "left".equals(c.getAlignment().toLowerCase()) )
                    label.setHorizontalAlignment(SwingConstants.LEFT);
            }
            
            //set vertical alignment if it is specified in the Column model
            if ( c.getVAlignment() != null ) {
                if ( "top".equals(c.getVAlignment().toLowerCase()) )
                    label.setVerticalAlignment(SwingConstants.TOP);
                else if ( "center".equals(c.getVAlignment().toLowerCase()) )
                    label.setVerticalAlignment(SwingConstants.CENTER);
                else if ( "bottom".equals(c.getVAlignment().toLowerCase()) )
                    label.setVerticalAlignment(SwingConstants.BOTTOM);
            }
        }
        
        private String format(Object value, String format, String defaultFormat) {
            DecimalFormat formatter = null;
            if ( format != null)
                formatter = new DecimalFormat(format);
            else
                formatter = new DecimalFormat(defaultFormat);
            
            return formatter.format(value);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  BooleanRenderer (class)  ">
    private static class BooleanRenderer extends AbstractRenderer {
        
        private JCheckBox component;
        private JLabel empty;
        
        BooleanRenderer() {
            component = new JCheckBox();
            component.setHorizontalAlignment(SwingConstants.CENTER);
            component.setBorderPainted(true);
            
            //empty renderer when row object is null
            empty = new JLabel("");
        }
        
        public JComponent getComponent(JTable table, int row, int column) {
            AbstractListModel alm = ((ReportSheetComponent) table).getListModel();
            if ( alm.getItemList().get(row).getItem() == null )
                return empty;
            
            return component;
        }
        
        public void refresh(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
            AbstractListModel alm = ((ReportSheetComponent) table).getListModel();
            if ( alm.getItemList().get(row).getItem() == null ) return;
            
            component.setSelected("true".equals(value+""));
        }
    }
    //</editor-fold>
    
}
