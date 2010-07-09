package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import javax.swing.JLabel;

/**
 *
 * @author jaycverg
 */
public class XLabel extends JLabel implements UIControl, Containable {
    
    private int index;
    private String[] depends = new String[]{};
    private Binding binding;
    private ControlProperty property = new ControlProperty();
    
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }

    public int getIndex() {
        return index;
    }
    
    public void setIndex(int idx) {
        index = idx;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
    }

    public Binding getBinding() {
        return binding;
    }

    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setText(( value != null? value+"" : ""));
    }
    
    public void load() {
    
    }

    public int compareTo(Object o) {
        if ( o == null || !(o instanceof UIControl)) return 0;
        return this.index - ((UIControl) o).getIndex();
    }

    public ControlProperty getControlProperty() {
        return property;
    }
    
}
