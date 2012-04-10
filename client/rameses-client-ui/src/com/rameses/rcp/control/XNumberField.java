package com.rameses.rcp.control;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.Beans;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.JTextField;

/**
 *
 * @author Windhel
 */

public class XNumberField extends XTextField {
    
    private Class fieldType;
    private String pattern;
    private DecimalFormat formatter;
    private boolean immediate;
    
    NumberFieldSupport fieldSupport = new NumberFieldSupport();
    
    
    public XNumberField() {
        setHorizontalAlignment(JTextField.RIGHT);
        addKeyListener(fieldSupport);
        addFocusListener(fieldSupport);
    }
    
    public void refresh() {
        try {
            if( !isReadonly() && !isFocusable() ) setReadonly(false);
            
            showFormattedValue(true);
        } catch(Exception e) {
            //block input is name is null
            setText("");
            setEditable(false);
            setFocusable(false);
            
            if( ClientContext.getCurrentContext().isDebugMode() ) {
                e.printStackTrace();
            }
        }
    }
    
    public void load() {
        if( !immediate )
            setInputVerifier(UIInputUtil.VERIFIER);
        else
            fieldSupport.setUpdateOnKeyRelease(true);
        
        //calculate field type if not specified
        getFieldType();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private Object convertValue() {
        Class fType = getFieldType();
        String fieldText = getText().replace(",", "");
        
        if( fieldText.trim().length() == 0 ) {
             return (fType != null && fType.isPrimitive()) ? 0 : null;
        }
        
        try {
            if(fType == BigDecimal.class) {
                return new BigDecimal(fieldText);
            } else if(fType == Integer.class) {
                return new Integer(fieldText);
            } else if(fType == Double.class) {
                return new Double(fieldText);
            } else if(fType == int.class) {
                return Integer.parseInt(fieldText);
            } else if(fType == Long.class) {
                return new Long(fieldText);
            } else if(fType == long.class) {
                return Long.parseLong(fieldText);
            } else if(fType == double.class) {
                return Double.parseDouble(fieldText);
            }
        } 
        catch(NumberFormatException nfe) {
            return (fType != null && fType.isPrimitive()) ? 0 : null;
        }
        
        return fieldText;
    }
    
    private final void showFormattedValue(boolean formatted) {
        Object value = UIControlUtil.getBeanValue(this);
        if( formatted && formatter !=null && value!=null ) {
            setValue( formatter.format(value) );
        } else {
            if( value == null )
                setValue("");
            else
                setValue( value+"" );
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public Object getValue() {
        if( Beans.isDesignTime())
            return "";
        
        return convertValue();
    }
    
    public void setValue(Object value) {
        if ( value instanceof KeyEvent ) {
            String text = ((KeyEvent) value).getKeyChar()+"";
            if ( text.matches("[^\\d|\\.]")) return;
        }
        
        super.setValue(value);
    }
    
    private Class getFieldType() {
        if( fieldType == null ) {
            fieldType = UIControlUtil.getValueType(this, getName());
            //this happens if a field is from a Map (map key)
            if ( fieldType == null ) {
                Object value = UIControlUtil.getBeanValue(this);
                if ( value != null ) {
                    fieldType = value.getClass();
                }
            }
            if( pattern == null || pattern.trim().length()==0 ) {
                if( fieldType == BigDecimal.class || fieldType == Double.class || fieldType == double.class ) {
                    setPattern("#,##0.00");
                } else {
                    setPattern("#,##0");
                }
            }
        }
        return fieldType;
    }
    
    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
        if( pattern !=null && pattern.trim().length()>0 ) {
            formatter = new DecimalFormat(pattern);
        } else {
            formatter = null;
        }
    }
    
    public boolean isImmediate() {
        return immediate;
    }
    
    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  NumberFieldKeyListener (class)  ">
    private class NumberFieldSupport implements KeyListener, FocusListener {
        
        private boolean updateOnKeyRelease;
        
        public void keyTyped(KeyEvent e) {
            char k = e.getKeyChar();
            boolean acceptable = false;
            switch(k) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '0':
                case '.':
                case '-':
                    acceptable = true;
            }
            if( !acceptable )
                e.consume();
        }
        
        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {
            if( updateOnKeyRelease )
                UIInputUtil.updateBeanValue(XNumberField.this);
        }
        
        public void focusGained(FocusEvent e) {
            showFormattedValue(false);
            selectAll();
        }
        
        public void focusLost(FocusEvent e) {
            if ( e.isTemporary() ) return;
            
            showFormattedValue(true);
        }
        
        public void setUpdateOnKeyRelease(boolean updateOnKeyRelease) {
            this.updateOnKeyRelease = updateOnKeyRelease;
        }
    }
    //</editor-fold>
    
}
