package com.rameses.osiris2.netbeans;

import com.rameses.rcp.interfaces.MainWindow;
import com.rameses.rcp.interfaces.MainWindowListener;
import com.rameses.rcp.interfaces.SubWindow;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NBMainWindow implements MainWindow
{
    private MainWindowListener listener;
    private JFrame window;
    private Hashtable windows = new Hashtable();
    private String mainTitle = "";
    
    public NBMainWindow(JFrame window) {
        this.window = window;
    }

    public Component getComponent() { return window; } 
    
    public MainWindowListener getListener() { return listener; }
    public void setListener(MainWindowListener listener) {
        this.listener = listener;
    }

    public void invoke(String name, String action, Map properties) 
    {
        if (name == null) return;
        
        if (name.startsWith("NB:"))
        {
            Map nbmenus = (Map) window.getRootPane().getClientProperty("NBMenus"); 
            if (nbmenus != null) 
            { 
                Object o = nbmenus.get(name.substring(3));
                if (o instanceof AbstractButton) 
                {
                    ((AbstractButton) o).doClick(); 
                }
                else 
                    JOptionPane.showMessageDialog(window, "No available handler for action '"+name+"'     "); 
            }
        }
    }

    public boolean isWindowExist(String id) {
        return false;
    }

    public SubWindow addWindow(String id, JComponent c, Map params) 
    {
        NBSubWindow win = null;
        if (!windows.containsKey(id))
        {
            win = new NBSubWindow(this, id);
            win.add(c);

            String closeable = params.get("canclose")+"";
            if ("false".equalsIgnoreCase(closeable))
                win.setCloseable(false);
            else
                win.setCloseable(true);

            String title = (String) params.get("title");
            if (title == null || title.trim().length() == 0) title = id;

            win.setDisplayName(title);
            windows.put(id, win);

            win.open();
        }
        else {
            win = (NBSubWindow) windows.get(id);
        }
        
        win.requestActive(); 
        
        final JComponent jcc = c;
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                try {
                    jcc.requestFocus();
                } catch(Exception ign) {;} 
            }
        });
        return win; 
    }

    public SubWindow getWindow(String id) 
    {
        return (SubWindow) windows.get(id);
    }

    public void removeWindow(String id) 
    {
        windows.remove(id);
    }

    public void setMenuBar(JMenuBar m) 
    {
        if (m == null) m = new JMenuBar();

        List list = new ArrayList();
        for (int i=0; i<m.getComponentCount(); i++)
        {
            JMenu jm = m.getMenu(i);
            jm.getComponentCount(); 
            
            if (jm.getMenuComponentCount() == 0) 
                list.add(m.getMenu(i));
        }
        
        while (!list.isEmpty())
        {
            JMenu mnu = (JMenu) list.remove(0);
            m.remove(mnu);
        }

        window.setJMenuBar(m);
        SwingUtilities.updateComponentTreeUI(m);
    }

    private void addToolbarComponent(Component comp)
    {
        JComponent jc = (JComponent) NBManager.getInstance().getToolbarView();
        if (jc == null) return;
        
        if (comp != null) jc.add(comp);
    }
    
    public void add(Component comp, Object constraints)
    {
        if (comp == null) return;
        
        if ("toolbar".equals(constraints))
        {
            JComponent jc = (JComponent) NBManager.getInstance().getToolbarView();
            if (jc != null) 
            {
                comp.setName("toolbarpanel"); 
                jc.add(comp);
            }
        }
    }
    
    public void remove(Object constraints) 
    {
        boolean clearAll = false;
        if (constraints == null) clearAll = true;
        
        if ("toolbar".equals(constraints) || clearAll)
        {
            JComponent jc = (JComponent) NBManager.getInstance().getToolbarView();
            if (jc != null) jc.removeAll(); 
        }        
    }    

    public void setTitle(String title) {
        window.setTitle(mainTitle + " " + title);
    }

    public void close() { 
        NBLifecycleManager.getInstance().exit(); 
    } 
    
    public void setMainTitle(String title) {
        this.mainTitle = title;
    }
    
}
