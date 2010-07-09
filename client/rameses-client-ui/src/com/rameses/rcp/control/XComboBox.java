/*
 * XComboBox.java
 *
 * Created on June 26, 2010, 1:37 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class XComboBox extends JComboBox implements UIInput, ItemListener {
    
    private Binding binding;
    private int index;
    private String[] depends;
    private boolean nullWhenEmpty = true;
    private String caption;
    private String items;
    private boolean dynamic = false;
    private boolean isEnum = false;
    
    private DefaultComboBoxModel model;
    
    
    public XComboBox() {
        model = new DefaultComboBoxModel();
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  UIControl Properties  ">
    public Object getValue() {
        if ( Beans.isDesignTime() ) return null;
        
        if( super.getSelectedItem() ==null ) {
            return null;
        }
        return ((ComboItem)super.getSelectedItem()).getValue();
    }
    
    public void setValue(Object value) {
        if ( Beans.isDesignTime() ) return;
        if ( value instanceof KeyEvent ) return;
        
        ComboItem c = new ComboItem( value );
        for(int i=0; i< getItemCount();i++) {
            ComboItem ci = (ComboItem) getItemAt(i);
            if( ci.equals(c)) {
                model.setSelectedItem(ci);
                return;
            }
        }
        model.setSelectedItem(null);
    }
    
    public boolean isNullWhenEmpty() {
        return nullWhenEmpty;
    }
    
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
    
    public void refresh() {
        if ( dynamic && !isEnum ) {
            buildList();
        }
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        super.setModel(model);
        if ( dynamic ) {
            super.addItemListener(this);
        } else {
            super.setInputVerifier(UIInputUtil.VERIFIER);
            buildList();
        }
    }
    
    private void buildList() {
        model.removeAllElements(); //clear combo model
        
        Object beanItems = UIControlUtil.getBeanValue(this, getItems());
        Collection list = null;
        Class type = null;
        isEnum = false;
        if ( beanItems != null ) {
            type = beanItems.getClass();
            if ( type.isArray() ) {
                list = Arrays.asList((Object[]) beanItems);
            } else if ( beanItems instanceof Collection ) {
                list = (Collection) beanItems;
            }
        } else {
            type = UIControlUtil.getValueType(this, getName());
            if ( type.isEnum()) {
                list = Arrays.asList(type.getEnumConstants());
                isEnum = true;
            }
        }
        
        if ( list == null ) return;
        
        for ( Object o: list ) {
            addItem(o, o+"");
        }
    }
    
    public int compareTo(Object o) {
        if ( o == null || !(o instanceof UIControl) ) return 0;
        return this.index - ((UIControl) o).getIndex();
    }
    //</editor-fold>
    
    public void addItem(Object value, String text) {
        ComboItem cbo = new ComboItem(value, text);
        model.addElement(cbo);
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
    
    public void itemStateChanged(ItemEvent e) {
        if ( e.getStateChange() == ItemEvent.SELECTED ) {
            UIInputUtil.updateBeanValue(this);
        }
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  ComboItem (class)  ">
    public static class ComboItem {
        private String text;
        private Object value;
        
        public ComboItem(Object v) {
            value = v;
        }
        
        public ComboItem(Object v , String t) {
            text = t;
            value = v;
        }
        
        public String toString() {
            return text;
        }
        
        public Object getValue() {
            return value;
        }
        
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof ComboItem)) return false;
            
            ComboItem ci = (ComboItem)o;
            if (value == null && ci.value == null)
                return true;
            else if (value != null && ci.value == null)
                return false;
            else if (value == null && ci.value != null)
                return false;
            else
                return value.equals(ci.value);
        }
        
    }
    //</editor-fold>
    
}
