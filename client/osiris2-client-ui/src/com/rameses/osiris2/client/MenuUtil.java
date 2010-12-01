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
                    String separator = null;
                    JMenuItem mi = null;
                    if( f.getInvoker() ==null ) {
                        separator = (String) f.getProperties().get("separator");
                        mi = new MenuProxy((Folder) o);
                    }
                    else {
                        separator = (String) f.getInvoker().getProperties().get("separator");
                        mi = new MenuItemProxy((Folder)o);
                    }
                    if(separator!=null && separator.trim().length()>0 ) {
                        mi.putClientProperty("separator", separator);
                    }
                    list.add( mi );
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
                List<JMenuItem> menus = getMenuCollection( id );
                int sz = menus.size(); 
                for( int i=0; i<sz; i++ ) {
                    JMenuItem mi = menus.get(i);
                    String separator = (String)mi.getClientProperty("separator");
                    if( i>0 && separator!=null && separator.equalsIgnoreCase("before")) {
                        String pseparator = (String)menus.get(i-1).getClientProperty("separator");
                        if(pseparator==null || !pseparator.equalsIgnoreCase("after")) {
                            addSeparator();
                        }
                    }
                    add(mi);
                    if( (i!=sz-1) && separator!=null && separator.equalsIgnoreCase("after")) addSeparator();
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
