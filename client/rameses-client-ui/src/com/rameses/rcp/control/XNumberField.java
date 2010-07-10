package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.PropertyResolver;
import com.rameses.util.ValueUtil;
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

public class XNumberField extends JTextField implements UIInput, Validatable, Containable{
    
    private Binding binding;
    private int index;
    private boolean nullWhenEmpty = true;
    private String[] depends;
    private Class fieldType;
    private String pattern;
    private DecimalFormat formatter;
    private boolean immediate;
    private ControlProperty controlProperty = new ControlProperty();
    private ActionMessage actionMessage = new ActionMessage();
    
    public XNumberField() {        
    }
    
    public Object getValue() {
        if( Beans.isDesignTime())
            return "";

        String value = getText();
        return convertValue(value);
    }
    
    public void setValue(Object value) {
        setText( value==null? "" : value.toString() );
    }
    
    private Class getFieldType() {
        if( fieldType == null ) {
            fieldType = UIControlUtil.getValueType(this, getName());
            if( pattern == null || pattern.trim().length()==0 ) {
                if( fieldType == BigDecimal.class || fieldType == Double.class) {
                    setPattern("#,##0.00");
                } else {
                    setPattern("#");
                }
            }
        }
        return fieldType;
    }
    
    private Object convertValue(String fieldText) {
        if( fieldText.trim().length() == 0 ) {
            return null;
        }
        Class fType = getFieldType();
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
        return fieldText;
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
        NumberFieldSupport nfs = new NumberFieldSupport();
        if( !immediate )
            setInputVerifier(UIInputUtil.VERIFIER);
        else
            nfs.setUpdateOnKeyRelease(true);
        
        addKeyListener(nfs);
        addFocusListener(nfs);
    }
    
    public int compareTo(Object o) {
        if(o == null || !(o instanceof UIControl) )
            return 0;
        
        UIControl u = (UIControl)o;
        return this.index - u.getIndex();
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
    
    private final void showFormattedValue(boolean formatted) {
        Object value = UIControlUtil.getBeanValue(this);                
        if( formatted && formatter !=null && value!=null ) {
            setText( formatter.format(value) );
        } else {
            if( value == null )
                setText("");
            else
                setText( value+"" );
        }
    }
        
    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
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
            actionMessage.addMessage("", "{0} is required", new Object[]{ getCaption() });
            controlProperty.setErrorMessage(actionMessage.toString());
        }
    }

    public ActionMessage getActionMessage() {
        return actionMessage;
    }

    public ControlProperty getControlProperty() {
        return controlProperty;
    }
    
    
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
        }

        public void focusLost(FocusEvent e) {
            showFormattedValue(true);
        }

        public void setUpdateOnKeyRelease(boolean updateOnKeyRelease) {
            this.updateOnKeyRelease = updateOnKeyRelease;
        }
    }
    //</editor-fold>
}
