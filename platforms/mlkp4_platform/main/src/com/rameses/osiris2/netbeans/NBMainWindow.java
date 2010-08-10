package com.rameses.osiris2.netbeans;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.MainWindowListener;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

public class NBMainWindow implements MainWindow {
    
    private List<MainWindowListener> listeners = new ArrayList();
    private JFrame window;
    private String mainTitle = "";
    
    public NBMainWindow(JFrame window) {
        this.window = window;
    }
    
    public Component getComponent() { return window; }
    
    public List<MainWindowListener> getListeners() {
        return listeners;
    }
    
    public void addListener(MainWindowListener listener) {
        if ( !listeners.contains(listener) ) {
            listeners.add( listener );
        }
    }
    
    public void removeListener(MainWindowListener listener) {
        listeners.remove(listener);
    }
    
    public void invoke(String name, String action, Map properties) {
        if (name == null) return;
        
        if (name.startsWith("NB:")) {
            Map nbmenus = (Map) window.getRootPane().getClientProperty("NBMenus");
            if (nbmenus != null) {
                Object o = nbmenus.get(name.substring(3));
                if (o instanceof AbstractButton) {
                    ((AbstractButton) o).doClick();
                } else
                    JOptionPane.showMessageDialog(window, "No available handler for action '"+name+"'     ");
            }
        }
    }
    
    private void setMenuBar(JMenuBar m) {
        if (m == null) m = new JMenuBar();
        
        List list = new ArrayList();
        for (int i=0; i<m.getComponentCount(); i++) {
            JMenu jm = m.getMenu(i);
            jm.getComponentCount();
            
            if (jm.getMenuComponentCount() == 0)
                list.add(m.getMenu(i));
        }
        
        while (!list.isEmpty()) {
            JMenu mnu = (JMenu) list.remove(0);
            m.remove(mnu);
        }
        
        NBHeaderBar headerBar = NBManager.getInstance().getHeaderBar();
        headerBar.setTopView(m);
    }
    
    public void setTitle(String title) {
        window.setTitle(mainTitle + " " + title);
    }
    
    public String getTitle() {
        return window.getTitle();
    }
    
    public void close() {
        NBLifecycleManager.getInstance().exit();
    }
    
    public void setComponent(JComponent comp, String constraint) {
        if ( MainWindow.TOOLBAR.equals(constraint) ) {
            NBHeaderBar headerBar = NBManager.getInstance().getHeaderBar();
            headerBar.setBottomView(comp);
        } else if ( MainWindow.MENUBAR.equals(constraint) ) {
            JMenuBar m = (JMenuBar) comp;
            setMenuBar(m);
        }
    }
    
    public void show() {
    }
    
}
