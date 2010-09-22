package com.rameses.rcp.control;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.Beans;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Windhel
 */

public class IconButton extends XButton {
    
    private static Font BOTTOM_FONT = new Font("Dialog", Font.PLAIN, 9);
    private static Font RIGHT_FONT = new Font("Dialog", Font.PLAIN, 11);
    private static Color ICON_TOP_COLOR = new Color(238, 233, 233);
    private static Color ICON_BOTTOM_COLOR = new Color(197, 205, 211);
    private static Color ICON_BORDER_CORDER = new Color(132, 130, 132);
    private static Color ICON_HOVER_BORDER_COLOR = new Color(123, 138, 156);
    private static String BOTTOM = "BOTTOM";
    private static String RIGHT = "RIGHT";
    private static int BTNHEIGHT = 50;
    private static int BTNWIDTH = 50;
    private static int EXTENDED_BUTTON_WIDTH = 150;
    private static int BORDER_EXTRA_SPACE = 5;
    
    private boolean mouseOverImage = false;
    private boolean mousePressed = false;
    private Image image;
    private String caption;
    private String captionOrientation;
    private Color captionClr;
    private String imagePath;
    private int btnHeight;
    private int btnWidth;
    private int imgXPos;
    private int imgYPos;
    private int imgHeight;
    private int imgWidth;
    private int strWidth;
    private int strHeight;
    private GradientPaint gradient;
    
    public IconButton() {
        setCaptionClr(Color.BLACK);
        setCaptionOrientation("BOTTOM");
    }
    
    public IconButton(String caption, String path) {
        this.caption = caption;
        setImage(path);
        setCaptionClr(Color.BLACK);
        setCaptionOrientation("BOTTOM");
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getter / Setter ">
    
    public int getStringLineMetric(String str) {
        return getFontMetrics(BOTTOM_FONT).charsWidth(str.toCharArray(), 0, str.length());
    }
    
    public String getCaptionOrientation() {
        return captionOrientation;
    }
    
    public void setCaptionOrientation(String captionOrientation) {
        this.captionOrientation = captionOrientation;
    }
    
    public String getText() {
        return "";
    }
    
    public Image getImage() {
        return image;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    private URL getImageResource(String path) {
        if ( ValueUtil.isEmpty(path) ) return null;
        
        ClassLoader cl = ClientContext.getCurrentContext().getClassLoader();
        return cl.getResource(path);
    }
    
    public void setImage(String imagePath) {
        try {
            URL res = getImageResource(imagePath);
            if ( res == null ) return;
            
            ImageIcon imgIcn = new ImageIcon(res);
            image = imgIcn.getImage();
            imgHeight = image.getHeight(null);
            imgWidth = image.getWidth(null);
            
        } catch(Exception ex) { ex.printStackTrace(); }
    }
    
    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String caption) {
        if(caption == null)
            this.caption = "";
        else {
            this.caption = caption;
        }
    }
    
    public Color getCaptionClr() {
        return captionClr;
    }
    
    public void setCaptionClr(Color captionClr) {
        this.captionClr = captionClr;
    }
    //</editor-fold>
    
    public void paintComponent(Graphics g) {
        if(Beans.isDesignTime())
            return;
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setFont(getFont());
        if(mousePressed && mouseOverImage) {
            g2.setPaint(null);
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(1,1, btnWidth - 3, btnHeight - 3);
        } else {
            g2.setPaint(gradient);
            g2.fillRect(1,1, btnWidth - 3, btnHeight - 3);
        }
        
        if(image != null && caption != null || caption != "") {
            g2.drawImage(image, imgXPos, imgYPos, imgWidth, imgHeight,null);
            g2.setColor(captionClr);
            g2.drawString(caption, strWidth, strHeight);
        }
        
        if(mouseOverImage) {
            g2.setColor(ICON_HOVER_BORDER_COLOR);
            g2.drawRect(0,0, btnWidth, btnHeight);
            g2.drawRect(0,0, btnWidth - 1, btnHeight - 1);
            g2.drawRect(0,0, btnWidth - 2, btnHeight - 2);
            g2.drawRect(1,1, btnWidth - 2, btnHeight - 2);
            g2.drawRect(1,1, btnWidth - 3, btnHeight - 3);
        }else {
            g2.setColor(ICON_BORDER_CORDER);
            g2.drawRect(1,1, btnWidth - 3, btnHeight - 3);
        }
        
        super.paintComponent(g2);
        g2.dispose();
    }
    
    public void load() {
        MouseSupport mouseSupport = new MouseSupport();
        addMouseListener(mouseSupport);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCaption(caption);
        if(BOTTOM.equals(getCaptionOrientation()) && image != null) {
            btnWidth = BTNWIDTH;
            strWidth = (int)((btnWidth - getStringLineMetric(caption)) / 2);
            if(btnWidth < getStringLineMetric(caption)) {
                btnWidth = btnWidth + (getStringLineMetric(caption) - btnWidth) + BORDER_EXTRA_SPACE;
                strWidth = strWidth - (getStringLineMetric(caption) - btnWidth);
                if(strWidth < 0)
                    strWidth = strWidth - strWidth;
            }
            btnHeight = BTNHEIGHT;
            strHeight = btnHeight - BORDER_EXTRA_SPACE;
            imgXPos = (int)((btnWidth - image.getWidth(null)) / 2);
            if(caption == null || caption == "")
                imgYPos = (int)((btnHeight - image.getHeight(null)) / 2);
            else
                imgYPos = (int)((btnHeight - image.getHeight(null)) / 2) - 4;
        }
        if(RIGHT.equals(getCaptionOrientation()) && image != null) {
            btnHeight = BTNHEIGHT;
            btnWidth = EXTENDED_BUTTON_WIDTH;
            if(image != null) {
                imgXPos = 10;
                imgYPos = (int)((btnHeight - image.getHeight(null)) / 2);
                strHeight = (int)(btnHeight / 2) + BORDER_EXTRA_SPACE;
                strWidth = imgXPos + image.getWidth(null) + (BORDER_EXTRA_SPACE * 2);
            }
        }
        
        gradient = new GradientPaint(0,((int) btnHeight / 2), ICON_TOP_COLOR, 0, btnHeight, ICON_BOTTOM_COLOR);
        setPreferredSize(new Dimension(btnWidth, btnHeight));
    }
    
    //<editor-fold defaultstate="collapsed" desc="  MouseSupport -- MouseListener ">
    private class MouseSupport implements MouseListener {
        public void mouseClicked(MouseEvent e) {
        }
        
        public void mousePressed(MouseEvent e) {
            mousePressed = true;
            IconButton.this.repaint();
        }
        
        public void mouseReleased(MouseEvent e) {
            mousePressed = false;
            IconButton.this.repaint();
        }
        
        public void mouseEntered(MouseEvent e) {
            mouseOverImage = true;
            IconButton.this.repaint();
        }
        
        public void mouseExited(MouseEvent e) {
            mouseOverImage = false;
            IconButton.this.repaint();
        }
    }
    //</editor-fold>
    
}
