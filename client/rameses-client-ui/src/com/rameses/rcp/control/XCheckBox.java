package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;

/**
 *
 * @author jaycverg
 */
public class XCheckBox extends JCheckBox implements UIInput, Containable {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private ControlProperty property = new ControlProperty();
    
    public XCheckBox() {
        
        addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                UIInputUtil.updateBeanValue(XCheckBox.this);
            }
        });
    }
    
    public Object getValue() {
        return isSelected();
    }
    
    public void setValue(Object value) {
        if ( value instanceof KeyEvent ) {
            refresh();
            setSelected(!isSelected());
        } else {
            setSelected("true".equals(value+""));
        }
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
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setSelected(Boolean.parseBoolean(value+""));
    }
    
    public void load() {
    }
    
    public int compareTo(Object o) {
        if ( o ==  null || !(o instanceof UIControl) ) return 0;
        return this.index - ((UIControl) o).getIndex();
    }
    
    public ControlProperty getControlProperty() {
        return property;
    }
    
}
