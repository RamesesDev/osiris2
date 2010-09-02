package com.rameses.rcp.control;

import bsh.This;
import com.rameses.rcp.support.DateDocument;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Windhel
 */

public class XDateField extends XTextField {
        
    private Date currentDate;
    private SimpleDateFormat inputFormatter;
    private SimpleDateFormat outputFormatter;
    private SimpleDateFormat valueFormatter;
    private String valueFormat;
    private String outputFormat;
    private String inputFormat;
    private Date date;
    private String formattedString;
    private int txtYPos;
    private int txtXPos;
    private String guideFormat;
    private char dateSeparator;
    
    private DateDocument dateDocument;
    private Document oldDocument;
    
        
    public XDateField() {
        setOutputFormat("yyyy-MM-dd");
        setInputFormat("yyyy-MM-dd");
        setValueFormat("yyyy-MM-dd");
        DateFieldSupport dateFieldSupport = new DateFieldSupport();
        addFocusListener(dateFieldSupport);
        guideFormat = getInputFormat();
        super.setShowHint(false);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  Getter / Setter  ">
    public Object getValue() {
        if( Beans.isDesignTime())
            return "";
        
        try {
            if ( !ValueUtil.isEmpty(getText()) ) {
                date = inputFormatter.parse(getText());
                formattedString = valueFormatter.format(date);
            }
        } catch(Exception e) {
            formattedString = null;
            ActionMessage actionMessage = getActionMessage();
            actionMessage.addMessage("", "Expected format for {0} is " + inputFormat, new Object[] {getCaption()});
            
            if(actionMessage.hasMessages())  {
                ControlProperty controlProperty = getControlProperty();
                controlProperty.setErrorMessage(actionMessage.toString());
            }
        }
        
        return formattedString;
    }
    
    public void setValue(Object value) {
        if ( value instanceof KeyEvent ) {
            String text = ((KeyEvent) value).getKeyChar()+"";
            if ( text.matches("[\\d]")) {
                setText( text );
            }
        } else {
            showFormattedValue(true);
        }
    }
        
    public String getOutputFormat() {
        return outputFormat;
    }
    
    public void setOutputFormat(String pattern) {
        this.outputFormat = pattern;
        if( !ValueUtil.isEmpty(pattern) )
            outputFormatter = new SimpleDateFormat(pattern);
        else
            outputFormatter = null;
    }
    
    public String getInputFormat() {
        return inputFormat;
    }
    
    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
        if( !ValueUtil.isEmpty(inputFormat) )
            inputFormatter = new SimpleDateFormat(inputFormat);
        else
            inputFormatter = null;
    }
    
    public String getValueFormat() {
        return valueFormat;
    }
    
    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
        if( !ValueUtil.isEmpty(valueFormat))
            valueFormatter = new SimpleDateFormat(valueFormat);
        else
            valueFormatter = null;
    }
    //</editor-fold>    
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);
        guideFormat = getInputFormat();
        for(char c : getInputFormat().toCharArray()) {
            if(c != 'y' && c != 'M' && c != 'd') {
                dateSeparator = c;                
                break;
            }
        }
        
        oldDocument = getDocument();
        dateDocument = new DateDocument(dateSeparator, inputFormat);
        super.setShowHint(false);
    }
    
    private final void showFormattedValue(boolean formatted) {
        try {
            Object value = UIControlUtil.getBeanValue(this);
            date = valueFormatter.parse(value.toString());
            if( formatted && outputFormatter !=null && value!=null ) {
                setText( outputFormatter.format(date) );
            } else {
                if( value == null )
                    setText("");
                else {
                    setText( inputFormatter.format(date) );
                }
            }
        } catch(Exception ex) {}
    }
    
    public void calculatePosition() {
        txtYPos = (int)(getHeight() /2) + (getInsets().top + (int)(getInsets().bottom / 2));
                
        if(super.getText().length() <= getInputFormat().length())
            guideFormat = getInputFormat().substring(super.getText().length());
        txtXPos = getInsets().left;
        for(int i = 0 ; i < super.getText().length() ; i++) {
            txtXPos = txtXPos + (getFontMetrics(getFont()).charWidth(super.getText().charAt(i)));
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(Beans.isDesignTime() == false) {
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(getFont());
            calculatePosition();
            g.drawString("" + guideFormat, txtXPos, txtYPos);
        }
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  DateFieldSupport (class)  ">
    private class DateFieldSupport implements FocusListener {
        
        public void focusGained(FocusEvent e){
            try {
                showFormattedValue(false);
                setDocument(dateDocument);
            }catch(Exception ex) { ex.printStackTrace(); }
        }
        
        public void focusLost(FocusEvent e) {
            if ( e.isTemporary() ) return;
            
            try{
                setDocument(oldDocument);
                showFormattedValue(true);
            }catch(Exception ex) { ex.printStackTrace(); }
        }        
    }
    //</editor-fold>
    
}
