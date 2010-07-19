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
import com.rameses.rcp.control.XTextField;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusListener;
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

public final class TableManager {
    
    public static final Insets CELL_MARGIN = new Insets(1, 3, 1, 1);
    public static final Color FOCUS_BG = new Color(254, 255, 208);
    public static final String HIDE_ON_ENTER = "hide.on.enter";
    
    private static Map<String, Class<?extends JComponent>> editors = new HashMap();
    private static Map<String, Class> numClass = new HashMap();
    private static Map<String, TableCellRenderer> renderers = new HashMap();
    private static TableCellRenderer headerRenderer = new TableHeaderRenderer();
    
    
    //<editor-fold defaultstate="collapsed" desc="  static initializer  ">
    static {
        //map of editors
        editors.put("string", XTextField.class);
        editors.put("boolean", XCheckBox.class);
        editors.put("combo", XComboBox.class);
        editors.put("date", XDateField.class);
        editors.put("double", XNumberField.class);
        editors.put("integer", XNumberField.class);
        editors.put("decimal", XNumberField.class);
        editors.put("lookup", XLookupField.class);
        
        //map of renderers
        TableCellRenderer renderer = new StringRenderer();
        renderers.put("string", renderer);
        renderers.put("number", renderer);
        renderers.put("decimal", renderer);
        renderers.put("date", renderer);
        renderers.put("combo", renderer);
        renderers.put("lookup", renderer);
        
        renderer = new BooleanRenderer();
        renderers.put("boolean", renderer);
        
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
    
    public static boolean hideOnEnter(JComponent editor) {
        String hide = editor.getClientProperty(HIDE_ON_ENTER)+"";
        return !"false".equals(hide);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper  ">
    private static void customize(JComponent editor, Column col) {
        String type = col.getType()+"";
        editor.setBackground(FOCUS_BG);
        Font font = (Font) UIManager.get("Table.font");
        editor.setFont(font);
        
        //remove all focus listeners (we don't need it in the table)
        for (FocusListener l: editor.getFocusListeners() ) {
            editor.removeFocusListener(l);
        }
        
        if ( "boolean".equals(type) ) {
            ((JCheckBox) editor).setHorizontalAlignment(SwingConstants.CENTER);
        } else if ( editor instanceof XNumberField ) {
            ((XNumberField) editor).setFieldType(numClass.get(type));
        } else if ( "lookup".equals(type) ) {
            XLookupField lookup = (XLookupField) editor;
            lookup.setHandler( col.getHandler() );
            lookup.setTranserFocusOnSelect(false);
            lookup.putClientProperty(HIDE_ON_ENTER, false);
        }
        
        if ( "combo".equals(type) ) {
            XComboBox cbox = (XComboBox) editor;
            if ( col.getItems() != null ) {
                cbox.setItems( col.getItems() );
            }
            if ( col.isRequired() ) {
                cbox.setAllowNull(false);
            }
        } else {
            editor.setBorder(BorderFactory.createEmptyBorder(CELL_MARGIN.top, CELL_MARGIN.left, CELL_MARGIN.bottom, CELL_MARGIN.right));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  TableHeaderRenderer (class)  ">
    private static class TableHeaderRenderer implements TableCellRenderer {
        
        private JLabel label = new JLabel();
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            label.setText( ValueUtil.isEmpty(value) ? " " : value + "");
            Border bb = new TableHeaderBorder();
            Border eb = BorderFactory.createEmptyBorder(2,5,2,1);
            label.setBorder( BorderFactory.createCompoundBorder(bb, eb) );
            return label;
        }
        
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  AbstractRenderer (class)  ">
    private static abstract class AbstractRenderer implements TableCellRenderer {
        
        public abstract JComponent getComponent();
        
        public abstract void refresh(JTable table, Object value, boolean selected, boolean focus, int row, int column);
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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
            }
            
            TableComponent tc = (TableComponent) table;
            if ( tc.getListModel() instanceof SubListModel ) {
                SubListModel slm = (SubListModel) tc.getListModel();
                String errmsg = slm.getErrorMessage(row);
                
                if (errmsg != null) {
                    if (!hasFocus) {
                        comp.setBackground(Color.PINK);
                        comp.setOpaque(true);
                    }
                }
            }
            
            refresh(table, value, isSelected, hasFocus, row, column);
            return comp;
        }
        
        protected int getAlignmentConstant(String alignment) {
            if ( "right".equals(alignment)) return SwingConstants.RIGHT;
            if ( "center".equals(alignment) ) return SwingConstants.CENTER;
            
            return SwingConstants.LEFT;
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
            if ( "decimal".equals(c.getType()) || value instanceof BigDecimal || value instanceof Double ) {
                label.setHorizontalAlignment( SwingConstants.RIGHT );
                label.setText((value == null ? "" : format(value, format, "#,##0.00")));
                
            } else if ( "number".equals(c.getType()) || value instanceof Number ) {
                label.setHorizontalAlignment( SwingConstants.CENTER );
                label.setText((value == null ? "" : format(value, format, "#,##0")));
                
            } else if ( "date".equals(c.getType()) || value instanceof Date ||
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
