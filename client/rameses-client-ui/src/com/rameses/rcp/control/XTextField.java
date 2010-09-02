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
import java.awt.event.KeyEvent;
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
    private String onAfterUpdate;
    private boolean readonly;
    
    private TextDocument document = new TextDocument();
    
    protected ControlProperty property = new ControlProperty();
    protected ActionMessage actionMessage = new ActionMessage();
    
    
    public XTextField() {
        document.setTextCase(TextCase.UPPER);
    }
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);
        setDocument(document);
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
    
    public String getOnAfterUpdate() {
        return onAfterUpdate;
    }
    
    public void setOnAfterUpdate(String onAfterUpdate) {
        this.onAfterUpdate = onAfterUpdate;
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
    //</editor-fold>
    
}
