package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import javax.swing.JCheckBox;

/**
 *
 * @author jaycverg
 */
public class XCheckBox extends JCheckBox implements UIInput, ActiveControl {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private Object checkValue = true;
    private Object uncheckValue = false;
    private ControlProperty property = new ControlProperty();
    private String onAfterUpdate;
    private boolean readonly;
    
    
    public XCheckBox() {}
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                UIInputUtil.updateBeanValue(XCheckBox.this);
            }
        });
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public Object getValue() {
        return isSelected()? getCheckValue() : getUncheckValue();
    }
    
    public void setValue(Object value) {
        if ( value instanceof KeyEvent ) {
            refresh();
            setSelected(!isSelected());
        } else {
            boolean isCheck = getCheckValue().equals(value);
            boolean isUncheck = getUncheckValue().equals(value);
            if ( !isCheck ) {
                setSelected(false);
                if ( !isUncheck ) {
                    UIInputUtil.updateBeanValue(this, false);
                }
            } else {
                setSelected(true);
            }
        }
    }
    
    public String getCaption() {
        return property.getCaption();
    }
    
    public void setCaption(String caption) {
        property.setCaption(caption);
    }
    
    public boolean isNullWhenEmpty() {
        return false;
    }
    
    public String[] getDepends() {
        return this.depends;
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
    
    public ControlProperty getControlProperty() {
        return property;
    }
    
    public Object getCheckValue() {
        return checkValue;
    }
    
    public void setCheckValue(Object checkValue) {
        if ( !Beans.isDesignTime() && isExpression(checkValue) ) {
            checkValue = UIControlUtil.evaluateExpr(binding.getBean(), checkValue+"");
        }
        this.checkValue = checkValue;
    }
    
    public Object getUncheckValue() {
        return uncheckValue;
    }
    
    public void setUncheckValue(Object uncheckValue) {
        if ( !Beans.isDesignTime() && isExpression(uncheckValue) ) {
            uncheckValue = UIControlUtil.evaluateExpr(binding.getBean(), uncheckValue+"");
        }
        this.uncheckValue = uncheckValue;
    }
    
    private boolean isExpression(Object exp) {
        if ( exp == null || !(exp instanceof String) ) return false;
        String expr = exp.toString();
        return expr.matches(".*#\\{[^\\{\\}]+\\}.*");
    }
    
    public String getOnAfterUpdate() {
        return onAfterUpdate;
    }
    
    public void setOnAfterUpdate(String onAfterUpdate) {
        this.onAfterUpdate = onAfterUpdate;
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
        return true;
    }
    //</editor-fold>
    
}
