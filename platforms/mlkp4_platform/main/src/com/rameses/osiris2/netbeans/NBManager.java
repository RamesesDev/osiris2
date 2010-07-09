package com.rameses.osiris2.netbeans;

import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import java.awt.Container;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public final class NBManager 
{
    private static NBManager instance;
    
    public static NBManager getInstance()
    {
        if (instance == null)
            instance = new NBManager();
        
        return instance;
    }
    
    
    private Object treeLock = new Object();
    private JFrame mainWindow;
    private JMenuBar menubar;
    private Container toolbarView;
    private Container desktopView;
    private Container statusView;
    private Map<Object,Object> properties = new Hashtable<Object,Object>();

    private NBManager() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Getter/Setter ">   
    public JFrame getMainWindow() { 
        return mainWindow; 
    }
    
    void setMainWindow(JFrame mainWindow) { 
        this.mainWindow = mainWindow; 
    }

    public JMenuBar getMenubar() { 
        return menubar; 
    }
    
    void setMenubar(JMenuBar menubar) 
    {
        this.menubar = menubar;
    }
    
    public Container getToolbarView() { 
        return toolbarView; 
    }
    
    void setToolbarView(Container container) 
    {
        this.toolbarView = container;
    }
    
    public Container getDesktopView() { 
        return desktopView; 
    }
    
    void setDesktopView(Container desktopView) {
        this.desktopView = desktopView;
    }
    
    public Container getStatusView() { 
        return statusView; 
    }
    
    void setStatusView(JComponent statusView) {
        this.statusView = statusView;
    }    
        
    public Object getProperty(Object name) { 
        return properties.get(name); 
    }
    
    public void setProperty(Object name, Object value) {
        properties.put(name, value);
    }
    //</editor-fold>
    
    
    //added information:
    private static ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();

    public static ClassLoader originalClassLoader() {
        return originalClassLoader;
    }

    
    
}
