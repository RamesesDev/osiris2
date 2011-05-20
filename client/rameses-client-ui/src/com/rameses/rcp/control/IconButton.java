package com.rameses.rcp.control;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Windhel
 */

class IconButton extends XButton {
    
    private boolean clicked;
    private boolean entered;
    private boolean pressed;
    private Color bgColor = new Color(246,246,241,255);
    private Color borderColor = Color.LIGHT_GRAY;//new Color(206,206,195,255);
    private Color pressedColor = new Color(228,227,220,255);
    private Color pressedShadeColor = Color.LIGHT_GRAY;
    private Color pressedBorderColor = new Color(157,157,146,255);
    private Color pressedOffsetColor = Color.WHITE;
    private Color transparent = new Color(246,246,241,0);
    
    private GradientPaint gradient;
    private GradientPaint rightGradient;
    
    public IconButton() {
        entered = false;
        clicked = false;
        pressed = false;
        addMouseListener(new MouseSupport());
        //setBorder(new ButtonBorder());
        setBorder(new EmptyBorder(0,0,0,0));
    }
    
    protected void paintComponent(Graphics g) {
        int width = getSize().width;
        int height = getSize().height;
        int x = 0;
        int y = 0;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(new BasicStroke(0.5f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if(entered) {
            gradient = new GradientPaint(0, height-6,bgColor,0, height-1, borderColor);
            rightGradient = new GradientPaint(width-4, 0,transparent,width-1, 0, borderColor);
            
            g2.setPaint(gradient);
            g2.fillRoundRect(x,y,width-1,height-1,7,7);
            g2.setPaint(rightGradient);
            g2.fillRoundRect(x,y,width-1,height-1,7,7);
            g2.setPaint(null);
            g2.setColor(borderColor);
            g2.drawRoundRect(x,y,width-1,height-1,7,7);
        }
        //pressedOffsetColor
        if(pressed) {
            gradient = new GradientPaint(0, 0, pressedShadeColor,0, 4, pressedColor);
            rightGradient = new GradientPaint(0, 0, pressedShadeColor,4, 0, transparent);
            
            g2.setPaint(null);
            g2.setColor(pressedColor);
            g2.fillRoundRect(x,y,width-1,height-1,7,7);
            g2.setPaint(gradient);
            g2.fillRoundRect(x,y,width-1,height-1,7,7);
            g2.setPaint(rightGradient);
            g2.fillRoundRect(x,y,width-1,height-1,7,7);
            
            gradient = new GradientPaint(0, height-4,transparent,0, height-1, pressedOffsetColor);
            rightGradient = new GradientPaint(width-3, 0,transparent,width-1, 0, pressedOffsetColor);
            g2.setPaint(gradient);
            g2.fillRoundRect(x,y,width-1,height-1,7,7);
            g2.setPaint(rightGradient);
            g2.fillRoundRect(x,y,width-1,height-1,7,7);
            
            g2.setStroke(new BasicStroke(1.0f));
            g2.setColor(pressedBorderColor);
            g2.drawRoundRect(x,y,width-1,height-1,7,7);
        }
        
        super.paintComponent(g);
        g2.dispose();
    }
    
    //<editor-fold defaultstate="collapsed" desc="MouseSupport(MouseListener)">
    private class MouseSupport implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            clicked = true;
            repaint();
        }
        
        public void mousePressed(MouseEvent e) {
            pressed = true;
            clicked = false;
            repaint();
        }
        
        public void mouseReleased(MouseEvent e) {
            pressed = false;
            clicked = false;
            repaint();
        }
        
        public void mouseEntered(MouseEvent e) {
            entered = true;
            repaint();
        }
        
        public void mouseExited(MouseEvent e) {
            clicked = false;
            entered = false;
            repaint();
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ButtonBorder(AbstractBorder) #not used ">
    private class ButtonBorder extends AbstractBorder {
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setStroke(new BasicStroke(0.5f));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if(entered) {
                gradient = new GradientPaint(0, height-6,transparent,0, height-1, borderColor);
                rightGradient = new GradientPaint(width-4, 0,transparent,width-1, 0, borderColor);
                
                g2.setPaint(gradient);
                g2.fillRoundRect(x,y,width-1,height-1,7,7);
                
                g2.setPaint(rightGradient);
                g2.fillRoundRect(x,y,width-1,height-1,7,7);
                
                g2.setPaint(null);
                g2.setColor(borderColor);
                g2.drawRoundRect(x,y,width-1,height-1,7,7);
            }
            
            g2.dispose();
            super.paintBorder(c,g,x,y,width,height);
        }
        
    }
    //</editor-fold>
}
