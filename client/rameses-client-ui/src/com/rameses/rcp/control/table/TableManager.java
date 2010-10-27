/*
 * TableManager.java
 *
 * Created on June 26, 2010, 10:53 AM
 * @author jaycverg
 */

package com.rameses.rcp.control.table;

import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.SubListModel;
import com.rameses.rcp.control.XCheckBox;
import com.rameses.rcp.control.XComboBox;
import com.rameses.rcp.control.XDateField;
import com.rameses.rcp.control.XLookupField;
import com.rameses.rcp.control.XNumberField;
import com.rameses.rcp.control.XTable;
import com.rameses.rcp.control.XTextField;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusListener;
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
import javax.swing.JTextField;
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

public final class TableManager {
    
    public static final Insets CELL_MARGIN = new Insets(1, 3, 1, 1);
    public static final Color FOCUS_BG = new Color(254, 255, 208);
    public static final String HIDE_ON_ENTER = "hide.on.enter";
    
    private static Map<String, Class<? extends JComponent>> editors = new HashMap();
    private static Map<String, Class> numClass = new HashMap();
    private static Map<String, TableCellRenderer> renderers = new HashMap();
    private static TableCellRenderer headerRenderer = new TableHeaderRenderer();
    
    
    //<editor-fold defaultstate="collapsed" desc="  static initializer  ">
    static {
        //map of editors
        editors.put("string", XTextField.class);
        editors.put("boolean", XCheckBox.class);
        editors.put("checkbox", XCheckBox.class);
        editors.put("combo", XComboBox.class);
        editors.put("date", XDateField.class);
        editors.put("double", XNumberField.class);
        editors.put("integer", XNumberField.class);
        editors.put("decimal", XNumberField.class);
        editors.put("lookup", XLookupField.class);
        
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
        
        //number class types
        numClass.put("decimal", BigDecimal.class);
        numClass.put("integer", Integer.class);
        numClass.put("double", Double.class);
    }
    //</editor-fold>
    
