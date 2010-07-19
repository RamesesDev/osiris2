/*
 * XComboBox.java
 *
 * Created on June 26, 2010, 1:37 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ExpressionResolver;
import com.rameses.util.ValueUtil;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class XComboBox extends JComboBox implements UIInput, ItemListener, Validatable, Containable {
    
    private Binding binding;
    private int index;
    private String[] depends;
    private String caption;
    private String items;
    private boolean dynamic = false;
    private boolean isEnum = false;
    private String expression;
    private String emptyText = "-";
    private boolean allowNull = true;
    private ControlProperty property = new ControlProperty();
    private ActionMessage actionMessage = new ActionMessage();
    
    
    private DefaultComboBoxModel model;
    
    
    public XComboBox() {
        model = new DefaultComboBoxModel();
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
    
    //<editor-fold defaultstate="collapsed" desc="  buildList  ">
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
            //if type is null, happens when the source is a Map key
            //so try to use the classtype of the value if it is not null
            if ( type == null ) {
                Object value = UIControlUtil.getBeanValue(this);
                if ( value != null ) {
                    type = value.getClass();
                }
            }
            if ( type != null && type.isEnum()) {
                list = Arrays.asList(type.getEnumConstants());
                isEnum = true;
            }
        }
        
        if ( list == null ) return;
        
        if ( allowNull ) {
            addItem(null, emptyText);
        }
        
        ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
        for ( Object o: list ) {
            Object caption = null;
            if ( !ValueUtil.isEmpty(expression) ) {
                caption = er.evaluate(o, expression);
            }
            if ( caption == null ) caption = o;
            
            addItem(o, caption+"");
        }
    }
    
    private void addItem(Object value, String text) {
        ComboItem cbo = new ComboItem(value, text);
        model.addElement(cbo);
    }
    //</editor-fold>
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void itemStateChanged(ItemEvent e) {
        if ( e.getStateChange() == ItemEvent.SELECTED ) {
            UIInputUtil.updateBeanValue(this);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
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
        
        if ( value == null && !allowNull ) {
            ComboItem c = (ComboItem) getItemAt(0);
            model.setSelectedItem( c );
            UIInputUtil.updateBeanValue(this);
        } else {
            ComboItem c = new ComboItem( value );
            for(int i=0; i< getItemCount();i++) {
                ComboItem ci = (ComboItem) getItemAt(i);
                if( ci.equals(c)) {
                    model.setSelectedItem(ci);
                }
            }
        }
    }
    
    public boolean isNullWhenEmpty() {
        return true;
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
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    public boolean isAllowNull() {
        return allowNull;
    }
    
    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }
    
    public String getCaption() {
        return property.getCaption();
    }
    
    public void setCaption(String caption) {
        property.setCaption(caption);
    }
    
    public boolean isRequired() {
        return property.isRequired();
    }
    
    public void setRequired(boolean required) {
        property.setRequired(required);
    }
    
    public void validateInput() {
        actionMessage.clearMessages();
        property.setErrorMessage(null);
        if ( isRequired() && ValueUtil.isEmpty(getValue()) ) {
            actionMessage.clearMessages();
            actionMessage.addMessage("1001", "{0} is required.", new Object[] {getCaption()});
            property.setErrorMessage(actionMessage.toString());
        }
    }
    
    public ActionMessage getActionMessage() {
        return actionMessage;
    }
    
    public ControlProperty getControlProperty() {
        return property;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  ComboItem (class)  ">
    public static class ComboItem {
        private String text;
        private Object value;
        
        public ComboItem(Object v) {
            value = v;
        }
        
        public ComboItem(Object v , String t) {
            text = ValueUtil.isEmpty(t)? "": t;
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
