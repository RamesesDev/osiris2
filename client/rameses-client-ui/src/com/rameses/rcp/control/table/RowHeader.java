/*
 * RowHeader.java
 *
 * Created on February 22, 2011, 3:10 PM
 * @author jaycverg
 */

package com.rameses.rcp.control.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;


public class RowHeader extends TableHeaderRenderer {
    
    private Color borderColor;
    
    public RowHeader(Color borderColor) {
        this.borderColor = borderColor;
        setBorder(BorderFactory.createLineBorder(borderColor));
        setPreferredSize(new Dimension(23,23));
        setHorizontalAlignment(SwingConstants.CENTER);
        setForeground(Color.BLUE);
        setFont(new Font("Courier", Font.PLAIN, 11));
        //edit(true);
    }
    
    public void setText(String text) {;}
    
    public void edit(boolean b) {
        if (b)
            super.setText("<html><b>*</b></html>");
        else
            super.setText("");
    }
}
