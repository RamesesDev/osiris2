package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
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
import javax.swing.ImageIcon;
import javax.swing.JTextField;

/**
 *
 * @author Windhel
 */

public abstract class XIconedTextField extends JTextField implements ActionListener, UIInput, Validatable {
    
    private static final int XPAD = 4;
    private static final int MARGIN_PAD = 5;
    
    private ImageIcon icon;
    private int imgWidth = 0;
    private int imgHeight = 0;
    private String orient;
    private boolean mouseOverImage = false;
    
    private boolean nullWhenEmpty = true;
    private String[] depends;
    private int index;
    private Binding binding;
    private ControlProperty controlProperty = new ControlProperty();
    private ActionMessage actionMessage = new ActionMessage();
    
    public XIconedTextField() {
    }
    
    
    public Object getValue() {
        if(Beans.isDesignTime())
            return "";
        
        String value = getText();
        return value;
    }

    public void setValue(Object value) {
        setText( value==null ? "" : value.toString() );
    }

    public boolean isNullWhenEmpty() {
        return nullWhenEmpty;
    }
    
    public void setNullWhenEmpty(boolean nullWhenEmpty) {
        this.nullWhenEmpty = nullWhenEmpty;
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

    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }

    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);        
        
        XIconedTextFieldSupport support = new XIconedTextFieldSupport();
        addMouseListener(support);
        addMouseMotionListener(support);
        addActionListener(this);
    }

    public int compareTo(Object o) {
        if(o == null || !(o instanceof UIControl) )
            return 0;
        
        UIControl u = (UIControl)o;
        return this.index - u.getIndex();
    }
    
    public String getCaption() {
        return controlProperty.getCaption();
    }

    public void setCaption(String caption) {
        controlProperty.setCaption(caption);
    }

    public boolean isRequired() {
        return controlProperty.isRequired();
    }

    public void setRequired(boolean required) {
        controlProperty.setRequired(required);
    }

    public void validateInput() {
        actionMessage.clearMessages();
        controlProperty.setErrorMessage(null);
        if( isRequired() && ValueUtil.isEmpty(getText()) ) {
            actionMessage.addMessage("", "{0} is required", new Object[]{ getCaption() });
            controlProperty.setErrorMessage(actionMessage.toString());
        }
    }

    public ActionMessage getActionMessage() {
        return actionMessage;
    }

    public ControlProperty getControlProperty() {
        return controlProperty;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        if( mouseOverImage == true )
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
        if(imgWidth > 0)
            if(orient.toUpperCase() == "RIGHT")
                g2.drawImage( icon.getImage(), this.getWidth() - (imgWidth + XPAD), (this.getHeight() - imgHeight) / 2 , null);
            else
                g2.drawImage( icon.getImage(), XPAD, (this.getHeight() - imgHeight) / 2 , null);
        g2.dispose();
    }
    
    public abstract void actionPerformed();
    
    public void actionPerformed(ActionEvent e) {
        actionPerformed();
    }
    
    public void setMargin(Insets m) {
        Insets insets = new Insets(m.top, m.left, m.bottom, m.right);
        super.setMargin(insets);
    }
    
    public void setOrientation(String orient) {
        this.orient = orient;
        Insets insets = getMargin();
        Insets leftOrientationInset = new Insets(insets.top,  imgWidth + MARGIN_PAD, insets.bottom, 0);
        Insets rightOrientationInset = new Insets(insets.top,  insets.right, insets.bottom, imgWidth + MARGIN_PAD);
        if(orient.toUpperCase() == "LEFT")
            setMargin(leftOrientationInset);
        else
            setMargin(rightOrientationInset);
    }
    
    public ImageIcon getIcon() {
        return icon;
    }
    
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
        imgWidth = icon.getIconWidth();
        imgHeight = icon.getIconHeight();
    }
    
    public void setIcon(String str) {
        setIcon(new ImageIcon(str));
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  XIconedTextFieldSupport (class)  ">
    private class XIconedTextFieldSupport implements MouseListener, MouseMotionListener {
        public void mouseClicked(MouseEvent e) {
            if(orient.toUpperCase() == "RIGHT") {
                if(e.getX() >= (XIconedTextField.this.getWidth() - (imgWidth + XPAD)))
                    actionPerformed();
            } else {
                if(e.getX() > 0 && e.getX() < (XPAD + imgWidth))
                    actionPerformed();
            }
        }
        
        public void mousePressed(MouseEvent e) {
        }
        
        public void mouseReleased(MouseEvent e) {
        }
        
        public void mouseEntered(MouseEvent e) {
        }
        
        public void mouseExited(MouseEvent e) {
            mouseOverImage = false;
            XIconedTextField.this.repaint();
        }
        
        public void mouseDragged(MouseEvent e) {
        }
        
        public void mouseMoved(MouseEvent e) {
            if(orient.toUpperCase() == "RIGHT") {
                if(e.getX() >= (XIconedTextField.this.getWidth() - (imgWidth + XPAD))) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    mouseOverImage = true;
                    XIconedTextField.this.repaint();
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    mouseOverImage = false;
                    XIconedTextField.this.repaint();
                }
            } else {
                if(e.getX() > 0 && e.getX() < (XPAD + imgWidth)) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    mouseOverImage = true;
                    XIconedTextField.this.repaint();
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    mouseOverImage = false;
                    XIconedTextField.this.repaint();
                }
            }
        }
        
    }
    //</editor-fold>
}
