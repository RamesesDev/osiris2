/*
 * ColorPropertyEditor.java
 *
 * Created on September 3, 2009, 6:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.beaninfo.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;



public class ColorPropertyEditor extends PropertyEditorSupport {
    
    private JPanel panel;
    private JPanel colorField;
    private Color color;
    
    static
    {
        String[] path = PropertyEditorManager.getEditorSearchPath();
        String[] newPath = new String[ path.length + 1];
        
        newPath[0] = "com.rameses.rcp.ui.editors";
        System.arraycopy( path, 0, newPath, 1, path.length);
        
        PropertyEditorManager.setEditorSearchPath( newPath);
        
        for ( int i = 0; i < newPath.length; i++)
            System.err.println( newPath[i]);
        
        PropertyEditorManager.registerEditor( new java.awt.Color(10).getClass(),null);
        PropertyEditorManager.registerEditor( new java.awt.Color(10).getClass(), ColorPropertyEditor.class);
    }
    
    public ColorPropertyEditor() {
        color = Color.white;
        
        panel = new JPanel();
        panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));
        
        colorField = new JPanel() {
            public Dimension getPreferredSize() {
                return getMinimumSize();
            }
            public Dimension getMinimumSize() {
                return new Dimension( 15, 15);
            }
        };
        colorField.setBackground( color);
        panel.add( colorField);
        
        JButton button = new JButton( "Choose");
        button.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                color = JColorChooser.showDialog( panel, "Color Chooser", color);
            }
        });
        panel.add( button);
    }
    
    public void setValue( Object value) {
        color = new Color( ((Color)value).getRGB());
        
        colorField.setBackground( color);
        colorField.repaint();
    }
    
    public Object getValue() {
        return color;
    }
    
    public boolean isPaintable() {
        return false;
    }
    
    public void paintValue( Graphics g, Rectangle r) {
    }
    
    public String getAsText() {
        return "red";
    }
    
    public void setAsText(String text) throws IllegalArgumentException {
    }
    
    public String[] getTags() {
        return null;
    }
    
    public Component getCustomEditor() {
        return panel;
    }
    
    public boolean supportsCustomEditor() {
        return true;
    }
}

