package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.Beans;
import java.io.File;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class XImagePanel extends JPanel implements UIControl 
{    
    private static String DISPLAY_NORMAL        = "NORMAL";
    private static String DISPLAY_TILE          = "TILE";
    private static String DISPLAY_STRETCH       = "STRETCH";
    private static String DISPLAY_AUTO_RESIZE   = "AUTO";

    private String displayMode = DISPLAY_NORMAL; 
    private ImageIcon imageIcon;
    private Border innerBorder;
    private Border outerBorder;
    
    private Binding binding;
    private String[] depends;
    private boolean dynamic; 
    private int index; 
    private String iconResource; 
    
    public XImagePanel() {
        dynamic = true; 
    } 
    
    //<editor-fold defaultstate="collapsed" desc="  setter(s)/getter(s)  ">
    public Border getBorder() { return innerBorder; } 
    public void setBorder(Border border) 
    {
        innerBorder = border; 
        if (innerBorder == null)
            super.setBorder(outerBorder); 
        else 
            super.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder)); 
    } 
    
    public String getDisplayMode() { return displayMode; }
    public void setDisplayMode(String displayMode) 
    {
        if (displayMode != null) 
            this.displayMode = displayMode.toUpperCase(); 
        else
            this.displayMode = DISPLAY_NORMAL; 
    }
    
    public String[] getDepends() { return depends; }   
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }    
    public void setIndex(int index) { this.index = index; }
        
    public Binding getBinding() { return binding; }
    public void setBinding(Binding binding) { this.binding = binding; }
    
    public boolean isDynamic() { return dynamic; }
    public void setDynamic(boolean dynamic) { this.dynamic = dynamic; }
    
    public Icon getIcon() { return imageIcon; } 
    
    public String getIconResource() { return iconResource; } 
    public void setIconResource(String iconResource)
    {
        this.iconResource = iconResource; 
        
        URL url = null;
        try { 
            url = getClass().getResource(iconResource); 
        } catch(Exception ign) {;} 
        
        try 
        { 
            if (url == null)    
                url = ClientContext.getCurrentContext().getClassLoader().getResource(iconResource);  
        } catch(Exception ign) {;} 
        
        try 
        { 
            if (url == null)
                url = Thread.currentThread().getContextClassLoader().getResource(iconResource); 
        } catch(Exception ign) {;}         
        
        try 
        {
            imageIcon = new ImageIcon(url); 
            int iw = imageIcon.getIconWidth(); 
            int ih = imageIcon.getIconHeight(); 
            setPreferredSize(new Dimension(iw, ih));  
            setMinimumSize(new Dimension(iw, ih));             
            setDynamic(true);
        } 
        catch(Exception ign) { 
            imageIcon = null; 
        } 
    }
    //</editor-fold>
    
    private URL getImageResource(String path) 
    {
        if (ValueUtil.isEmpty(path)) return null;
        
        ClassLoader cl = ClientContext.getCurrentContext().getClassLoader();
        return cl.getResource(path);
    }
        
    public void refresh() 
    {
        if (!isDynamic()) return;
        
        Object value = UIControlUtil.getBeanValue(this);
        try 
        {
            imageIcon = null;
            
            if (value instanceof URL) {
                imageIcon = new ImageIcon((URL) value); 
            }
            else if(value instanceof String) 
            {
                URL url = getImageResource(value.toString());
                if (url != null) imageIcon = new ImageIcon(url);
            } 
            else if (value instanceof byte[]) {
                imageIcon = new ImageIcon((byte[])value);
            } 
            else if (value instanceof ImageIcon) {
                imageIcon = (ImageIcon) value;
            } 
            else if (value instanceof File) 
            {
                File file = (File) value; 
                imageIcon = new ImageIcon(file.toURL());
            }
            
            int iw = imageIcon.getIconWidth();
            int ih = imageIcon.getIconHeight();
            setPreferredSize(new Dimension(iw, ih)); 
            setMinimumSize(new Dimension(iw, ih));
        } 
        catch(Exception ex) {;}
    }
    
    public void load() {
    }
                
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }

    protected void paintComponent(Graphics g) 
    {
        super.paintComponents(g);
        
        if (Beans.isDesignTime()) return; 
        if (imageIcon == null) return; 
                
        int imgWidth = imageIcon.getIconWidth();
        int imgHeight = imageIcon.getIconHeight(); 
        int cw = getWidth();
        int ch = getHeight();
        if (DISPLAY_TILE.equals(displayMode)) 
        {
            Color oldColor = g.getColor();
            int cols = cw / imgWidth;
            int rows = ch / imgHeight;
            if (cw % imgWidth > 0) cols += 1;
            if (ch % imgHeight > 0) rows += 1;
            
            for (int i = 0 ; i < rows; i++) {
                for(int ii = 0; ii < cols; ii++) {
                    g.drawImage(imageIcon.getImage(), ii * imgWidth, i * imgHeight, imgWidth, imgHeight, null);
                }
            }
        }
        else if (DISPLAY_STRETCH.equals(displayMode)) 
        {
            cw = Math.max(cw, imgWidth); 
            ch = Math.max(ch, imgHeight); 
            g.drawImage(imageIcon.getImage(), 0, 0, cw, ch, null); 
        }
        else {
            g.drawImage(imageIcon.getImage(), 0, 0, imgWidth, imgHeight, null);
        }        
    }
}

