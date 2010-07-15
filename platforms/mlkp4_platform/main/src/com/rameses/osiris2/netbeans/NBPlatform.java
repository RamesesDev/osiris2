/*
 * NBPlatform.java
 *
 * Created on July 13, 2010, 6:40 PM
 * @author jaycverg
 */

package com.rameses.osiris2.netbeans;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.Platform;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class NBPlatform implements Platform {
    
    private NBMainWindow mainWindow;
    private Hashtable windows = new Hashtable();
    
    public NBPlatform(NBMainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    
    
    public void showWindow(JComponent actionSource, JComponent comp, Map properties) {
        NBSubWindow win = null;
        String id = (String) properties.get("id");
        if ( id == null || id.trim().length() == 0 )
            throw new IllegalStateException("Id is required when showing a window.");
        
        if (!windows.containsKey(id)) {
            win = new NBSubWindow(this, mainWindow, id);
            win.add(comp);
            
            String closeable = properties.get("canclose")+"";
            if ("false".equalsIgnoreCase(closeable))
                win.setCloseable(false);
            else
                win.setCloseable(true);
            
            String title = (String) properties.get("title");
            if (title == null || title.trim().length() == 0) title = id;
            
            win.setDisplayName(title);
            windows.put(id, win);
            
            win.open();
        } else {
            win = (NBSubWindow) windows.get(id);
        }
        
        win.requestActive();
        comp.requestFocus();
    }
    
    public boolean isWindowExists(String id) {
        return windows.containsKey(id);
    }
    
    public void activateWindow(String id) {
        if ( windows.containsKey(id) ) {
            NBSubWindow win = (NBSubWindow) windows.get(id);
            win.requestActive();
        }
    }
    
    public void removeWindow(String id) {
        windows.remove(id);
    }
    
    public void showPopup(JComponent actionSource, JComponent comp, Map properties) {
    }
    
    public void showError(JComponent actionSource, Exception e) {
        JOptionPane.showMessageDialog(mainWindow.getComponent(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean showConfirm(JComponent actionSource, Object message) {
        int resp = JOptionPane.showConfirmDialog(mainWindow.getComponent(), message);
        return resp == JOptionPane.YES_OPTION;
    }
    
    public void showInfo(JComponent actionSource, Object message) {
        JOptionPane.showMessageDialog(mainWindow.getComponent(), message.toString(), "Message", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showAlert(JComponent actionSource, Object message) {
        JOptionPane.showMessageDialog(mainWindow.getComponent(), message.toString(), "Message", JOptionPane.WARNING_MESSAGE);
    }
    
    public Object showInput(JComponent actionSource, Object message) {
        return JOptionPane.showInputDialog(mainWindow.getComponent(), message);
    }
    
    public MainWindow getMainWindow() {
        return mainWindow;
    }
    
    public void closeWindow(String id) {
        if ( windows.containsKey(id) ) {
            NBSubWindow win = (NBSubWindow) windows.get(id);
            win.closeWindow();
        }
    }
    
}
