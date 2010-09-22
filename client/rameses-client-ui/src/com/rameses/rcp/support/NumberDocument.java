/*
 * NumberDocument.java
 *
 * Created on September 2, 2010, 11:27 AM
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
public class NumberDocument extends PlainDocument {
    
    private boolean hasPeriod;
    
    public NumberDocument() {
    }
    
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str.matches("\\d*\\.?\\d*")) {
            if(str.indexOf('.') != -1) {
                if(hasPeriod) {
                    super.insertString(offs, str, a);
                    return;
                }
                hasPeriod = true;
            }
            
            super.insertString(offs, str, a);
        }
    }
    
}
