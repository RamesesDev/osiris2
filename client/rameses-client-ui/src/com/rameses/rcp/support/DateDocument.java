/*
 * NumberDocument.java
 *
 * Created on September 1, 2010, 5:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.support;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Windhel
 */

public class DateDocument extends PlainDocument {
    
    private char dateSeparator;
    private String format;
    
    public DateDocument(char separator, String format) {
        this.dateSeparator = separator;
        this.format = format;
    }
    
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str.matches("\\d+")) {            
            if(offs + 1 < format.length() && format.charAt(offs + 1) == dateSeparator) {
                str = str + dateSeparator;
                super.insertString(offs, str, a);
            } else if(super.getLength() < format.length()) {
                super.insertString(offs, str, a);
            }            
        }
    }
    
    
    
    
}
