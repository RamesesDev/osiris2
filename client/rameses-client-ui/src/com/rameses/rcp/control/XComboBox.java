/*
 * XComboBox.java
 *
 * Created on June 26, 2010, 1:37 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.common.PropertyResolver;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.support.ThemeUI;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.common.ExpressionResolver;
import com.rameses.util.ValueUtil;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class XComboBox extends JComboBox implements UIInput, ItemListener, Validatable, ActiveControl {
    
    protected Binding binding;
    
    private int index;
    private String[] depends;
    private String caption;
    private String items;
    private boolean immediate;
    private boolean dynamic;
    private String expression;
    private String emptyText = "-";
    private boolean allowNull = true;
    private ControlProperty property = new ControlProperty();
    private ActionMessage actionMessage = new ActionMessage();
    private Class fieldType;
    private boolean readonly;
    private String itemKey;
    
    protected DefaultComboBoxModel model;
    private boolean updating;
    
    
    public XComboBox() {
        initComponents();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initComponents method  ">
    private void initComponents() {
        UIManager.put("ComboBox.disabledForeground", getForeground());
        if ( Beans.isDesignTime() ) {
            model = new DefaultComboBoxModel(new Object[]{"Item 1"});
            super.setModel( model );
        }
        
        //default font
        Font f = ThemeUI.getFont("XComboBox.font");
        if ( f != null ) setFont( f );
    }
    //</editor-fold>
    
    public void refresh() {
        if ( dynamic ) {
            buildList();
        }
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        model = new DefaultComboBoxModel();
        super.setModel(model);
        
        if ( !dynamic ) {
            buildList();
        }
        
        if ( immediate ) {
            super.addItemListener(this);
        } else {
            super.setInputVerifier(new InputVerifier() {
                public boolean verify(JComponent input) {
                    if ( isPopupVisible() ) return true;
                    return UIInputUtil.VERIFIER.verify(input);
                }
            });
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="  buildList  ">
    private Collection fetchItems() {
        Object beanItems = UIControlUtil.getBeanValue(this, getItems());
        Collection list = null;
        Class type = null;
        if ( beanItems != null ) {
            type = beanItems.getClass();
            if ( type.isArray() ) {
                list = Arrays.asList((Object[]) beanItems);
            } else if ( beanItems instanceof Collection ) {
                list = (Collection) beanItems;
            }
        } else {
            if ( fieldType != null )
                type = fieldType;
            else
                type = UIControlUtil.getValueType(this, getName());
            
            //if type is null, happens when the source is a Map key and no fieldType supplied
            //try to use the classtype of the value if it is not null
            if ( type == null ) {
                Object value = UIControlUtil.getBeanValue(this);
                if ( value != null ) {
                    type = value.getClass();
                }
            }
            if ( type != null && type.isEnum()) {
                list = Arrays.asList(type.getEnumConstants());
            }
        }
        return list;
    }
    
    private void buildList() {
        updating = true;
        model.removeAllElements(); //clear combo model
        
        if ( allowNull ) {
            addItem(null, emptyText);
        }
        
        Collection list = fetchItems();
        if ( list == null ) return;
        
        ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
        for ( Object o: list ) {
            Object caption = null;
            if ( !ValueUtil.isEmpty(expression) ) {
                caption = er.evaluate(o, expression);
            }
            if ( caption == null ) caption = o;
            
            addItem(o, caption+"");
        }
        SwingUtilities.updateComponentTreeUI(this);
        updating = false;
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
        if ( e.getStateChange() == ItemEvent.SELECTED && !updating ) {
            UIInputUtil.updateBeanValue(this);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setName(String name) {
        super.setName(name);
        
        if ( Beans.isDesignTime() ) {
            model.removeAllElements();
            model.addElement(name);
        }
    }
    
    public Object getValue() {
        if ( Beans.isDesignTime() ) return null;
        
        if( super.getSelectedItem() == null ) {
            return null;
        }
        Object value = ((ComboItem)super.getSelectedItem()).getValue();
        if ( value != null && !ValueUtil.isEmpty(itemKey) ) {
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            value = res.getProperty(value, itemKey);
        }
        
        return value;
    }
    
    public void setValue(Object value) {
        if ( Beans.isDesignTime() ) return;
        if ( value instanceof KeyEvent ) return;
        
        if ( value == null && !allowNull ) {
            ComboItem c = (ComboItem) getItemAt(0);
            model.setSelectedItem( c );
            UIInputUtil.updateBeanValue(this);
        } else {
            for(int i=0; i< getItemCount();i++) {
                ComboItem ci = (ComboItem) getItemAt(i);
                if( isSelected(ci, value) ) {
                    model.setSelectedItem(ci);
                    break;
                }
            }
        }
    }
    
    protected boolean isSelected(ComboItem ci, Object value) {
        if ( value != null && !ValueUtil.isEmpty(itemKey) ) {
            if ( ci.getValue() == null ) return false;
            
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            Object key = res.getProperty(ci.getValue(), itemKey);
            return key != null && value.equals(key);
            
        } else {
            ComboItem c = new ComboItem( value );
            return ci.equals(c);
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
    
    public char getCaptionMnemonic() {
        return property.getCaptionMnemonic();
    }
    
    public void setCaptionMnemonic(char c) {
        property.setCaptionMnemonic(c);
    }
    
    public boolean isRequired() {
        return property.isRequired();
    }
    
    public void setRequired(boolean required) {
        property.setRequired(required);
    }

    public int getCaptionWidth() {
        return property.getCaptionWidth();
    }
    
    public void setCaptionWidth(int width) {
        property.setCaptionWidth(width);
    }
    
    public boolean isShowCaption() {
        return property.isShowCaption();
    }
    
    public void setShowCaption(boolean showCaption) {
        property.setShowCaption(showCaption);
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
    
    public Class getFieldType() {
        return fieldType;
    }
    
    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }
    
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        setEnabled(!readonly);
        setFocusable(!readonly);
    }
    
    public boolean isReadonly() {
        return readonly;
    }
    
    public void setRequestFocus(boolean focus) {
        if ( focus ) requestFocus();
    }
    
    public boolean isImmediate() {
        return immediate;
    }
    
    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }
    
    public String getItemKey() {
        return itemKey;
    }
    
    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }
    
    public String getEmptyText() {
        return emptyText;
    }
    
    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  ComboItem (class)  ">
    public class ComboItem {
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
            if (value == null && ci.value == null) {
                return true;
            } else if (value != null && ci.value == null) {
                return false;
            } else if (value == null && ci.value != null) {
                return false;
            }
            
            return value.equals(ci.value);
        }
        
    }
    //</editor-fold>
    
}
