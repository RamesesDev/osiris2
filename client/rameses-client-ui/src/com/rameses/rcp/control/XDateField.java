package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.Beans;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFormattedTextField;

/**
 *
 * @author Windhel
 */

public class XDateField extends JFormattedTextField implements UIInput, Validatable, Containable {
    
    private boolean nullWhenEmpty = true;
    private String[] depends;
    private int index;
    private Binding binding;
    private Date currentDate;
    private SimpleDateFormat inputFormatter;
    private SimpleDateFormat outputFormatter;
    private String outputFormat;
    private String inputFormat;
    private ControlProperty controlProperty = new ControlProperty();
    private ActionMessage actionMessage = new ActionMessage();
    private Date formattedDate;
    
    public XDateField() {
        setOutputFormat("yyyy-MM-dd");
        setInputFormat("yyyy-MM-dd");
    }
    
    public Object getValue() {
        if( Beans.isDesignTime())
            return "";
        
        return formattedDate;
    }
    
    public void setValue(Object value) {
        value = outputFormatter.format(value);
        setText( value==null? "" : value.toString() );
    }
    
    public boolean isNullWhenEmpty() {
        return nullWhenEmpty;
    }
    
    public void setNullWhenEmpty(boolean nullWhenEmpty) {
        this.nullWhenEmpty = nullWhenEmpty;
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
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);
        
        addFocusListener(new DateFieldSupport());
    }
    
    public int compareTo(Object o) {
        if( o == null || !(o instanceof UIControl) )
            return 0;
        
        UIControl u = (UIControl)o;
        return this.index - u.getIndex();
    }
    
    public String getOutputFormat() {
        return outputFormat;
    }
    
    public void setOutputFormat(String pattern) {
        this.outputFormat = pattern;
        if( !ValueUtil.isEmpty(pattern) ) {
            outputFormatter = new SimpleDateFormat(pattern);
        } else {
            outputFormatter = null;
        }
    }
    
    public String getInputFormat() {
        return inputFormat;
    }
    
    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
        if( !ValueUtil.isEmpty(inputFormat) ) {
            inputFormatter = new SimpleDateFormat(inputFormat);
        } else {
            inputFormatter = null;
        }
    }
    
    private final void showFormattedValue(boolean formatted) {
        Object value = UIControlUtil.getBeanValue(this);
        if( formatted && outputFormatter !=null && value!=null ) {
            setText( outputFormatter.format(value));
        } else {
            if( value == null )
                setText("");
            else
                setText( inputFormatter.format(value).toString() );
        }
    }
    
    public String getCaption() {
        return controlProperty.getCaption();
    }
    
    public void setCaption(String caption) {
        controlProperty.setCaption(caption);
    }
    
    public boolean isRequired() {
        return controlProperty.isRequired();
    }
    
    public void setRequired(boolean required) {
        controlProperty.setRequired(required);
    }
    
    public void validateInput() {
        actionMessage.clearMessages();
        controlProperty.setErrorMessage(null);
        if( isRequired() && ValueUtil.isEmpty(getText()) ) {
            actionMessage.addMessage("", "{0} is required", new Object[] {getCaption()});
        }
        try {
            formattedDate = inputFormatter.parse(getText());
        } catch(Exception e) {
            formattedDate = null;
            if(isRequired()) {
                actionMessage.addMessage("", "Expected format for {0} is " + inputFormat, new Object[] {getCaption()});
            }
        }
        
        if(actionMessage.hasMessages())
            controlProperty.setErrorMessage(actionMessage.toString());
    }
    
    public ActionMessage getActionMessage() {
        return actionMessage;
    }
    
    public ControlProperty getControlProperty() {
        return controlProperty;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  DateFieldSupport (class)  ">
    private class DateFieldSupport implements FocusListener {
        
        public void focusGained(FocusEvent e) {
            showFormattedValue(false);
        }
        
        public void focusLost(FocusEvent e) {
            showFormattedValue(true);
        }
        
    }
    //</editor-fold>
    
}
