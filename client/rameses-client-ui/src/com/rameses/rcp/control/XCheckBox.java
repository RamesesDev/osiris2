package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.support.ThemeUI;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.Beans;
import java.util.EventObject;
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
    private boolean readonly;
    
    
    public XCheckBox() {
        //default font
        Font f = ThemeUI.getFont("XCheckBox.font");
        if ( f != null ) setFont( f );
    }
    
    public void refresh() {
        try {
            if( !isReadonly() && !isFocusable() ) setReadonly(false);
            
            Object value = UIControlUtil.getBeanValue(this);
            setValue(value);
        } catch(Exception e) {
            setSelected(false);
            setFocusable(false);
            setEnabled(false);
            
            if( ClientContext.getCurrentContext().isDebugMode() ) {
                e.printStackTrace();
            }
        }
    }
    
    public void load() {
        addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                try {
                    UIControlUtil.getBeanValue(XCheckBox.this); //check if name is not null
                    UIInputUtil.updateBeanValue(XCheckBox.this);
                } catch(Exception ex){;}
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
        if ( value instanceof EventObject ) {
            refresh();
            setSelected(!isSelected());
        } else {
            boolean isCheck = getCheckValue().equals(value);
            boolean isUncheck = getUncheckValue().equals(value);
            if ( !isCheck ) {
                setSelected(false);
                if ( !isUncheck ) {
                    UIInputUtil.updateBeanValue(this, false, false);
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
    
    public char getCaptionMnemonic() {
        return property.getCaptionMnemonic();
    }
    
    public void setCaptionMnemonic(char c) {
        property.setCaptionMnemonic(c);
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
    
    public Font getCaptionFont() {
        return property.getCaptionFont();
    }
    
    public void setCaptionFont(Font f) {
        property.setCaptionFont(f);
    }
    
    public Insets getCellPadding() {
        return property.getCellPadding();
    }
    
    public void setCellPadding(Insets padding) {
        property.setCellPadding(padding);
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
