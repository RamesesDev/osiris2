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
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author jaycverg
 */
public class XTextField extends JTextField implements UIInput, Validatable, Containable {
    
    private int index;
    private String[] depends = new String[]{};
    private Binding binding;
    private ControlProperty property = new ControlProperty();
    private ActionMessage actionMessage = new ActionMessage();
    private boolean nullWhenEmpty = true;
    
    public XTextField() {
        setInputVerifier(UIInputUtil.VERIFIER);
    }
    
    public Object getValue() {
        String txtValue = getText();
        if ( txtValue != null ) return txtValue;
        else if ( nullWhenEmpty ) return null;
        
        return "";
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
        
    }
    
    public int compareTo(Object o) {
        if ( o == null || !(o instanceof UIControl) ) return 0;
        return this.index - ((UIControl) o).getIndex();
    }
    
    public void validateInput() {
        actionMessage.clearMessages();
        property.setErrorMessage(null);
        if ( isRequired() && ValueUtil.isEmpty(getValue()) ) {
            actionMessage.clearMessages();
            actionMessage.addMessage("1001", "{0} is required.", new Object[] {getCaption()});
            property.setErrorMessage(actionMessage.toString());
        }
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
}
