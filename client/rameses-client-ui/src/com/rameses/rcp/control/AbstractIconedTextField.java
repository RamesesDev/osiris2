package com.rameses.rcp.control;

import com.rameses.rcp.framework.ClientContext;
import java.awt.AlphaComposite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.Beans;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

/**
 *
 * @author Windhel
 */

public abstract class AbstractIconedTextField extends XTextField implements ActionListener {
    
    public static final String ICON_ON_LEFT = "LEFT";
    public static final String ICON_ON_RIGHT = "RIGHT";
    
    private static final int XPAD = 4;
    private static final int MARGIN_PAD = 5;
    
    private ImageIcon icon;
    private int imgWidth = 0;
    private int imgHeight = 0;
    private String orientation = ICON_ON_RIGHT;
    private boolean mouseOverImage = false;
    
    
    public AbstractIconedTextField() {
        IconedTextFieldSupport support = new IconedTextFieldSupport();
        addMouseListener(support);
        addMouseMotionListener(support);
        addActionListener(this);
    }
    
    public AbstractIconedTextField(String iconPath) {
        this();
        
        if ( Beans.isDesignTime() ) return;
        
        ClassLoader loader = ClientContext.getCurrentContext().getClassLoader();
        URL url = loader.getResource(iconPath);
        setIcon(new ImageIcon(url));
    }
    
    
    public abstract void actionPerformed();
    
    
    public void actionPerformed(ActionEvent e) {
        if ( !isFocusable() || !isVisible() || !isEnabled() ) return;
        
        actionPerformed();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        
        if( !isFocusable() ) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f));
        } else if( mouseOverImage == true ) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
        }
        
        if(imgWidth > 0) {
            if(orientation.toUpperCase() == "RIGHT")
                g2.drawImage( icon.getImage(), this.getWidth() - (imgWidth + XPAD), (this.getHeight() - imgHeight) / 2 , null);
            else
                g2.drawImage( icon.getImage(), XPAD, (this.getHeight() - imgHeight) / 2 , null);
        }
        g2.dispose();
    }
    
    public void setBorder(Border border) {
        Border inner = null;
        if( ICON_ON_LEFT.equals( orientation ) ) {
            inner = BorderFactory.createEmptyBorder(0, imgWidth + MARGIN_PAD, 0, 0);
        } else {
            inner = BorderFactory.createEmptyBorder(0, 0, 0, imgWidth + MARGIN_PAD);
        }
        
        Border newBorder = null;
        if ( border != null )
            newBorder = BorderFactory.createCompoundBorder(border, inner);
        else
            newBorder = inner;
        
        super.setBorder(newBorder);
    }
    
    public void setOrientation(String orient) {
        this.orientation = orient;
        Insets insets = super.getMargin();
        Insets actualInsets = null;
        if( ICON_ON_LEFT.equals( orientation ) ) {
            actualInsets = new Insets(insets.top,  imgWidth + MARGIN_PAD, insets.bottom, 0);
            super.setMargin(actualInsets);
        } else {
            actualInsets = new Insets(insets.top,  insets.right, insets.bottom, imgWidth + MARGIN_PAD);
            super.setMargin(actualInsets);
        }
    }
    
    public ImageIcon getIcon() {
        return icon;
    }
    
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
        imgWidth = icon.getIconWidth();
        imgHeight = icon.getIconHeight();
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  IconedTextFieldSupport (class)  ">
    private class IconedTextFieldSupport implements MouseListener, MouseMotionListener {
        
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseDragged(MouseEvent e) {}
        
        public void mouseClicked(MouseEvent e) {
            if(orientation.toUpperCase() == "RIGHT") {
                if(e.getX() >= (AbstractIconedTextField.this.getWidth() - (imgWidth + XPAD)))
                    actionPerformed(null);
            } else {
                if(e.getX() > 0 && e.getX() < (XPAD + imgWidth))
                    actionPerformed(null);
            }
        }
        
        public void mouseExited(MouseEvent e) {
            mouseOverImage = false;
            AbstractIconedTextField.this.repaint();
        }
        
        public void mouseMoved(MouseEvent e) {
            if(orientation.toUpperCase() == "RIGHT") {
                if(e.getX() >= (AbstractIconedTextField.this.getWidth() - (imgWidth + XPAD))) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    mouseOverImage = true;
                    AbstractIconedTextField.this.repaint();
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    mouseOverImage = false;
                    AbstractIconedTextField.this.repaint();
                }
            } else {
                if(e.getX() > 0 && e.getX() < (XPAD + imgWidth)) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    mouseOverImage = true;
                    AbstractIconedTextField.this.repaint();
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    mouseOverImage = false;
                    AbstractIconedTextField.this.repaint();
                }
            }
        }
        
    }
    //</editor-fold>
    
}