    public static JComponent createCellEditor(Column col) {
        String type = col.getType()+"";
        JComponent editor = null;
        try {
            editor = editors.get(type).newInstance();
            customize(editor, col);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return editor;
    }
    
    public static TableCellRenderer getCellRenderer(String type) {
        return renderers.get(type);
    }
    
    public static TableCellRenderer getHeaderRenderer() {
        return headerRenderer;
    }
    
    public static JComponent getTableCornerComponent() {
        JLabel label = new JLabel(" ");
        Border bb = new TableHeaderBorder();
        Border eb = BorderFactory.createEmptyBorder(2,5,2,1);
        label.setBorder( BorderFactory.createCompoundBorder(bb, eb) );
        return label;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  editor customizer method  ">
    private static void customize(JComponent editor, Column col) {
        //add XTable flag to notify that editor is in XTable component
        editor.putClientProperty(XTable.class, true);
        
        //remove all focus listeners (we don't need it in the table)
        for (FocusListener l: editor.getFocusListeners() ) {
            editor.removeFocusListener(l);
        }
        
        String type = col.getType()+"";
        if ( editor instanceof JCheckBox ) {
            ((JCheckBox) editor).setHorizontalAlignment(SwingConstants.CENTER);
            
        } else if ( editor instanceof XNumberField ) {
            XNumberField xnf = (XNumberField) editor;
            xnf.setFieldType(numClass.get(type));
            if ( !ValueUtil.isEmpty(col.getFormat()) ) {
                xnf.setPattern( col.getFormat() );
            }
            
        } else if ( editor instanceof XLookupField ) {
            XLookupField xlf = (XLookupField) editor;
            xlf.setHandler( col.getHandler() );
            xlf.setTranserFocusOnSelect(false);
            
        } else if ( editor instanceof XDateField ) {
            XDateField xdf = (XDateField) editor;
            xdf.setHorizontalAlignment(SwingConstants.CENTER);
            if ( !ValueUtil.isEmpty(col.getFormat()) ) {
                xdf.setInputFormat( col.getFormat() );
                xdf.setOutputFormat( col.getFormat() );
            }
        }
        
        
        editor.setBackground(FOCUS_BG);
        Font font = (Font) UIManager.get("Table.font");
        editor.setFont(font);
        
        //set alignment if it is specified in the Column model
        if ( col.getAlignment() != null && editor instanceof JTextField ) {
            JTextField jtf = (JTextField) editor;
            if ( "right".equals(col.getAlignment().toLowerCase()) )
                jtf.setHorizontalAlignment(SwingConstants.LEFT);
            else if ( "center".equals(col.getAlignment().toLowerCase()))
                jtf.setHorizontalAlignment(SwingConstants.CENTER);
            else if ( "left".equals(col.getAlignment().toLowerCase()) )
                jtf.setHorizontalAlignment(SwingConstants.LEFT);
        }
        
        if ( editor instanceof XComboBox ) {
            XComboBox cbox = (XComboBox) editor;
            if ( col.getItems() != null ) {
                cbox.setItems( col.getItems() );
            }
            if ( col.isRequired() ) {
                cbox.setAllowNull(false);
            }
            if ( col.getFieldType() != null ) {
                cbox.setFieldType( col.getFieldType() );
            }
            
        } else {
            Border b = BorderFactory.createEmptyBorder(CELL_MARGIN.top, CELL_MARGIN.left, CELL_MARGIN.bottom, CELL_MARGIN.right);
            editor.setBorder( b );
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  TableHeaderRenderer (class)  ">
    private static class TableHeaderRenderer implements TableCellRenderer {
        
        private JLabel label = new JLabel();
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if ( !Beans.isDesignTime() ) {
                TableComponent xtable = (TableComponent) table;
                Column c = ((DefaultTableModel)xtable.getModel()).getColumn(column);
                if ( isRightAligned(c, value) ) {
                    label.setHorizontalAlignment( SwingConstants.RIGHT );
                    
                } else if ( isCenterAligned(c, value) ) {
                    label.setHorizontalAlignment( SwingConstants.CENTER );
                    
                } else {
                    label.setHorizontalAlignment( SwingConstants.LEFT );
                }
            }
            
            label.setText( ValueUtil.isEmpty(value) ? " " : value + "");
            Border bb = new TableHeaderBorder();
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
    
    
    //<editor-fold defaultstate="collapsed" desc="  AbstractRenderer (class)  ">
    private static abstract class AbstractRenderer implements TableCellRenderer {
        
        public abstract JComponent getComponent();
        
        public abstract void refresh(JTable table, Object value, boolean selected, boolean focus, int row, int column);
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            TableComponent xtable = (TableComponent) table;
            JComponent comp = getComponent();
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
            
            TableComponent tc = (TableComponent) table;
            if ( tc.getListModel() instanceof SubListModel ) {
                SubListModel slm = (SubListModel) tc.getListModel();
                String errmsg = slm.getErrorMessage(row);
                
                if (errmsg != null) {
                    if (!hasFocus) {
                        comp.setBackground( xtable.getErrorBackground() );
                        comp.setForeground( xtable.getErrorForeground() );
                        comp.setOpaque(true);
                    }
                }
            }
            
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
            label.setVerticalAlignment(SwingConstants.TOP);
        }
        
        public JComponent getComponent() { return label; }
        
        public void refresh(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
            TableComponent tc = (TableComponent) table;
            Column c = ((DefaultTableModel) tc.getModel()).getColumn(column);
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
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                else if ( "center".equals(c.getAlignment().toLowerCase()))
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                else if ( "left".equals(c.getAlignment().toLowerCase()) )
                    label.setHorizontalAlignment(SwingConstants.LEFT);
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
        
        BooleanRenderer() {
            component = new JCheckBox();
            component.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        public JComponent getComponent() { return component; }
        
        public void refresh(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
            component.setSelected("true".equals(value+""));
        }
    }
    //</editor-fold>
    
}
