package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author jaycverg
 */
public class XLabel extends JLabel implements UIControl, ActiveControl {
    
    private int index;
    private String[] depends;
    private Binding binding;
    private ControlProperty property = new ControlProperty();
    private String expression;
    private Insets padding;
    
    private Border origBorder;
    
    /**
     * ActiveControl support fields/properties
     * this is used when this UIControl is used as a label for an ActiveControl
     */
    private String labelFor;
    private boolean addCaptionColon = true;
    private boolean forceUseActiveCaption;
    private ControlProperty activeProperty;
    private JComponent activeComponent;
    private ActiveControlSupport activeControlSupport;
    
    
    public XLabel() {
        super();
        setPadding(new Insets(1,3,1,1));
    }
    
    public XLabel(boolean forceUseActiveCaption) {
        this();
        this.forceUseActiveCaption = forceUseActiveCaption;
    }
    
    public void refresh() {
        Object value = null;
        if ( !ValueUtil.isEmpty(expression) ) {
            value = UIControlUtil.evaluateExpr(binding.getBean(), expression);
        } else if ( !ValueUtil.isEmpty(getName()) ) {
            value = UIControlUtil.getBeanValue(this);
        } else {
            value = super.getText();
        }
        
        super.setText(( value != null? value+"" : ""));
    }
    
    public void load() {
        if ( !ValueUtil.isEmpty(labelFor) ) {
            UIControl c = binding.find(labelFor);
            if ( c == null ) return;
            if (c instanceof JComponent) {
                this.setLabelFor((JComponent) c);
            }
        }
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    private void formatText(String text, boolean required) {
        StringBuffer sb = new StringBuffer(text);
        if ( addCaptionColon && !ValueUtil.isEmpty(text) ) {
            sb.append(" :");
        }
        if ( required ) {
            sb.insert(0, "<html>");
            sb.append(" <font color=\"red\">*</font>");
            sb.append("</html>");
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
    
    public void setText(String text) {
        setExpression(text);
    }
    
    public void setBorder(Border border) {
        origBorder = border;
        
        if ( padding != null ) {
            Border padBorder = BorderFactory.createEmptyBorder(padding.top, padding.left, padding.bottom, padding.right);
            Border b = BorderFactory.createCompoundBorder(origBorder, padBorder);
            super.setBorder(b);
        } else {
            super.setBorder(origBorder);
        }
    }
    
    public Border getBorder() {
        return origBorder;
    }
    
    public String getCaption() {
        return property.getCaption();
    }
    
    public void setCaption(String caption) {
        property.setCaption(caption);
    }
    
    public boolean isShowCaption() {
        return property.isShowCaption();
    }
    
    public void setShowCaption(boolean show) {
        property.setShowCaption(show);
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
    
    public void setLabelFor(Component c) {
        activeComponent = (JComponent) c;
        if ( c instanceof ActiveControl ) {
            ActiveControl ac = (ActiveControl) c;
            activeProperty = ac.getControlProperty();
            String acCaption = activeProperty.getCaption();
            if ( forceUseActiveCaption || (!ValueUtil.isEmpty(acCaption) && !acCaption.equals("Caption")) ) {
                setName(null);
                setExpression(null);
                formatText(activeProperty.getCaption(), activeProperty.isRequired());
                super.setDisplayedMnemonic(activeProperty.getCaptionMnemonic());
            }
            
            activeControlSupport = new ActiveControlSupport();
            activeProperty.addPropertyChangeListener(activeControlSupport);
        }
        super.setLabelFor(c);
    }
    
    public Insets getPadding() {
        return padding;
    }
    
    public void setPadding(Insets padding) {
        this.padding = padding;
        this.setBorder(origBorder);
    }
    
    public boolean isAddCaptionColon() {
        return addCaptionColon;
    }
    
    public void setAddCaptionColon(boolean addCaptionColon) {
        this.addCaptionColon = addCaptionColon;
        formatText( activeProperty.getCaption(), activeProperty.isRequired() );
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  ActiveControlSupport (class)  ">
    private class ActiveControlSupport implements PropertyChangeListener {
        
        private Color oldFg;
        
        public void propertyChange(PropertyChangeEvent evt) {
            String propName = evt.getPropertyName();
            Object value = evt.getNewValue();
            
            if ( "caption".equals(propName) ) {
                String text = (value == null)? "" : value+"";
                formatText( text, activeProperty.isRequired() );
                
            } else if ( "captionMnemonic".equals(propName) ) {
                setDisplayedMnemonic( (value+"").charAt(0) );
                
            } else if ( "required".equals(propName) ) {
                boolean req = Boolean.parseBoolean(value+"");
                formatText( activeProperty.getCaption(), req);
                
            } else if ( "errorMessage".equals(propName) ) {
                String message = (value != null)? value+"" : null;
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
