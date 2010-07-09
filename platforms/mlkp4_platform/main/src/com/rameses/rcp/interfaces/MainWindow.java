package com.rameses.rcp.interfaces;

import java.awt.Component;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JMenuBar;

public interface MainWindow 
{
    
    public static final long serialVersionUID = 1L;
    
    void setListener( MainWindowListener listener );
    
    void invoke(String name, String action, Map properties);
    boolean isWindowExist(String id);
    SubWindow addWindow( String id, JComponent c, Map params );
    SubWindow getWindow(String id);
    void removeWindow(String id);
    
    void setMenuBar( JMenuBar m );
    void setTitle(String title);
    void close();
    
    Component getComponent(); 
    void add(Component comp, Object constraints); 
    void remove(Object constraints);
    
}
