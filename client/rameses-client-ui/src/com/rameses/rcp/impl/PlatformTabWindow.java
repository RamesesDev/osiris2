/*
 * PlatformTabWindow.java
 *
 * Created on July 20, 2011, 10:03 PM
 */

package com.rameses.rcp.impl;

import com.rameses.platform.interfaces.SubWindow;
import com.rameses.platform.interfaces.ViewContext;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *
 * @author jaycverg
 */
public class PlatformTabWindow extends JPanel implements SubWindow {

    private String id;
    private String title;
    private boolean canClose = true;
    private ViewContext viewContext;
    private PlatformImpl platform;
    
    
    public PlatformTabWindow(String id, Component comp, PlatformImpl platform) {
        this.id = id;
        this.platform = platform;
        
        setLayout(new BorderLayout());
        add(comp);
        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        }, KeyStroke.getKeyStroke("ctrl W"), JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        if( comp instanceof ViewContext ) {
            viewContext = (ViewContext) comp;
        }
    }
    
    public PlatformTabWindow(String id, Component comp, PlatformImpl platform, boolean canClose) {
        this(id, comp, platform);
        this.canClose = canClose;
    }

    public void close() {
        if( !canClose ) return;
        if( viewContext != null && !viewContext.close() ) return;
        
        Component p = getParent();
        if( p instanceof ExtTabbedPane ) {
            ((ExtTabbedPane)p).remove(this);
        }
        if( platform != null ) {
            platform.windows.remove(id);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCanClose() {
        return canClose;
    }

    public void setCanClose(boolean canClose) {
        this.canClose = canClose;
    }

    public void closeWindow() {
        close();
    }
    
    public void activate() {
        Component p = getParent();
        if( p instanceof ExtTabbedPane ) {
            ((ExtTabbedPane)p).setSelectedComponent(this);
        }
    }

}
