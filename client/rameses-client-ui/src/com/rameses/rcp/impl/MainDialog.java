/*
 * MainDialog.java
 *
 * Created on October 27, 2009, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.impl;

import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.MainWindowListener;
import com.rameses.rcp.framework.ClientContext;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author elmo
 */
public class MainDialog implements MainWindow {
    
    private JDialog dialog = new JDialog();
    private List<MainWindowListener> listeners = new ArrayList();
    
    public MainDialog() {
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout( new BorderLayout() );
        
        JPanel dummy = new JPanel();
        dummy.setPreferredSize(new Dimension(500,500));
        dialog.add(dummy, BorderLayout.CENTER, 0);
        
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }
    
    public JDialog getComponent() { return dialog; }
    
    public void show() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }
    
    public void close() {
        if ( listeners != null ) {
            for(MainWindowListener mwl: listeners ) {
                try {
                    if ( !mwl.onClose() ) return;
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        dialog.dispose();
    }
    
    public void setTitle(String title) {
        dialog.setTitle(title);
    }
    
    public void addListener(MainWindowListener listener) {
        if ( !listeners.contains(listener) ) {
            listeners.add( listener );
        }
    }
    
    public void removeListener(MainWindowListener listener) {
        listeners.remove(listener);
    }
    
    public void setComponent(JComponent comp, String constraint) {
        if ( constraint == null ) {
            //do nothing
        } else if ( constraint.equals(MainWindow.MENUBAR)) {
            dialog.setJMenuBar((JMenuBar) comp);
        } else if ( constraint.equals(MainWindow.TOOLBAR) ) {
            if ( dialog.getComponentCount() > 1 ) {
                dialog.remove(1);
            }
            dialog.add(comp, BorderLayout.NORTH, 1);
        }
        SwingUtilities.updateComponentTreeUI( dialog.getContentPane() );
    }
    
}
