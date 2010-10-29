package com.rameses.osiris2.netbeans;

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
    private NBHeaderBar headerBar = new NBHeaderBar(); 

    private NBManager() 
    {
        try
        {
            //URL url = Thread.currentThread().getContextClassLoader().getResource("javax/swing/plaf/metal/icons/ocean/warning.png"); 
            //headerBar.setLogoIcon(new ImageIcon(url));
        } catch(Exception ing){;} 
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
    
    NBHeaderBar getHeaderBar() { 
        return headerBar;
    }
    
    public Container getToolbarView() { 
        return toolbarView; 
    }
    
    void setToolbarView(Container container) 
    {
        this.toolbarView = container;
        container.add(headerBar); 
        headerBar.setName("toolbarpanel"); 
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
