package com.rameses.rcp.control;

import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Windhel
 */

public class XDateField extends XTextField {
    
    private Date currentDate;
    private SimpleDateFormat inputFormatter;
    private SimpleDateFormat outputFormatter;
    private String outputFormat;
    private String inputFormat;
    private Date formattedDate;
    
    public XDateField() {
        setOutputFormat("yyyy-MM-dd");
        setInputFormat("yyyy-MM-dd");
        addFocusListener(new DateFieldSupport());
    }
    
    public Object getValue() {
        if( Beans.isDesignTime())
            return "";
        
        return formattedDate;
    }
    
    public void setValue(Object value) {
        if ( value instanceof KeyEvent ) {
            String text = ((KeyEvent) value).getKeyChar()+"";
            if ( text.matches("[\\d]")) {
                setText( text );
            }
        } else {
            if ( value != null ) {
                value = outputFormatter.format(value);
            }
            setText( value==null? "" : value.toString() );
        }
    }
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);
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
    
    public void validateInput() {
        ActionMessage actionMessage = getActionMessage();
        ControlProperty controlProperty = getControlProperty();
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
    
    //<editor-fold defaultstate="collapsed" desc="  DateFieldSupport (class)  ">
    private class DateFieldSupport implements FocusListener {
        
        public void focusGained(FocusEvent e) {
            showFormattedValue(false);
        }
        
        public void focusLost(FocusEvent e) {
            if ( e.isTemporary() ) return;
            
            showFormattedValue(true);
        }
        
    }
    //</editor-fold>
    
}
