/*
 * PopupDialog.java
 *
 * Created on July 26, 2010, 11:45 AM
 * @author jaycverg
 */

package com.rameses.rcp.impl;

import com.rameses.platform.interfaces.SubWindow;
import com.rameses.platform.interfaces.ViewContext;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;


public class PopupDialog extends JDialog implements SubWindow, WindowListener {
    
    private ViewContext viewContext;
    private boolean canClose = true;
    private PlatformImpl platformImpl;
    private String id;
    
    public PopupDialog() {
        super();
        init();
    }
    
    public PopupDialog(JDialog parent) {
        super(parent);
        init();
    }
    
    private void init() {
        addWindowListener(this);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }
    
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);
        if ( contentPane instanceof ViewContext ) {
            viewContext = (ViewContext) contentPane;
        }
    }
    
    public void closeWindow() {
        if ( !canClose ) return;
        if ( viewContext != null && !viewContext.close() ) return;
        
        super.dispose();
        platformImpl.getWindows().remove(id);
    }
    
    public boolean isCanClose() {
        return canClose;
    }
    
    public void setCanClose(boolean canClose) {
        this.canClose = canClose;
    }
    
    public PlatformImpl getPlatformImpl() {
        return platformImpl;
    }
    
    public void setPlatformImpl(PlatformImpl platformImpl) {
        this.platformImpl = platformImpl;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void windowClosing(WindowEvent e) {
        closeWindow();
    }
    
    public void windowOpened(WindowEvent e) {
        if ( viewContext != null ) {
            viewContext.display();
        }
    }
    
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    
}
