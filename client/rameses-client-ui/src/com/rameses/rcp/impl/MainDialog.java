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
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    
    public MainDialog() {
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel dummy = new JPanel();
        dummy.setPreferredSize(new Dimension(500,500));
        dialog.setContentPane(dummy);
        
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
        ClientContext.getCurrentContext().getTaskManager().stop();
        dialog.dispose();
    }
    
    public void setTitle(String title) {
        dialog.setTitle(title);
    }
    
    public void setListener(MainWindowListener listener) {
    }
    
    public void setComponent(JComponent comp, String constraint) {
        if ( constraint.equals(MainWindow.MENUBAR)) {
            dialog.setJMenuBar((JMenuBar) comp);
        }
    }
    
}
