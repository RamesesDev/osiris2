package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.beans.Beans;
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
    private String expression;
    
    
    public XLabel() {
        super();
    }
    
    public void refresh() {
        Object value = null;
        if ( !ValueUtil.isEmpty(expression)) {
            value = UIControlUtil.evaluateExpr(binding.getBean(), expression);
        } else {
            value = UIControlUtil.getBeanValue(this);
        }
        setText(( value != null? value+"" : ""));
    }
    
    public void load() {}
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public String getText() {
        if ( Beans.isDesignTime() ) {
            if ( !ValueUtil.isEmpty(expression) )
                return expression;
            else if ( !ValueUtil.isEmpty(getName()) )
                return getName();
            else
                return super.getText();
            
        } else {
            return super.getText();
        }
    }
    
    public void setName(String name) {
        super.setName(name);
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
    
    public void setIndex(int idx) {
        index = idx;
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
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    //</editor-fold>
    
}
