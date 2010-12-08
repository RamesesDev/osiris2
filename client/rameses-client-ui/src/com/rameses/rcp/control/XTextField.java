package com.rameses.rcp.control;

import com.rameses.rcp.constant.TextCase;
import com.rameses.rcp.constant.TrimSpaceOption;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.support.TextDocument;
import com.rameses.rcp.support.TextEditorSupport;
import com.rameses.rcp.support.ThemeUI;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author jaycverg
 */
public class XTextField extends JTextField implements UIInput, Validatable, ActiveControl {
    
    protected Binding binding;
    
    private int index;
    private String[] depends = new String[]{};
    private boolean nullWhenEmpty = true;
    private boolean readonly;
    private String hint;
    private String inputFormat;
    private String inputFormatErrorMsg;
    private String[] replaceExpr;
    private String[] replaceString;
    
    private boolean showHint;
    private boolean isHintShown;
    
    private TextDocument document = new TextDocument();
    protected ControlProperty property = new ControlProperty();
    protected ActionMessage actionMessage = new ActionMessage();
    
    public XTextField() {
        document.setTextCase(TextCase.UPPER);
        TextEditorSupport.install(this);
        
        //set default font
        Font f = ThemeUI.getFont("XTextField.font");
        if ( f != null ) setFont(f);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        if( showHint && ValueUtil.isEmpty(getText()) ) {
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(getFont());
            int x = getInsets().left;
            int y = (int)(getHeight() /2) + (getInsets().top + (int)(getInsets().bottom / 2));
            g.drawString(" " + getHint(), x, y);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="  UIControl implementation  ">
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
        if ( ValueUtil.isEmpty( getText() ) ) {
            if ( isRequired() ) {
                actionMessage.addMessage("1001", "{0} is required.", new Object[] { getCaption() });
            }
        } else if ( !ValueUtil.isEmpty(inputFormat) && !getText().matches(inputFormat) ) {
            String msg = null;
            if ( inputFormatErrorMsg != null ) msg = inputFormatErrorMsg;
            else msg = "Invalid input format for {0}";
            
            actionMessage.addMessage(null, msg, new Object[]{ getCaption() });
        }
        
        if ( actionMessage.hasMessages() ) {
            property.setErrorMessage( actionMessage.toString() );
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setName(String name) {
        super.setName(name);
        super.setText(name);
    }
    
    public Object getValue() {
        String txtValue = getText();
        if ( ValueUtil.isEmpty(txtValue) && nullWhenEmpty )
            return null;
        
        if ( replaceExpr != null && replaceString != null ) {
            for(int i=0; i<replaceExpr.length; ++i) {
                if ( replaceString.length <= i) break;
                txtValue = txtValue.replaceAll( replaceExpr[i], replaceString[i] );
            }
        }
        
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
    
    public TrimSpaceOption getTrimSpaceOption() {
        return document.getTrimSpaceOption();
    }
    
    public void setTrimSpaceOption(TrimSpaceOption option) {
        document.setTrimSpaceOption(option);
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
    
    public String getInputFormat() {
        return inputFormat;
    }
    
    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }
    
    public String getInputFormatErrorMsg() {
        return inputFormatErrorMsg;
    }
    
    public void setInputFormatErrorMsg(String inputFormatErrorMsg) {
        this.inputFormatErrorMsg = inputFormatErrorMsg;
    }
    
    public String[] getReplaceExpr() {
        return replaceExpr;
    }
    
    public void setReplaceExpr(String[] replaceExpr) {
        this.replaceExpr = replaceExpr;
    }
    
    public String[] getReplaceString() {
        return replaceString;
    }
    
    public void setReplaceString(String[] replaceString) {
        this.replaceString = replaceString;
    }
    //</editor-fold>
    
}
