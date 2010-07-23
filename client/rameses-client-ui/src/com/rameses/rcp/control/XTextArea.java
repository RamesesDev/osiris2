package com.rameses.rcp.control;

import com.rameses.rcp.constant.TextCase;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.support.TextDocument;
import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import javax.swing.JTextArea;

/**
 *
 * @author Windhel
 */

public class XTextArea extends JTextArea implements UIInput, Validatable, Containable {
    
    private Binding binding;
    private int index;
    private boolean nullWhenEmpty = true;
    private String[] depends;
    private ControlProperty controlProperty = new ControlProperty();
    private ActionMessage actionMessage = new ActionMessage();
    private String onAfterUpdate;
    private boolean readonly;
    
    private TextDocument textDocument = new TextDocument();
    
    
    public XTextArea() {}
    
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);
        setDocument(textDocument);
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void validateInput() {
        actionMessage.clearMessages();
        controlProperty.setErrorMessage(null);
        if( isRequired() && ValueUtil.isEmpty(getText()) ) {
            actionMessage.addMessage("", "{0} is required", new Object[]{ getCaption() });
            controlProperty.setErrorMessage(actionMessage.toString());
        }
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setName(String name) {
        super.setName(name);
        setText(name);
    }
    
    public Object getValue() {
        String text = getText();
        if ( ValueUtil.isEmpty(text) && nullWhenEmpty )
            return null;
        
        return text;
    }
    
    public void setValue(Object value) {
        setText(value==null? "" : value.toString());
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
    
    public ActionMessage getActionMessage() {
        return actionMessage;
    }
    
    public ControlProperty getControlProperty() {
        return controlProperty;
    }
    
    public TextCase getTextCase() {
        return textDocument.getTextCase();
    }
    
    public void setTextCase(TextCase textCase) {
        textDocument.setTextCase(textCase);
    }
    
    public String getOnAfterUpdate() {
        return onAfterUpdate;
    }
    
    public void setOnAfterUpdate(String onAfterUpdate) {
        this.onAfterUpdate = onAfterUpdate;
    }
    
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        setEditable(!readonly);
        setFocusable(!readonly);
    }
    
    public boolean isReadonly() {
        return readonly;
    }
    //</editor-fold>
    
}
