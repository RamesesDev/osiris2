package com.rameses.rcp.control;

import com.rameses.rcp.framework.ClientContext;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Windhel
 */

public class IconButton extends XButton {
    
    private static Font FONT = new Font("Dialog", Font.PLAIN, 9);
    private static Color ICNHOVERBG = new Color(255, 255, 255);//(238, 233, 233); //lighter background used for gradient
    private static Color ICNLWRBG = new Color(100,149,237);//119, 136, 153); //darker background used for gradient
    private static float ALPHACOMP = 0.9f;
    
    private boolean mouseOverImage = false;
    private boolean mousePressed = false;
    private Image image; // the image
    private String caption; // the caption below the image
    private Color captionClr; //caption Color
    private String imagePath; 
    private int btnHeight = 50; //the default button height
    private int btnWidth = 50; //the default button width
    private int imgXPos; // the X position of the image 
    private int imgYPos; // the Y position of the image 
    private int imgHeight; // is set, if we decide to manipulate the images within the code.
    private int imgWidth; // is set, if we decide to manipulate the images within the code.
    private int strWidth; // width of the caption
    private int strHeight; // height of the caption
    private GradientPaint gradient = new GradientPaint(0,0, ICNHOVERBG, 0, btnHeight, ICNLWRBG);
    
    public IconButton() {}
    
    public IconButton(String caption, String path) {
        this.caption = caption;
        setImage(path);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getter / Setter ">
    
    public int getStringLineMetric(String str) {
        return getFontMetrics(FONT).charsWidth(str.toCharArray(), 0, str.length());
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
        ClassLoader cl = ClientContext.getCurrentContext().getClassLoader();
        return cl.getResource(path);
    }
    
    public void setImage(String imagePath) {
        try {
            URL res = getImageResource(imagePath);
            if(res == null)
                res = getImageResource("icons/noIcon.png");
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
        Graphics2D g2 = (Graphics2D) g.create();
        if(image != null)
            g2.drawImage(image, imgXPos, imgYPos, imgWidth, imgHeight,null);
        if(caption != null || caption != "") {
            g2.setFont(FONT);
            g2.setColor(captionClr);
            g2.drawString(caption, strWidth, strHeight);
        }
        if(mousePressed == true)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHACOMP));
        if( mouseOverImage == true ) {
            g2.setPaint(gradient);
            g2.fillRect(0,0, btnWidth, btnHeight);
            if(image != null)
                g2.drawImage(image, imgXPos, imgYPos, imgWidth, imgHeight,null);
            if(caption != null || caption != "") {
                g2.setColor(Color.WHITE);
                g2.drawString(caption, strWidth, strHeight);
            }
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
        btnWidth = (int)this.getPreferredSize().getWidth();
        strWidth = (int)((btnWidth - getStringLineMetric(caption)) / 2);
        if(btnWidth < getStringLineMetric(caption)) {
            btnWidth = btnWidth + (getStringLineMetric(caption) - btnWidth);
            strWidth = strWidth - (getStringLineMetric(caption) - btnWidth);
            if(strWidth < 0)
                strWidth = strWidth - strWidth;
        }
        if(caption == "")
            btnHeight = (int)this.getPreferredSize().getHeight();
        else {
            btnHeight = (int)this.getPreferredSize().getHeight() + 5;
            setPreferredSize(new Dimension(btnWidth, btnHeight));
        }        
        strHeight = btnHeight - 5;
        if(image != null) {
            imgXPos = (int)((btnWidth - image.getWidth(null)) / 2);
            if(caption == null || caption == "")
                imgYPos = (int)((btnHeight - image.getHeight(null)) / 2);
            else
                imgYPos = (int)((btnHeight - image.getHeight(null)) / 2) - 4;
        }
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
