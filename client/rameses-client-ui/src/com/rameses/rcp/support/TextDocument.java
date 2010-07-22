package com.rameses.rcp.support;

import com.rameses.rcp.constant.TextCase;
import com.rameses.rcp.constant.TrimSpaceOption;
import java.util.logging.Logger;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextDocument extends PlainDocument {
    private Logger logger;
    private TrimSpaceOption trimSpaceOption;
    private TextCase textCase;
    private int maxlength;
    
    public TextDocument() {
        this.logger = Logger.getLogger(getClass().getName());
        this.trimSpaceOption = TrimSpaceOption.ALL;
        this.textCase = TextCase.NONE;
        this.maxlength = -1;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Getter/Setter ">
    public TrimSpaceOption getTrimSpaceOption() {
        return trimSpaceOption;
    }
    
    public void setTrimSpaceOption(TrimSpaceOption trimSpaceOption) {
        this.trimSpaceOption = trimSpaceOption;
    }
    
    public TextCase getTextCase() {
        return textCase;
    }
    
    public void setTextCase(TextCase textCase) {
        this.textCase = textCase;
        try { super.insertString(0, "", null); } catch(Exception ign) {;}
    }
    
    public int getMaxlength() {
        return maxlength;
    }
    
    public void setMaxlength(int length) {
        maxlength = length;
    }
    
    // </editor-fold>
    
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        //check the trim space option
        if(trimSpaceOption!=null) {
            str = trimSpaceOption.trim(str);
        }
        
        if(maxlength>0) {
            if(getLength()+str.length()>maxlength) {
                //MsgBox.err("Text is too long. Maximum length is " + getMaxlength());
                return;
            }
        }
        
        //convert if textCase is specified
        if (textCase != null) {
            str = textCase.convert(str);
        }
        
        super.insertString(offs, str, a);
    }
    
    
}

