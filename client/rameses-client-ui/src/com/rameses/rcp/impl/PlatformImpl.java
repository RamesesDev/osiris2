package com.rameses.rcp.impl;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.Platform;
import com.rameses.util.ValueUtil;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

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
        showPopup(actionSource, comp, properties);
    }
    
    public void showPopup(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        if ( ValueUtil.isEmpty(id) )
            throw new IllegalStateException("id is required for a page.");
        
        String title = (String) properties.get("title");
        if ( ValueUtil.isEmpty(title) ) title = id;
        String canClose = (String) properties.get("canclose");
        
        JDialog d = new JDialog(mainWindow.getComponent());
        d.setTitle(title);
        d.setContentPane(comp);
        
        if ( "false".equals(canClose) )
            d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        else
            d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        d.pack();
        d.setVisible(true);
        windows.put(id, d);
    }
    
    public void showError(JComponent actionSource, Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean showConfirm(JComponent actionSource, Object message) {
        return JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION;
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
            JDialog d = (JDialog) windows.remove(id);
            d.dispose();
        }
    }
    
    public void activateWindow(String id) {
        if ( windows.containsKey(id) ) {
            JDialog d = (JDialog) windows.get(id);
            d.toFront();
        }
    }
    
}
