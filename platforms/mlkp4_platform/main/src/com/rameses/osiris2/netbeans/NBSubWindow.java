package com.rameses.osiris2.netbeans;

import com.rameses.rcp.interfaces.SubWindow;
import com.rameses.rcp.interfaces.ViewContext;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.windows.TopComponent;

public class NBSubWindow extends TopComponent implements SubWindow 
{
    private NBMainWindow mainWindow;
    private String preferredID;
    private boolean closeable = true;
    private boolean bypassVerifyClose; 
    
    public NBSubWindow(NBMainWindow mainWindow, String preferredID) 
    {
        this.mainWindow = mainWindow;
        this.preferredID = preferredID;
        setLayout(new BorderLayout());
        
        ActionListener closeAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) { closeWindow(); }
        };
        registerKeyboardAction(closeAction, KeyStroke.getKeyStroke("ctrl W"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    public void setCloseable(boolean closeable) { this.closeable = closeable; }    
    
    protected String preferredID() { return preferredID; }
    
    public int getPersistenceType() { return PERSISTENCE_NEVER; }
    
    public boolean canClose() 
    { 
        if (!closeable) return false;
        if (!bypassVerifyClose) 
        {
            Component c = getComponent(0);
            if (c instanceof ViewContext)
                return ((ViewContext) c).close(); 
        }
        return true;
    }

    protected void componentClosed() 
    {
        super.componentClosed();
        mainWindow.removeWindow(preferredID);        
    }

    public void closeWindow() 
    {
        if (canClose())
        {
            try
            {
                bypassVerifyClose = true;
                close(); 
            }
            catch(RuntimeException rex) { throw rex; }
            catch(Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
            finally {
                bypassVerifyClose = false;
            }
        }
    }
}
