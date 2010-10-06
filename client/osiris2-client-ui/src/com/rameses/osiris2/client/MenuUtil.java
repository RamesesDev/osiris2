/*
 * OsirisMenuUtil.java
 *
 * Created on October 27, 2009, 4:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.Folder;
import com.rameses.osiris2.Invoker;
import com.rameses.rcp.util.ControlSupport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author elmo
 */
public final class MenuUtil {
    
    public static JMenuBar getMenuBar(String name) {
        JMenuBar menuBar = new JMenuBar();
        for(JMenuItem m: getMenuCollection(name)) {
            menuBar.add( m );
        }
        return menuBar;
    }
    
    public static List<JMenuItem> getMenuCollection(String name) {
        List list = new ArrayList<JMenuItem>();
        if( name != null ) {
            SessionContext app = OsirisContext.getSession();
            List items = (List) app.getFolders(name);
            if(items!=null) {
                for (Object o : items) {
                    Folder f = (Folder)o;
                    if( f.getInvoker() ==null ) {
                        list.add(new MenuProxy((Folder) o));
                    }
                    else {
                        list.add(new MenuItemProxy((Folder)o));
                    }
                }
            }
        }
        return list;
    }
    
    private static char getFirstChar(String value) {
        if (value != null && value.trim().length() > 0)
            return value.charAt(0);
        else
            return '\u0000';
    }
    
    private static class MenuProxy extends JMenu {
        private boolean init;
        private String id;
        
        MenuProxy(Folder menu) {
            this.id = menu.getFullId();
            setText(menu.getCaption());
            Object mnemonic = menu.getProperties().get("mnemonic");
            if (mnemonic != null) setMnemonic(getFirstChar(mnemonic.toString()));
            String icon = (String)menu.getProperties().get("icon");
            if( icon !=null) {
                setIcon(ControlSupport.getImageIcon(icon));
            }            
        }

        public int getComponentCount() {
            if(!init) {
                for(JMenuItem mi: getMenuCollection( id ) ) {
                    add(mi);
                }        
                init = true;
            }
            return super.getComponentCount();
        }
        
    }
    
    private static class MenuItemProxy extends JMenuItem implements ActionListener {
        private Invoker invoker;
        
        MenuItemProxy(Folder menu) {
            setText(menu.getCaption());
            Object mnemonic = menu.getProperties().get("mnemonic");
            if (mnemonic != null) setMnemonic(getFirstChar(mnemonic.toString()));
            invoker = menu.getInvoker();
            
            String icon = (String)invoker.getProperties().get("icon");
            if( icon !=null) {
                setIcon(ControlSupport.getImageIcon(icon));
            }
            setActionCommand(invoker.getAction());
            addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent e) {
            try {
                InvokerUtil.invoke(invoker, null);
            } catch(Exception ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
        }
    }
    
    
    
}
