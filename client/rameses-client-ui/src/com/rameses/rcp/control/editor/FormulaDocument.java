/*
 * FormulaDocument.java
 *
 * @author jaycverg
 */

package com.rameses.rcp.control.editor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class FormulaDocument extends DefaultStyledDocument 
{
    private List<String> keywords;
    
    private Style style;
    private Style varStyle;
    private Style amtStyle;
    
    
    public FormulaDocument() {        
        style = addStyle("Default-Style", null);
        
        varStyle = addStyle("Variable-Style", style);
        StyleConstants.setForeground(varStyle, new Color(0,0,102));
        StyleConstants.setBold(varStyle, true);
        
        amtStyle = addStyle("Amount-Style", style);
        StyleConstants.setForeground(amtStyle, new Color(204,0,0));
    }
    
    public List<String> getKeywords() {
        if( keywords != null ) return keywords;
        
        return (keywords = new ArrayList<String>(){
            public boolean add(String str) {
                return super.add( str==null? null : str.toUpperCase() );
            }
            public boolean addAll(Collection<? extends String> col) {
                List<String> list = new ArrayList();
                for(String s : col) list.add( s.toUpperCase() );
                return super.addAll( list );
            }
        });
    }
    
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, str.toUpperCase(), style);
        updateCharacterAttributes();
    }
    
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        updateCharacterAttributes();
    }
    
    
    private void updateCharacterAttributes() throws BadLocationException {
        try {
            StringBuffer sb = new StringBuffer(getText(0, getLength()));
            StringBuffer buffer = new StringBuffer();
            int offset = -1;
            for (int i=0; i<sb.length(); i++) {
                if( offset == -1 ) offset = i;
                
                char ch = sb.charAt(i);
                buffer.append( ch );
                
                if( (ch+"").matches("[^\\w_]") ) {
                    buffer.delete(0, buffer.length());
                    offset = -1;
                }
                else {
                    applyStyle(buffer, offset);
                }
            }
        } catch(BadLocationException ble) {
            throw ble;
        }
    }
    
    private void applyStyle(StringBuffer buffer, int offset) {
        String str = buffer.toString();
        if( getKeywords().contains( str ) )
            setCharacterAttributes(offset, buffer.length(), varStyle, true);
        else if ( str.matches("(\\d+)(\\.?\\d+)?") )
            setCharacterAttributes(offset, buffer.length(), amtStyle, true);
        else
            setCharacterAttributes(offset, buffer.length(), style, true);
    }
    
}
