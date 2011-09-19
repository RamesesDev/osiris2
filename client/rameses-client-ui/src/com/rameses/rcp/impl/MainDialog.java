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
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
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
    private Component toolbar;
    private Component statusbar;
    private ExtTabbedPane tabbedPane;
    
    private Component defaultContentPane;
    
    
    public MainDialog() {
        dialog = new JFrame();
        dialog.setTitle("Main Dialog");
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.add((defaultContentPane = new TestPlatformContentPane()), BorderLayout.CENTER);
        
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
        //do nothing if comp or constraint is null
        if ( comp == null || constraint == null ) return;
        
        if ( constraint.equals(MainWindow.MENUBAR)) {
            dialog.setJMenuBar((JMenuBar) comp);
        } else if ( constraint.equals(MainWindow.TOOLBAR) ) {
            if ( comp instanceof JToolBar )
                ((JToolBar) comp).setFloatable(false);
            if ( toolbar != null ) {
                dialog.remove(toolbar);
            }
            
            toolbar = comp;
            comp.setBorder(new ToolbarBorder());
            dialog.add(comp, BorderLayout.NORTH, 1);
        } else if ( constraint.equals(MainWindow.CONTENT) ) {
            if( comp instanceof PlatformTabWindow ) {
                PlatformTabWindow tab = (PlatformTabWindow) comp;
                if( tabbedPane == null ) {
                    if( defaultContentPane != null )
                        dialog.remove(defaultContentPane);
                    
                    tabbedPane = new ExtTabbedPane();
                    dialog.add(tabbedPane, BorderLayout.CENTER);
                }
                tabbedPane.addTab(tab.getTitle(), tab);
            }
        } else if ( constraint.endsWith(MainWindow.STATUSBAR)) {
            if( statusbar != null )
                dialog.remove(statusbar);
            
            statusbar = comp;
            comp.setBorder(new StatusbarBorder());
            dialog.add(comp, BorderLayout.SOUTH);
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
    
    private class StatusbarBorder extends AbstractBorder {
        
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics g2 = g.create();            
            g2.setColor(UIManager.getColor("controlShadow"));
            g2.drawLine(x, y, width-1, y);
            g2.setColor(UIManager.getColor("controlHighlight"));
            g2.drawLine(x, y+1, width-1, y+1);
            g2.dispose();
        }
        
        public Insets getBorderInsets(Component c) {
            return new Insets(4,1,1,1);
        }
        
    }
    
}
