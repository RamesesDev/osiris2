/*
 * ReportSheetUtil.java
 *
 * Created on June 26, 2010, 10:53 AM
 * @author jaycverg
 */

package com.rameses.bi.control.reportsheet;

import com.rameses.bi.common.ReportSheetModel;
import com.rameses.common.ExpressionResolver;
import com.rameses.rcp.common.BeanWrapper;
import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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

public final class ReportSheetUtil {
    
    private static final Insets CELL_MARGIN = new Insets(1, 5, 1, 5);
    private static final Color FOCUS_BG = new Color(254, 255, 208);
    private static final String COLLAPSED_ICON = "/com/rameses/bi/icons/collapsed.png";
    private static final String EXPANDED_ICON = "/com/rameses/bi/icons/expanded.png";
    
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
    
    public static JComponent getTableCornerComponent(Color borderColor) {
        JLabel label = new JLabel(" ");
        Border bb = BorderFactory.createLineBorder(borderColor);
        Border eb = BorderFactory.createEmptyBorder(2,5,2,1);
        label.setBorder( BorderFactory.createCompoundBorder(bb, eb) );
        return label;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  AbstractRenderer (class)  ">
    private static abstract class AbstractRenderer implements TableCellRenderer {
        
        public abstract JComponent getComponent(JTable table, int row, int column);
        
        public abstract void refresh(JTable table, Object value, boolean selected, boolean focus, int row, int column);
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            ReportSheetTable xtable = (ReportSheetTable) table;
            ReportSheetTableModel xtableModel = (ReportSheetTableModel) xtable.getModel();
            Column colModel = xtableModel.getColumn(column);
            
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
            
            ReportSheetModel model = xtable.getListModel();
            ClientContext clientCtx = ClientContext.getCurrentContext();
            ExpressionResolver exprRes = clientCtx.getExpressionResolver();
            ListItem listItem = model.getItemList().get(row);
            
            StyleRule[] styles = xtable.getBinding().getStyleRules();
            if( styles != null && styles.length > 0) {
                Map bean = new HashMap();
                bean.put("row", listItem.getRownum());
                bean.put("column", column);
                bean.put("columnName", colModel.getName());
                bean.put("root", listItem.getRoot());
                bean.put("item", listItem.getItem());
                applyStyle( xtable.getName(), bean, comp, styles, exprRes );
            }
            
            
            int borderPaddingLeft = 0;
            
            //reset icon
            if( comp instanceof JLabel ) {
                JLabel label = (JLabel) comp;
                label.setIcon(null);
            }
            
            if( column == 0 ) {
                boolean iconVisible = true;
                Icon icon = null;
                Icon toggleIcon = null;
                if( !ValueUtil.isEmpty(colModel.getIconVisibleWhen()) ) {
                    try {
                        Map ext = null;
                        if( !ValueUtil.isEmpty(xtable.getVarStatus()) ) {
                            ext = new HashMap();
                            ext.put(xtable.getVarStatus(), listItem);
                        }
                        Object bean = new BeanWrapper(listItem.getItem(), ext);
                        Object o = exprRes.evaluate(bean, colModel.getIconVisibleWhen());
                        iconVisible = Boolean.valueOf(o+"");
                    } catch(Exception e) {;}
                }
                
                if( iconVisible ) {
                    if( colModel.getIcon() == null && colModel.getToggleIcon() == null ) {
                        icon = new ImageIcon(getClass().getResource(COLLAPSED_ICON));
                        toggleIcon = new ImageIcon(getClass().getResource(EXPANDED_ICON));
                    }
                }
                
                if( colModel.getIcon() != null ) {
                    try {
                        URL u = clientCtx.getClassLoader().getResource( colModel.getIcon() );
                        icon = new ImageIcon(u);
                    } catch(Exception e) {}
                }
                
                if( colModel.getToggleIcon() != null ) {
                    try {
                        URL u = clientCtx.getClassLoader().getResource( colModel.getToggleIcon() );
                        toggleIcon = new ImageIcon(u);
                    } catch(Exception e) {}
                }
                
                
                Object rowData = listItem.getItem();
                if( comp instanceof JLabel ) {
                    JLabel label = (JLabel) comp;
                    label.setIconTextGap(5);
                    if( rowData != null && comp instanceof JLabel ) {
                        ItemStatus status = model.getStatus(rowData);
                        borderPaddingLeft = status.level * model.getIndentWidth();
                        
                        if( iconVisible ) {
                            if( status.expanded )
                                label.setIcon(toggleIcon);
                            else
                                label.setIcon(icon);
                        } else {
                            if ( icon != null ) {
                                borderPaddingLeft += icon.getIconWidth() + 5;
                            }
                            label.setIcon(null);
                        }
                    }
                }
            }
            
            //border support
            Border inner = BorderFactory.createEmptyBorder(CELL_MARGIN.top, CELL_MARGIN.left + borderPaddingLeft, CELL_MARGIN.bottom, CELL_MARGIN.right);
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
        
        private void applyStyle(String name, Map bean, Component comp, StyleRule[] styles, ExpressionResolver exprRes) {
            if ( styles == null ) return;
            
            if( name == null ) name = "_any_name";
            
            //apply style rules
            for(StyleRule r : styles) {
                String pattern = r.getPattern();
                if( !pattern.startsWith("table:") ) continue;
                
                pattern = pattern.substring(6);
                String rule = r.getExpression();
                
                //test expression
                boolean applyStyles = false;
                if ( rule!=null && name.matches(pattern) ){
                    try {
                        Object o = exprRes.evaluate(bean, rule);
                        applyStyles = Boolean.valueOf(o+"");
                    } catch (Exception ign){
                        System.out.println("STYLE RULE ERROR: " + ign.getMessage());
                    }
                }
                
                if ( applyStyles ) {
                    ControlSupport.setStyles( r.getProperties(), comp );
                }
            }
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
            ReportSheetTable tc = (ReportSheetTable) table;
            Column c = ((ReportSheetTableModel) tc.getModel()).getColumn(column);
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
            ReportSheetModel alm = ((ReportSheetTable) table).getListModel();
            if ( alm.getItemList().get(row).getItem() == null )
                return empty;
            
            return component;
        }
        
        public void refresh(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
            ReportSheetModel alm = ((ReportSheetTable) table).getListModel();
            if ( alm.getItemList().get(row).getItem() == null ) return;
            
            component.setSelected("true".equals(value+""));
        }
    }
    //</editor-fold>
    
}
