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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author elmo
 */
public class MainDialog implements MainWindow {
    
    private JFrame dialog;
    private MainWindowListener listener;
    
    public MainDialog() {
        dialog = new JFrame();
        dialog.setTitle("Main Dialog");
        dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.setContentPane(new TestPlatformContentPane());
        
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }
    
    public JFrame getComponent() { return dialog; }
    
    public void show() {
        EventQueue.invokeLater(new Runnable() {
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
            comp.setBorder(new ToolbarBorder());
            dialog.add(comp, BorderLayout.NORTH, 1);
        }
        SwingUtilities.updateComponentTreeUI( dialog.getContentPane() );
    }
    
    private class ToolbarBorder extends AbstractBorder {
        
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics g2 = g.create();
            g2.setColor(UIManager.getColor("controlShadow"));
            g2.drawLine(x, height-1, width-1, height-1);
            g2.setColor(UIManager.getColor("controlHighlight"));
            g2.drawLine(x, height-2, width-1, height-2);
            g2.dispose();
        }
        
        public Insets getBorderInsets(Component c) {
            return new Insets(1,1,4,1);
        }
        
    }
    
}
