package com.rameses.rcp.control;

import com.rameses.rcp.control.date.DatePickerModel;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.Beans;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Windhel
 */

public class XDateField extends AbstractIconedTextField {
    
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
    private boolean useDatePickerModel = false;
    private DatePickerModel dpm;
    
    public XDateField() {
        setOutputFormat("yyyy-MM-dd");
        setInputFormat("yyyy-MM-dd");
        setValueFormat("yyyy-MM-dd");
        DateFieldSupport dateFieldSupport = new DateFieldSupport();
        addFocusListener(dateFieldSupport);
        addKeyListener(dateFieldSupport);
        guideFormat = getInputFormat();
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  Getter / Setter  ">
    public Object getValue() {
        if(Beans.isDesignTime())
            return "";
        
        try {
            if(!ValueUtil.isEmpty(getText())) {
                if(hasFocus())
                    date = inputFormatter.parse(getText());
                else
                    date = outputFormatter.parse(getText());
                
                formattedString = valueFormatter.format(date);
                if ( !formattedString.equals(getText()) ) {
                    actionMessage.addMessage("", "Invalid date entered for {0}.", new Object[] {getCaption()});
                }
            }
        } catch(Exception e) {
            actionMessage.addMessage("", "Expected format for {0} is " + inputFormat, new Object[] {getCaption()});
        }
        
        if(actionMessage.hasMessages())  {
            formattedString = null;
            ControlProperty controlProperty = getControlProperty();
            controlProperty.setErrorMessage(actionMessage.toString());
        }
        
        return formattedString;
    }
    
    public void setValue(Object value) {
        if (value instanceof KeyEvent ){
            String text = ((KeyEvent) value).getKeyChar()+"";
            if (text.matches("[\\d]")) {
                setText( text );
            }
        } else {
            if (value != null) {
                try{
                    Date d = valueFormatter.parse(value.toString());
                    value = outputFormatter.format(d);
                }catch(Exception ex) { ex.printStackTrace(); }
            }
            setText(value==null? "" : value + "");
        }
    }
    
    public String getOutputFormat() {
        return outputFormat;
    }
    
    public void setOutputFormat(String pattern) {
        this.outputFormat = pattern;
        if(!ValueUtil.isEmpty(pattern))
            outputFormatter = new SimpleDateFormat(pattern);
        else
            outputFormatter = null;
    }
    
    public String getInputFormat() {
        return inputFormat;
    }
    
    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
        if(!ValueUtil.isEmpty(inputFormat))
            inputFormatter = new SimpleDateFormat(inputFormat);
        else
            inputFormatter = null;
    }
    
    public String getValueFormat() {
        return valueFormat;
    }
    
    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
        if(!ValueUtil.isEmpty(valueFormat))
            valueFormatter = new SimpleDateFormat(valueFormat);
        else
            valueFormatter = null;
    }
    
    public boolean isUseDatePickerModel() {
        return useDatePickerModel;
    }
    
    public void setUseDatePickerModel(boolean useDatePickerModel) {
        this.useDatePickerModel = useDatePickerModel;
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
        
        if(isUseDatePickerModel()) {
            dpm = new DatePickerModel(this);
            super.setIcon("com/rameses/rcp/icons/search.png");
        }
    }
    
    private final void showFormattedValue(boolean formatted) {
        try{
            if( formatted && outputFormatter !=null && !ValueUtil.isEmpty(getText())) {
                date = inputFormatter.parse(getText());
                setText(outputFormatter.format(date));
            } else {
                date = outputFormatter.parse(getText());
                if(ValueUtil.isEmpty(getText()))
                    setText("");
                else
                    setText(inputFormatter.format(date));
            }
        }catch(Exception ex) {}
    }
    
    public void calculatePosition() {
        txtYPos = (int)(getHeight() /2) + (getInsets().top + (int)(getInsets().bottom / 2));
        
        if(super.getText().length() <= getInputFormat().length())
            guideFormat = getInputFormat().substring(super.getText().length());
        txtXPos = getInsets().left;
        for(int i = 0 ; i < super.getText().length() ; i++) {
            txtXPos = txtXPos + (getFontMetrics(getFont()).charWidth(super.getText().charAt(i)));
        }
        
        guideFormat = guideFormat.toUpperCase();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(Beans.isDesignTime())
            return;
        
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(getFont());
        calculatePosition();
        g.drawString(guideFormat, txtXPos, txtYPos);
    }
    
    public void actionPerformed() {
        if(isUseDatePickerModel()) {
            dpm.showCalendar();
        }
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  DateFieldSupport (class)  ">
    private class DateFieldSupport implements FocusListener, KeyListener {
        
        public void focusGained(FocusEvent e){
            try {
                showFormattedValue(false);
            }catch(Exception ex) { ex.printStackTrace(); }
        }
        
        public void focusLost(FocusEvent e) {
            if ( e.isTemporary() ) return;
            
            try{
                showFormattedValue(true);
                guideFormat = "";
                if(ValueUtil.isEmpty(getText())) {
                    setText(null);
                    setValue(null);
                }
            }catch(Exception ex) { ex.printStackTrace(); }
        }
        
        public void keyTyped(KeyEvent e) {}
        
        public void keyPressed(KeyEvent e) {
            if(e.getKeyChar() != dateSeparator &&
                    e.getKeyChar() != KeyEvent.VK_BACK_SPACE &&
                    e.getKeyChar() != KeyEvent.VK_HOME &&
                    e.getKeyChar() != KeyEvent.VK_END &&
                    e.getKeyChar() != KeyEvent.VK_LEFT &&
                    e.getKeyChar() != KeyEvent.VK_KP_RIGHT &&
                    e.getKeyChar() != KeyEvent.VK_KP_LEFT &&
                    e.getKeyCode() != 37 &&
                    e.getKeyCode() != 39) {
                if(getInputFormat().length() > getText().length()) {
                    if(getInputFormat().charAt(getText().length()) == dateSeparator)
                        setText(getText() + dateSeparator);
                }
            }
        }
        
        public void keyReleased(KeyEvent e) {}
        
    }
    //</editor-fold>
    
}
