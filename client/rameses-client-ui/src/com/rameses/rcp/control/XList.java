/*
 * XList.java
 *
 * Created on October 29, 2010, 10:59 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.common.ExpressionResolver;
import com.rameses.common.MethodResolver;
import com.rameses.common.PropertyResolver;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class XList extends JList implements UIControl, ListSelectionListener {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private String expression;
    private String items;
    private boolean dynamic;
    private Insets padding = new Insets(1,3,1,3);
    private String openAction;
    
    private DefaultListModel model;
    
    private int cellVerticalAlignment = SwingConstants.CENTER;
    private int cellHorizontalAlignment = SwingConstants.LEADING;
    
    
    public XList() {
        setCellRenderer(new DefaultCellRenderer());
        setMultiselect(false);
        
        if ( Beans.isDesignTime() ) {
            setPreferredSize(new Dimension(80, 100));
            super.setModel(new javax.swing.AbstractListModel() {
                String[] strings = { "Item 1", "Item 2" };
                public int getSize() { return strings.length; }
                public Object getElementAt(int i) { return strings[i]; }
            });
        }
        else {
            registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openItem();
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
        }
    }
    
    public void setModel(ListModel model) {;}
    
    public void refresh() {
        if ( dynamic ) buildList();
        
        selectSelectedItems();
    }
    
    public void load() {
        model = new DefaultListModel();
        super.setModel(model);
        
        if ( !dynamic ) buildList();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void buildList() {
        removeListSelectionListener(this);
        
        if( ValueUtil.isEmpty(items) ) return;
        
        model.clear();
        
        List list = new ArrayList();
        try {
            Object value = UIControlUtil.getBeanValue(this, items);
            if ( value == null ) return;
            
            if ( value instanceof Collection )
                list.addAll( (Collection) value );
            else if ( value.getClass().isArray() ) {
                for(Object o: (Object[]) value) list.add( o );
            }
            
            if ( list.size() == 0 ) return;
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        int i = 0;
        for(Object o: list) {
            model.add(i++, o);
        }
        
        if ( !ValueUtil.isEmpty(getName()) )
            addListSelectionListener(this);
    }
    
    private void selectSelectedItems() {
        if ( ValueUtil.isEmpty(getName()) ) return;
        
        Object value = null;
        try {
            value = UIControlUtil.getBeanValue(this);
        } catch(Exception e) {
            if( ClientContext.getCurrentContext().isDebugMode() ) {
                e.printStackTrace();
            }
        }
        
        if ( value == null ) return;
        
        //set selected item(s)
        if ( isMultiselect() ) {
            List list = new ArrayList();
            if ( value instanceof Collection )
                list.addAll( (Collection) value );
            else if ( value.getClass().isArray() ) {
                for(Object o: (Object[]) value) list.add( o );
            }
            
            if ( list.size() == 0 ) return;
            
            List indices = new ArrayList();
            for( int i=0; i < model.getSize(); i++ ) {
                Object item = model.getElementAt(i);
                if ( list.remove( item ) ) indices.add(i);
            }
            if ( indices.size() == 0 ) return;
            
            ListSelectionModel sm = getSelectionModel();
            sm.clearSelection();
            int size = getModel().getSize();
            for(int i = 0; i < indices.size(); i++) {
                int idx = Integer.parseInt( indices.get(i)+"" );
                if ( idx < size) {
                    sm.addSelectionInterval(idx, idx);
                }
            }
            
        } else {
            setSelectedValue(value, true);
        }
    }
    //</editor-fold>
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void valueChanged(ListSelectionEvent e) {
        if ( getSelectedIndex() != -1 && !e.getValueIsAdjusting() ) {
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            if ( isMultiselect() ) {
                res.setProperty(binding.getBean(), getName(), getSelectedValues());
            } else {
                res.setProperty(binding.getBean(), getName(), getSelectedValue());
            }
            binding.notifyDepends(this);
        }
    }
    
    private void openItem() {
        try {
            if( ValueUtil.isEmpty(openAction) ) return;
            
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            Object outcome = mr.invoke(binding.getBean(), openAction, null, null);
            if( outcome == null ) return;

            binding.fireNavigation(outcome);
        }
        catch(Exception e) {
            if( ClientContext.getCurrentContext().isDebugMode() ) {
                e.printStackTrace();
            }
        }
    }

    protected void processMouseEvent(MouseEvent e) {
        if( e.getClickCount() == 2 && e.getID() == MouseEvent.MOUSE_PRESSED) {
            openItem();
            e.consume();
            return;
        }
        
        super.processMouseEvent(e);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    public String getItems() {
        return items;
    }
    
    public void setItems(String items) {
        this.items = items;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    
    public void setMultiselect(boolean multi) {
        if ( multi )
            setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        else
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public boolean isMultiselect() {
        return getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
    }
    
    public Insets getPadding() {
        return padding;
    }
    
    public void setPadding(Insets padding) {
        if ( padding == null ) padding = new Insets(0,0,0,0);
        this.padding = padding;
    }
    
    public int getCellVerticalAlignment() {
        return cellVerticalAlignment;
    }
    
    public void setCellVerticalAlignment(int cellVerticalAlignment) {
        this.cellVerticalAlignment = cellVerticalAlignment;
    }
    
    public int getCellHorizontalAlignment() {
        return cellHorizontalAlignment;
    }
    
    public void setCellHorizontalAlignment(int cellHorizontalAlignment) {
        this.cellHorizontalAlignment = cellHorizontalAlignment;
    }
    
    public String getOpenAction() {
        return openAction;
    }

    public void setOpenAction(String openAction) {
        this.openAction = openAction;
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="  DefaultCellRenderer (class)  ">
    private class DefaultCellRenderer implements ListCellRenderer {
        
        private JLabel cellLabel;
        
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if( cellLabel == null ) {
                cellLabel = new JLabel();
                cellLabel.setComponentOrientation(list.getComponentOrientation());
                cellLabel.setOpaque(true);
            }
            
            cellLabel.setBorder(BorderFactory.createEmptyBorder(padding.top, padding.left, padding.bottom, padding.right));
            cellLabel.setSize( list.getFixedCellWidth(), list.getFixedCellHeight() );
            cellLabel.setEnabled(list.isEnabled());
            cellLabel.setFont(list.getFont());
            cellLabel.setVerticalAlignment( getCellVerticalAlignment() );
            cellLabel.setHorizontalAlignment( getCellHorizontalAlignment() );
            
            if(isSelected) {
                cellLabel.setBackground(list.getSelectionBackground());
                cellLabel.setForeground(list.getSelectionForeground());
            } else {
                cellLabel.setBackground(list.getBackground());
                cellLabel.setForeground(list.getForeground());
            }
            
            if ( Beans.isDesignTime() ) {
                cellLabel.setText( value+"" );
                return cellLabel;
            }
            
            Object val = null;
            if( getExpression() != null ) {
                try {
                    ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
                    val = er.evaluate( value, getExpression());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else {
                val = value;
            }
            
            cellLabel.setText( (val == null? " " : val.toString()) );
            
            return cellLabel;
        }
    }
    //</editor-fold>

}
