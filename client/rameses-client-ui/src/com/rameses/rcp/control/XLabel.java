package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author jaycverg
 */
public class XLabel extends JLabel implements UIControl, ActiveControl {
    
    private int index;
    private String[] depends = new String[]{};
    private Binding binding;
    private ControlProperty property = new ControlProperty();
    private String expression;
    
    /**
     * ActiveControl support fields/properties
     * this is used when this UIControl is used as a label for an ActiveControl
     */
    private String labelFor;
    private ControlProperty activeProperty;
    private JComponent activeComponent;
    private ActiveControlSupport activeControlSupport;
    private boolean formatted;
    private boolean dynamic;
    
    
    public XLabel() {
        super();
    }
    
    public void refresh() {
        Object value = null;
        dynamic = true;
        if ( !ValueUtil.isEmpty(expression) ) {
            value = UIControlUtil.evaluateExpr(binding.getBean(), expression);
        } else if ( !ValueUtil.isEmpty(getName()) ) {
            value = UIControlUtil.getBeanValue(this);
        } else {
            value = super.getText();
            dynamic = false;
        }
        
        if ( activeProperty != null ) {
            boolean req = activeProperty.isRequired();
            doSetText(( value != null? value+"" : ""), req);
            
        } else {
            super.setText(( value != null? value+"" : ""));
        }
    }
    
    public void load() {
        if ( !ValueUtil.isEmpty(labelFor) ) {
            UIControl c = binding.find(labelFor);
            if ( c instanceof ActiveControl ) {
                ActiveControl ac = (ActiveControl) c;
                if (ac instanceof JComponent) {
                    activeComponent = (JComponent) ac;
                }
                activeProperty = ac.getControlProperty();
                
                activeControlSupport = new ActiveControlSupport();
                activeProperty.addPropertyChangeListener(activeControlSupport);
            }
        }
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void doSetText(String text, boolean required) {
        StringBuffer sb = new StringBuffer(text);
        if ( required ) {
            if ( !formatted || dynamic ) {
                sb.insert(0, "<html>");
                sb.append(" <font color=\"red\">*</font>");
                sb.append("</html>");
                formatted = true;
            }
        } else {
            formatted = false;
        }
        super.setText(sb.toString());
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
    
    public String getCaption() {
        return property.getCaption();
    }
    
    public void setCaption(String caption) {
        property.setCaption(caption);
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
    
    public String getFor() {
        return labelFor;
    }
    
    public void setFor(String name) {
        this.labelFor = name;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  ActiveControlSupport (class)  ">
    private class ActiveControlSupport implements PropertyChangeListener {
        
        private Color oldFg;
                
        public void propertyChange(PropertyChangeEvent evt) {
            String propName = evt.getPropertyName();
            Object value = evt.getNewValue();
            
            if ( "required".equals(propName) ) {
                boolean req = Boolean.parseBoolean(value.toString());
                doSetText( XLabel.super.getText(), req);
            } else if ( "errorMessage".equals(propName) ) {
                String message = value != null? value.toString() : null;
                boolean error = !ValueUtil.isEmpty(message);
                if ( error ) {
                    oldFg = getForeground();
                    setForeground(Color.RED);
                } else {
                    setForeground(oldFg);
                }
                setToolTipText(message);
                if(activeComponent != null) {
                    activeComponent.setToolTipText(message);
                }
            }
        }
        
    }
    //</editor-fold>
}
