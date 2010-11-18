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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author elmo
 */
public class MainDialog implements MainWindow {
    
    private JFrame dialog = new JFrame();
    private MainWindowListener listener;
    
    public MainDialog() {
        dialog.setTitle("Main Dialog");
        dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
    
    public JFrame getComponent() { return dialog; }
    
    public void show() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }
    
    public void close() {
        if ( listener != null ) {
            try {
                if ( !listener.onClose() ) return;
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        
        dialog.dispose();
    }
    
    public void setTitle(String title) {
        dialog.setTitle(title);
    }
    
    public void setListener(MainWindowListener listener) {
        this.listener = listener;
    }
    
    public void setComponent(JComponent comp, String constraint) {
        if ( constraint == null ) {
            //do nothing
        } else if ( constraint.equals(MainWindow.MENUBAR)) {
            dialog.setJMenuBar((JMenuBar) comp);
        } else if ( constraint.equals(MainWindow.TOOLBAR) ) {
            if ( comp instanceof JToolBar )
                ((JToolBar) comp).setFloatable(false);
            if ( dialog.getComponentCount() > 1 ) {
                dialog.remove(1);
            }
            dialog.add(comp, BorderLayout.NORTH, 1);
        }
        SwingUtilities.updateComponentTreeUI( dialog.getContentPane() );
    }
    
}
