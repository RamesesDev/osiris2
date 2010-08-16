package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.Beans;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Windhel
 */

public class XPasswordField extends JPasswordField implements UIInput, Validatable, ActiveControl {
    
    private static Font FONT = new Font("Dialog", Font.PLAIN, 24);
    
    private ImageIcon[] icons;
    private String[] iconPathList;
    private int imgHeight;
    private int charWidth;
    private int iconIndex;
    private int psswrdWidth;
    private DefaultCaret caret;
    private List iconIndexList = new ArrayList();
    private boolean nullWhenEmpty = true;
    private String[] depends;
    private Binding binding;
    private int index;
    private char passwordChar = '*';
    private ActionMessage actionMessage = new ActionMessage();
    private ControlProperty controlProperty = new ControlProperty();
    private String onAfterUpdate;
    private boolean readonly;
    
    
    public XPasswordField() {}
    
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);
        
        if( getEchoChar() == ' ') {
            setFont(FONT);
            addKeyListener(new KeyListenerSupport());
            charWidth = getFontMetrics(FONT).charWidth(passwordChar);
            setMargin(new Insets(0,0,0,0));
            psswrdWidth= ((int)getSize().getWidth() / charWidth) - 1;
        }
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(Beans.isDesignTime()) return;
        
        if( getEchoChar() == ' ') {
            int x = 0;
            int iconIndex = 0;
            if( getPassword().length >= psswrdWidth)
                x = getPassword().length - psswrdWidth + 1;
            else
                x = 1;
            for(int counter = x, passLength = 0; counter < getPassword().length + 1 ; counter++) {
                if(! (passLength == (psswrdWidth - 1)))
                    passLength++;
                if(iconIndexList.isEmpty() == true)
                    iconIndex = 0;
                else
                    iconIndex = Integer.parseInt( (String) iconIndexList.get(counter - 1) );
                g.drawImage( icons[iconIndex].getImage(), passLength * charWidth, (getHeight() - imgHeight) / 2 , charWidth, imgHeight, null);
            }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public Object getValue() {
        if( Beans.isDesignTime())
            return "";
        
        return String.valueOf(getPassword());
    }
    
    public void setValue(Object value) {
        setText( value==null? "" : value.toString() );
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
    
    public Binding getBinding() {
        return binding;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public void setEchoChar(char c) {
        this.passwordChar = c;
        icons = null;
        iconPathList = null;
    }
    
    public char getEchoChar() {
        return passwordChar;
    }
    
    public void setMargin(Insets m) {
        Insets insets = new Insets(m.top, charWidth, m.bottom, charWidth);
        super.setMargin(insets);
    }
    
    public String[] getIcons() {
        return iconPathList;
    }
    
    public void setIcons(String[] iconPathList) {
        passwordChar = ' ';
        this.iconPathList = iconPathList;
        if(Beans.isDesignTime()) return;
        
        icons = new ImageIcon[iconPathList.length];
        for(int i = 0; i < icons.length; i++) {
            URL res = getImageResource(iconPathList[i]);
            if( res != null )
                icons[i] = new ImageIcon(res);
        }
        imgHeight = icons[0].getIconHeight();
    }
    
    private URL getImageResource(String path) {
        ClassLoader cl = ClientContext.getCurrentContext().getClassLoader();
        return cl.getResource(path);
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
        if( isRequired() && getPassword().length<=0 ) {
            actionMessage.addMessage("", "{0} is required.", new Object[] {getCaption()});
            controlProperty.setErrorMessage(actionMessage.toString());
        }
    }
    
    public ActionMessage getActionMessage() {
        return actionMessage;
    }
    
    public ControlProperty getControlProperty() {
        return controlProperty;
    }
    
    public String getOnAfterUpdate() {
        return onAfterUpdate;
    }
    
    public void setOnAfterUpdate(String onAfterUpdate) {
        this.onAfterUpdate = onAfterUpdate;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        setEditable(!readonly);
        setFocusable(!readonly);
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setRequestFocus(boolean focus) {
        if ( focus ) requestFocus();
    }

    public boolean isImmediate() {
        return false;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  KeyListenerSupport (class)  ">
    private class KeyListenerSupport implements KeyListener{
        public void keyTyped(KeyEvent e) {
        }
        
        public void keyPressed(KeyEvent e) {
            int idx = 0;
            // 35[Home] 36[End] 37-40[directional keys]
            if( !( e.getKeyCode() >= 35 && e.getKeyCode() <= 40 ) ){
                idx = getPassword().length % icons.length;
                iconIndexList.add( getCaretPosition(),"" +  idx);
            }
        }
        
        public void keyReleased(KeyEvent e) {
        }
    }
    //</editor-fold>
}
