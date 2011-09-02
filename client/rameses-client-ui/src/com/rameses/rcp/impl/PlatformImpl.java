package com.rameses.rcp.impl;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.Platform;
import com.rameses.platform.interfaces.SubWindow;
import com.rameses.rcp.util.ErrorDialog;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author jaycverg
 */
public class PlatformImpl implements Platform {
    
    private MainDialog mainWindow = new MainDialog(); //default impl
    
    Map windows = new HashMap();
    
    
    public PlatformImpl() {
    }
    
    public void showStartupWindow(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        if ( ValueUtil.isEmpty(id) )
            throw new IllegalStateException("id is required for a page.");
        
        if ( windows.containsKey(id) ) return;
        
        PlatformTabWindow tab = new PlatformTabWindow(id, comp, this, false);
        tab.setTitle( (String) properties.get("title") );
        mainWindow.setComponent(tab, MainWindow.CONTENT);
        windows.put(id, mainWindow);
    }
    
    public void showWindow(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.remove("id");
        if ( ValueUtil.isEmpty(id) )
            throw new IllegalStateException("id is required for a page.");
        
        if ( windows.containsKey(id) ) return;
        
        String title = (String) properties.remove("title");
        if ( ValueUtil.isEmpty(title) ) title = id;
        
        String canClose = (String) properties.remove("canclose");
        String modal = properties.remove("modal")+"";
        
        PlatformTabWindow t = new PlatformTabWindow(id, comp, this);
                
        t.setTitle(title);
        t.setCanClose( !"false".equals(canClose) );
        
        mainWindow.setComponent(t, MainWindow.CONTENT);
        windows.put(id, t);
    }
    
    public void showPopup(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.remove("id");
        if ( ValueUtil.isEmpty(id) )
            throw new IllegalStateException("id is required for a page.");
        
        if ( windows.containsKey(id) ) return;
        
        String title = (String) properties.remove("title");
        if ( ValueUtil.isEmpty(title) ) title = id;
        
        String modal = properties.remove("modal")+"";
        
        Component parent = getParentWindow(actionSource);
        PopupDialog dd = null;
        
        if ( parent instanceof JDialog ) {
            dd = new PopupDialog((JDialog) parent);
        }
        else if ( parent instanceof JFrame ) {
            dd = new PopupDialog((JFrame) parent);
        }
        
        if( properties.size() > 0 ) setProperties(dd, properties);
        
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
    
    private void setProperties(Object bean, Map properties) {
        for(Map.Entry me: (Set<Map.Entry>) properties.entrySet()) {
            try {
                PropertyUtils.setNestedProperty(bean, me.getKey().toString(), me.getValue());
            }catch(Exception e) {;}
        }
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
            SubWindow d = (SubWindow) windows.get(id);
            d.closeWindow();
        }
    }
    
    public void activateWindow(String id) {
        if ( windows.containsKey(id) ) {
            SubWindow w = (SubWindow) windows.get(id);
            if( w instanceof JDialog )
                ((JDialog)w).requestFocus();
            else if ( w instanceof PlatformTabWindow )
                ((PlatformTabWindow)w).activate();
        }
    }
    
    public Map getWindows() { return windows; }
    
    private Window getParentWindow(JComponent src) {
        if ( src == null ) {
            Window w = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
            if( w != null && w.isShowing() ) 
                return w;
            else
                return mainWindow.getComponent();
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
        mainWindow.close();
    }
    
    public void lock() {
    }
    
    public void unlock() {
    }
    
    public void showFloatingWindow(JComponent owner, JComponent comp, Map properties) {
        showPopup(owner, comp, properties);
    }
    
    
}
