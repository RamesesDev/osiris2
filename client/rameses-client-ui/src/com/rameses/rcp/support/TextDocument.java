package com.rameses.rcp.support;

import com.rameses.rcp.constant.TextCase;
import com.rameses.rcp.constant.TrimSpaceOption;
import java.util.logging.Logger;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextDocument extends PlainDocument {
    
    private Logger logger;
    private TextCase textCase;
    private int maxlength;
    
    
    public TextDocument() {
        this.logger = Logger.getLogger(getClass().getName());
        this.textCase = TextCase.NONE;
        this.maxlength = -1;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Getter/Setter ">
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
        if(maxlength>0) {
            if (getLength() >= maxlength) return;
            if (getLength()+str.length() > maxlength) {
                str = str.substring(0, maxlength - getLength());
            }
        }
        
        //convert if textCase is specified
        if (textCase != null) {
            str = textCase.convert(str);
        }
        
        super.insertString(offs, str, a);
    }

}

