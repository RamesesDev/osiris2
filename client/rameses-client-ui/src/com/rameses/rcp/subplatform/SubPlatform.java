package com.rameses.rcp.subplatform;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.framework.ClientContext;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author jaycverg
 */
public class SubPlatform implements Platform {
    
    private static final String ID_PREFIX = "subPlatform:";
    
    private SubPlatformWindow mainWindow = new SubPlatformWindow();
    private Map windows = new HashMap();
    
    public SubPlatform() {
    }
    
    public void showStartupWindow(JComponent actionSource, JComponent comp, Map properties) {}
    
    public void showWindow(JComponent actionSource, JComponent comp, Map properties) {
        if ( mainWindow.closeCurrentDisplay() ) {
            windows.clear();
            windows.put(properties.get("id"), comp);
            mainWindow.setComponent(comp, MainWindow.CONTENT);
        }
    }
    
    public void showPopup(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        properties.put("id", ID_PREFIX + id);
        ClientContext.getCurrentContext().getPlatform().showPopup(actionSource, comp, properties);
    }
    
    public void showError(JComponent actionSource, Exception e) {
        ClientContext.getCurrentContext().getPlatform().showError(actionSource, e);
    }
    
    public boolean showConfirm(JComponent actionSource, Object message) {
        return ClientContext.getCurrentContext().getPlatform().showConfirm(actionSource, message);
    }
    
    public void showInfo(JComponent actionSource, Object message) {
        ClientContext.getCurrentContext().getPlatform().showInfo(actionSource, message);
    }
    
    public void showAlert(JComponent actionSource, Object message) {
        ClientContext.getCurrentContext().getPlatform().showAlert(actionSource, message);
    }
    
    public Object showInput(JComponent actionSource, Object message) {
        return ClientContext.getCurrentContext().getPlatform().showInput(actionSource, message);
    }
    
    public MainWindow getMainWindow() {
        return mainWindow;
    }
    
    public boolean isWindowExists(String id) {
        Platform p = ClientContext.getCurrentContext().getPlatform();
        return windows.containsKey(id) || p.isWindowExists(ID_PREFIX + id);
    }
    
    public void closeWindow(String id) {
        Platform p = ClientContext.getCurrentContext().getPlatform();
        if ( windows.containsKey(id) ) {
            if ( mainWindow.closeCurrentDisplay() )
                windows.remove(id);
            
        } else if ( p.isWindowExists(ID_PREFIX + id) ) {
            p.closeWindow(ID_PREFIX + id);
        }
    }
    
    public void activateWindow(String id) {}
    
    public void shutdown() {
        ClientContext.getCurrentContext().getPlatform().closeWindow( mainWindow.getId() );
    }
    
    public void logoff() {
    }
    
    
}
