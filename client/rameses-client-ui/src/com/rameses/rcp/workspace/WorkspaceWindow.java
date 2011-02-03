/*
 * WorkspaceWindow.java
 *
 * Created on October 27, 2009, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.workspace;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.MainWindowListener;
import com.rameses.platform.interfaces.SubWindow;
import com.rameses.platform.interfaces.ViewContext;
import com.rameses.rcp.framework.ClientContext;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author elmo
 */
public class WorkspaceWindow extends JPanel implements MainWindow {
    
    private MainWindowListener listener;
    private JPanel headerPanel;
    private JPanel contentPane;
    private String title;
    private String id;
    
    private Component menubar;
    private Component toolbar;
    
    
    public WorkspaceWindow() {
        setLayout( new BorderLayout() );
        
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        add(contentPane, BorderLayout.CENTER);
    }
    
    
    public void show() {
        Map props = new HashMap();
        props.put("title", title);
        props.put("id", id);
        ClientContext.getCurrentContext().getPlatform().showWindow(null, new ViewPanel(this), props);
    }
    
    public boolean closeCurrentDisplay() {
        if ( contentPane.getComponentCount() > 0 ) {
            Component c = contentPane.getComponent(0);
            if ( c instanceof ViewContext ) {
                if ( !((ViewContext) c).close() ) return false;
            }
        }
        contentPane.removeAll();
        SwingUtilities.updateComponentTreeUI(contentPane);
        return true;
    }
    
    public void close() {}
    
    public void setTitle(String title) { this.title = title; }
    public String getTitle()           { return title; }
    
    public void setId(String id) { this.id = id; }
    public String getId() { return id; }
    
    public void setListener(MainWindowListener listener) {
        this.listener = listener;
    }
    
    public void setComponent(JComponent comp, String constraint) {
        if ( constraint == null ) return;
        
        if ( constraint.equals(MainWindow.MENUBAR)) {
            menubar = comp;
            headerPanel.add(comp, BorderLayout.NORTH);
            
        } else if ( constraint.equals(MainWindow.TOOLBAR) ) {
            if ( comp instanceof JToolBar ) ((JToolBar) comp).setFloatable(false);
            toolbar = comp;
            headerPanel.add(comp, BorderLayout.CENTER);
            
        } else if ( constraint.equals(MainWindow.CONTENT) ) {
            contentPane.removeAll();
            contentPane.add(comp);
        }
        SwingUtilities.updateComponentTreeUI(headerPanel);
    }
    
    public void display() {
    }
    
    public void setMenubarVisible(boolean visible) {
        if( menubar != null ) menubar.setVisible(visible);
    }
    
    public void setToolbarVisible(boolean visible) {
        if( toolbar != null ) toolbar.setVisible(visible);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  ViewPanel (class)  ">
    private class ViewPanel extends JPanel implements ViewContext {
        
        private SubWindow parent;
        
        ViewPanel(JComponent c) {
            setLayout(new BorderLayout());
            add(c);
        }
        
        public boolean close() {
            if ( listener != null ) {
                try {
                    if ( !listener.onClose() ) return false;
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            return true;
        }
        
        public void display() {
            if ( contentPane.getComponentCount() == 0) return;
            Component c = contentPane.getComponent(0);
            if ( c instanceof ViewContext ) ((ViewContext) c).display();
        }

        public void setSubWindow(SubWindow subWindow) {
            parent = subWindow;
        }

        public SubWindow getSubWindow() {
            return parent;
        }
        
    }
    //</editor-fold>
}
