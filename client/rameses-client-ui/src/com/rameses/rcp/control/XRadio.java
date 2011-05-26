/*
 * XRadio.java
 *
 * Created on July 27, 2010, 2:23 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.support.ThemeUI;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;


public class XRadio extends JRadioButton implements UIInput, ItemListener, ActiveControl {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private boolean readonly;
    
    private Object optionValue;
    private ButtonGroup buttonGroup;
    private ControlProperty property = new ControlProperty();
    
    
    public XRadio() {
        addItemListener(this);
        
        //default font
        Font f = ThemeUI.getFont("XRadio.font");
        if ( f != null ) setFont( f );
    }
    
    
    public void refresh() {
        try {
            Object value = UIControlUtil.getBeanValue(this);
            setValue( value );
            if( !readonly && !isFocusable() ) setReadonly(false);
        } catch(Exception e) {
            //disable when there is error
            setEnabled(false);
            setFocusable(false);
            
            if( ClientContext.getCurrentContext().isDebugMode() ) {
                e.printStackTrace();
            }
        }
    }
    
    public void load() {
        String name = getName();
        if( !ValueUtil.isEmpty(name) ) {
            Map<String, ButtonGroup> m = (Map)binding.getProperties().get(ButtonGroup.class);
            if( m == null ) {
                m = new HashMap<String, ButtonGroup>();
                binding.getProperties().put(ButtonGroup.class, m );
            }
            
            if(!m.containsKey(name)) {
                m.put(name, new ButtonGroup());
            }
            
            buttonGroup = m.get(name);
            buttonGroup.add(this);
        }
    }
    
    public void itemStateChanged(ItemEvent e) {
        if ( e.getStateChange() == ItemEvent.SELECTED ) {
            UIInputUtil.updateBeanValue(this);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setValue(Object value) {
        if ( value != null && value.equals(optionValue) ) {
            setSelected(true);
        }
    }
    
    public Object getValue() {
        if ( isSelected() ) {
            return optionValue;
        } else {
            return null;
        }
    }
    
    public boolean isNullWhenEmpty() { return true; }
    
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
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public Object getOptionValue() {
        return optionValue;
    }
    
    public void setOptionValue(Object optionValue) {
        this.optionValue = optionValue;
    }
    
    public ControlProperty getControlProperty() {
        return property;
    }
    
    public String getCaption() {
        return property.getCaption();
    }
    
    public void setCaption(String caption) {
        property.setCaption( caption );
    }
    
    public char getCaptionMnemonic() {
        return property.getCaptionMnemonic();
    }
    
    public void setCaptionMnemonic(char c) {
        property.setCaptionMnemonic(c);
    }
    
    public boolean isShowCaption() {
        return property.isShowCaption();
    }
    
    public void setShowCaption(boolean show) {
        property.setShowCaption(show);
    }
    
    public int getCaptionWidth() {
        return property.getCaptionWidth();
    }
    
    public void setCaptionWidth(int width) {
        property.setCaptionWidth(width);
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
    
    public boolean isImmediate() {
        return true;
    }
    //</editor-fold>
    
}
