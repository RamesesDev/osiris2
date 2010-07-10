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
import java.beans.Beans;
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
    
    public XTextArea() {
    }

    public Object getValue() {
        if(Beans.isDesignTime())
            return "";
        
        return getText();
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

    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }

    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);
    }

    public int compareTo(Object o) {
        if( o == null || !(o instanceof UIControl) )
            return 0;
        
        UIControl u = (UIControl)o;
        return this.index - u.getIndex();
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
    
}
