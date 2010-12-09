/*
 * OsirisSessionContext.java
 *
 * Created on April 28, 2010, 8:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.AppContext;
import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import com.rameses.platform.interfaces.MainWindow;
import com.rameses.rcp.framework.ClientContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenuBar;

/**
 *
 * @author elmo
 */
public class OsirisSessionContext extends SessionContext {
    
    private boolean offline = false;
    
    private Map profile = new HashMap();
    
    public OsirisSessionContext(AppContext ctx) {
        super(ctx);
        setSecurityProvider( new OsirisSecurityProvider() );
    }
    
    public void load() {
        reload();
    }
    
    public void reload() 
    {
        super.invokers.clear();
        super.folderIndex.clear();
        
        MainWindow m = ClientContext.getCurrentContext().getPlatform().getMainWindow();
        JMenuBar menuBar = MenuUtil.getMenuBar("menu");
        m.setComponent(menuBar, MainWindow.MENUBAR);
        m.setTitle((String) super.getEnv().get("app.title"));
        m.setComponent(ToolbarUtil.getToolBar(), MainWindow.TOOLBAR);
        m.setComponent(StatusbarUtil.getStatusbar(), MainWindow.STATUSBAR);
    }
    
    public List getPermissions() {
        return ((OsirisSecurityProvider)getSecurityProvider()).getPermissions();
    }
    
    public Map getProfile() {
        return profile;
    }
    
    public void setProfile(Map profile) {
        this.profile = profile;
    }

    public boolean checkInvoker(Invoker inv) {
        if( offline ) {
            String a = (String)inv.getProperties().get("allowOffline");
            if(a==null) return false;
            try {
                return Boolean.parseBoolean( a );
            }
            catch(Exception ign) {
                return false;
            }
        }
        else{
            return true;
        }
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    
}
