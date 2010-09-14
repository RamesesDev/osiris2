/*
 * XHeader.java
 *
 * Created on September 14, 2010, 1:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.Beans;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author rameses
 */
public class XImagePanel extends JLabel implements UIControl {
    
    private static String NORMAL = "NORMAL";
    private static String TILE = "TILE";
    //private static String STRETCH = "STRETCH";
    
    private ImageIcon imageIcon;
    private int index;
    private Binding binding;
    private String[] depends;
    private String displayMode = NORMAL;
    
    private int imgHeight = 0;
    private int imgWidth = 0;
    private int x = 0;
    private int y = 0;
    private int cw = 0;
    private int ch = 0;
    private int cols = 0;
    private int rows = 0;
    
    
    public XImagePanel() {
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  setter(s)/getter(s)  ">
    public String getDisplayMode() {
        return displayMode;
    }
    
    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
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
    //</editor-fold>
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        try {
            if(value instanceof String) {
                imageIcon = new ImageIcon(value.toString());
            } else if(value instanceof ImageIcon) {
                imageIcon = (ImageIcon)value;
            }
            
            imgHeight = imageIcon.getIconHeight();
            imgWidth = imageIcon.getIconWidth();
            super.setPreferredSize(new Dimension(imgWidth, imgHeight));
        } catch(Exception ex) { ex.printStackTrace(); }
    }
    
    public void load() {}
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        
        if(Beans.isDesignTime()) return;
        
        if(imageIcon == null) return;
        
        if(NORMAL.equals(displayMode)) {
            g.drawImage(imageIcon.getImage(), 0, 0, imgWidth, imgHeight, null);
        }else if(TILE.equals(displayMode)) {
            Color oldColor = g.getColor();
            cw = getWidth();
            ch = getHeight();
            cols = cw / imgWidth;
            rows = ch / imgHeight;            
            if(cw % imgWidth > 0 ) cols += 1;
            if(ch % imgHeight > 0 ) rows += 1;
            
            for(int i = 0 ; i < rows; i++) {
                for(int ii = 0; ii < cols; ii++) {
                    g.drawImage(imageIcon.getImage(), ii * imgWidth, i * imgHeight, imgWidth, imgHeight, null );
                }
            }
        }
    }
}
