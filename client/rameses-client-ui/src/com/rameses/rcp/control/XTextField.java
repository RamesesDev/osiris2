package com.rameses.rcp.control;

import com.rameses.rcp.constant.TextCase;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.support.TextDocument;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 *
 * @author jaycverg
 */
public class XTextField extends JTextField implements UIInput, Validatable, ActiveControl {
    
    private int index;
    private String[] depends = new String[]{};
    private Binding binding;
    private boolean nullWhenEmpty = true;
    private boolean readonly;
    private boolean showHint;
    private String hint;
    private boolean isHintShown;
    private int hintYPos;
    private int hintXPos;
    
    private TextDocument document = new TextDocument();
    
    protected ControlProperty property = new ControlProperty();
    protected ActionMessage actionMessage = new ActionMessage();
    
    public XTextField() {
        document.setTextCase(TextCase.UPPER);
        XTextFieldSupport xTextFieldSupport = new XTextFieldSupport();
        addFocusListener(xTextFieldSupport);
    }
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);
        setDocument(document);
        if(showHint)
            isHintShown = true;
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void validateInput() {
        actionMessage.clearMessages();
        property.setErrorMessage(null);
        if ( isRequired() && ValueUtil.isEmpty( getText() ) ) {
            actionMessage.addMessage("1001", "{0} is required.", new Object[] {getCaption()});
            property.setErrorMessage(actionMessage.toString());
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if( showHint ) {
            if( isHintShown ) {
                g.setColor(Color.LIGHT_GRAY);
                g.setFont(getFont());
                hintYPos = (int)(getHeight() /2) + (getInsets().top + (int)(getInsets().bottom / 2));
                hintXPos = getInsets().left;
                g.drawString(" " + getHint(), hintXPos, hintYPos);
            }
        }
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setName(String name) {
        super.setName(name);
        super.setText(name);
    }
    
    public Object getValue() {
        String txtValue = getText();
        if ( ValueUtil.isEmpty(txtValue) && nullWhenEmpty )
            return null;
        
        return txtValue;
    }
    
    public void setValue(Object value) {
        if ( value instanceof KeyEvent ) {
            KeyEvent ke = (KeyEvent) value;
            setText( ke.getKeyChar()+"" );
        } else {
            if ( value != null )
                setText(value.toString());
            else
                setText("");
        }
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
    
    public ActionMessage getActionMessage() {
        return actionMessage;
    }
    
    public boolean isRequired() {
        return property.isRequired();
    }
    
    public void setRequired(boolean required) {
        property.setRequired(required);
    }
    
    public String getCaption() {
        return property.getCaption();
    }
    
    public void setCaption(String caption) {
        property.setCaption(caption);
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
    
    public boolean isNullWhenEmpty() {
        return nullWhenEmpty;
    }
    
    public void setNullWhenEmpty(boolean nullWhenEmpty) {
        this.nullWhenEmpty = nullWhenEmpty;
    }
    
    public void setCaptionWidth(int width) {
        property.setCaptionWidth(width);
    }
    
    public boolean isShowCaption() {
        return property.isShowCaption();
    }
    
    public void setShowCaption(boolean showCaption) {
        property.setShowCaption(showCaption);
    }
    
    public ControlProperty getControlProperty() {
        return property;
    }
    
    public TextCase getTextCase() {
        return document.getTextCase();
    }
    
    public void setTextCase(TextCase textCase) {
        document.setTextCase(textCase);
    }

    public int getMaxLength() {
        return document.getMaxlength();
    }
    
    public void setMaxLength(int length) {
        document.setMaxlength(length);
    }
    
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        setEditable(!readonly);
        setFocusable(!readonly);
    }
    
    public boolean isReadonly() {
        return readonly;
    }
    
    public void setRequestFocus(boolean focus) {
        if ( focus ) requestFocus();
    }
    
    public boolean isImmediate() {
        return false;
    }
    
    public String getHint() {
        return hint;
    }
    
    public void setHint(String hint) {
        this.hint = hint;
        showHint = !ValueUtil.isEmpty(hint);
    }
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  XTextFieldSupport  ">
    private class XTextFieldSupport implements FocusListener {
        public void focusGained(FocusEvent e) {
            if(showHint) {
                isHintShown = false;
                repaint();
            }
        }
        
        public void focusLost(FocusEvent e) {
            if(showHint) {
                if(ValueUtil.isEmpty(getText()))
                    isHintShown = true;
                else
                    isHintShown = false;
                
                repaint();
            }
        }
        
    }
    //</editor-fold>
}
