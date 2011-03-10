/*
 * WorkspaceWindow.java
 *
 */

package com.rameses.rcp.workspace;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.MainWindowListener;
import com.rameses.platform.interfaces.ViewContext;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author jaycverg
 */
public class WorkspaceWindow extends JPanel implements MainWindow {
    
    private MainWindowListener listener;
    private JPanel headerPanel;
    private JPanel contentPane;
    
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
    
    public void close() {}
    public void setTitle(String title) {}
    
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
    
    public void setMenubarVisible(boolean visible) {
        if( menubar != null ) menubar.setVisible(visible);
    }
    
    public void setToolbarVisible(boolean visible) {
        if( toolbar != null ) toolbar.setVisible(visible);
    }
    
    public ViewContext getViewContext() {
        Component comp = contentPane.getComponent(0);
        if( comp != null && comp instanceof ViewContext ) {
            return (ViewContext) comp;
        }
        return null;
    }
    
}
