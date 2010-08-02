package com.rameses.rcp.impl;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.Platform;
import com.rameses.util.ValueUtil;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDialog;
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
    
    public void showWindow(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        if ( ValueUtil.isEmpty(id) )
            throw new IllegalStateException("id is required for a page.");
        
        if ( windows.containsKey(id) ) return;
        
        String title = (String) properties.get("title");
        if ( ValueUtil.isEmpty(title) ) title = id;
        
        String canClose = (String) properties.get("canclose");
        String modal = (String) properties.get("modal");
        
        JDialog parent = mainWindow.getComponent();
        PopupDialog d = new PopupDialog(parent);
        d.setTitle(title);
        d.setContentPane(comp);
        d.setCanClose( !"false".equals(canClose) );
        d.setId( id );
        d.setPlatformImpl(this);
        d.setModal(false);
        d.pack();
        d.setLocationRelativeTo(parent);
        d.setVisible(true);
        
        windows.put(id, d);
    }
    
    public void showPopup(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        if ( ValueUtil.isEmpty(id) )
            throw new IllegalStateException("id is required for a page.");
        
        if ( windows.containsKey(id) ) return;
        
        String title = (String) properties.get("title");
        if ( ValueUtil.isEmpty(title) ) title = id;
        
        String canClose = (String) properties.get("canclose");
        String modal = (String) properties.get("modal");
        
        JDialog parent = mainWindow.getComponent();
        if ( actionSource != null ) {
            parent = getParentDialog(actionSource);
        }
        
        final PopupDialog d = new PopupDialog(parent);
        d.setTitle(title);
        d.setContentPane(comp);
        d.setId( id );
        d.setPlatformImpl(this);
        d.setModal(false);
        d.pack();
        d.setLocationRelativeTo(parent);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                d.setVisible(true);
            }
        });
        
        windows.put(id, d);
    }
    
    public void showError(JComponent actionSource, Exception e) {
        JOptionPane.showMessageDialog(null, getMessage(e), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean showConfirm(JComponent actionSource, Object message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    public void showInfo(JComponent actionSource, Object message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showAlert(JComponent actionSource, Object message) {
        JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public Object showInput(JComponent actionSource, Object message) {
        return null;
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
    
    private JDialog getParentDialog(JComponent actionSource) {
        Container parent = actionSource.getParent();
        while( parent != null ) {
            if ( parent instanceof JDialog )
                return (JDialog) parent;
            
            parent = parent.getParent();
        }
        return null;
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
    
}
