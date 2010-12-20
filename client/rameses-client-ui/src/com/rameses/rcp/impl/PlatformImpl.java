package com.rameses.rcp.impl;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.util.ErrorDialog;
import com.rameses.util.ValueUtil;
import java.awt.EventQueue;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author jaycverg
 */
public class PlatformImpl implements Platform {
    
    private MainDialog mainWindow = new MainDialog(); //default impl
    private Map windows = new HashMap();
    
    public PlatformImpl() {
    }
    
    public void showStartupWindow(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        if ( ValueUtil.isEmpty(id) )
            throw new IllegalStateException("id is required for a page.");
        
        if ( windows.containsKey(id) ) return;
        
        String title = (String) properties.get("title");
        if ( ValueUtil.isEmpty(title) ) title = id;
        
        String canClose = "false";
        String modal = properties.get("modal")+"";
        
        JFrame parent = mainWindow.getComponent();
        final PopupDialog d = new PopupDialog(parent);
        d.setTitle(title);
        d.setContentPane(comp);
        d.setCanClose( !"false".equals(canClose) );
        d.setId( id );
        d.setPlatformImpl(this);
        d.setModal(false);
        d.pack();
        d.setLocationRelativeTo(parent);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                d.setVisible(true);
            }
        });
        
        windows.put(id, d);
    }
    
    public void showWindow(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        if ( ValueUtil.isEmpty(id) )
            throw new IllegalStateException("id is required for a page.");
        
        if ( windows.containsKey(id) ) return;
        
        String title = (String) properties.get("title");
        if ( ValueUtil.isEmpty(title) ) title = id;
        
        String canClose = (String) properties.get("canclose");
        String modal = properties.get("modal")+"";
        
        JFrame parent = mainWindow.getComponent();
        final PopupDialog d = new PopupDialog(parent);
        d.setTitle(title);
        d.setContentPane(comp);
        d.setCanClose( !"false".equals(canClose) );
        d.setId( id );
        d.setPlatformImpl(this);
        d.setModal(false);
        d.pack();
        d.setLocationRelativeTo(parent);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                d.setVisible(true);
            }
        });
        
        windows.put(id, d);
    }
    
    public void showPopup(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        if ( ValueUtil.isEmpty(id) )
            throw new IllegalStateException("id is required for a page.");
        
        if ( windows.containsKey(id) ) return;
        
        String title = (String) properties.get("title");
        if ( ValueUtil.isEmpty(title) ) title = id;
        
        String modal = properties.get("modal")+"";
        
        JFrame parent = mainWindow.getComponent();
        PopupDialog dd = null;
        if ( actionSource != null ) {
            Window w = getParentWindow(actionSource);
            if ( w instanceof JDialog ) {
                dd = new PopupDialog((JDialog) w);
            }
        }
        if ( dd == null ) dd = new PopupDialog(parent);
        
        final PopupDialog d = dd;
        d.setTitle(title);
        d.setContentPane(comp);
        d.setId( id );
        d.setPlatformImpl(this);
        d.setModal( !"false".equals(modal) );
        d.pack();
        d.setLocationRelativeTo(parent);
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                d.setVisible(true);
            }
        });
        
        windows.put(id, d);
    }
    
    public void showError(JComponent actionSource, Exception e) {
        ErrorDialog.show(e, actionSource);
    }
    
    public boolean showConfirm(JComponent actionSource, Object message) {
        return JOptionPane.showConfirmDialog(getParentWindow(actionSource), message, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    public void showInfo(JComponent actionSource, Object message) {
        JOptionPane.showMessageDialog(getParentWindow(actionSource), message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showAlert(JComponent actionSource, Object message) {
        JOptionPane.showMessageDialog(getParentWindow(actionSource), message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public Object showInput(JComponent actionSource, Object message) {
        return JOptionPane.showInputDialog(getParentWindow(actionSource), message);
    }
    
    public MainWindow getMainWindow() {
        return mainWindow;
    }
    
    public boolean isWindowExists(String id) {
        return windows.containsKey(id);
    }
    
    public void closeWindow(String id) {
        if ( windows.containsKey(id) ) {
            PopupDialog d = (PopupDialog) windows.get(id);
            d.closeWindow();
        }
    }
    
    public void activateWindow(String id) {
        if ( windows.containsKey(id) ) {
            JDialog d = (JDialog) windows.get(id);
            d.requestFocus();
        }
    }
    
    public Map getWindows() { return windows; }
    
    private Window getParentWindow(JComponent src) {
        if ( src == null ) {
            return KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        }
        return SwingUtilities.getWindowAncestor(src);
    }
    
    private String getMessage(Throwable t) {
        if (t == null) return null;
        
        String msg = t.getMessage();
        Throwable cause = t.getCause();
        while (cause != null) {
            String s = cause.getMessage();
            if (s != null) msg = s;
            
            cause = cause.getCause();
        }
        return msg;
    }
    
    public void shutdown() {
        mainWindow.close();
    }
    
    public void logoff() {
    }

    public void lock() {
    }

    public void unlock() {
    }

    public void showFloatingWindow(JComponent owner, JComponent comp, Map properties) {
        showPopup(owner, comp, properties);
    }
    
    
}
