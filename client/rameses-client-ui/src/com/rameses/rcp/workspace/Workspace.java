package com.rameses.rcp.workspace;

import com.rameses.platform.interfaces.AppLoader;
import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.framework.ClientContext;
import java.net.URLClassLoader;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author jaycverg
 */
public class Workspace implements Platform {
    
    public static final Workspace create(Map conf) {
        try {
            ClientContext ctx = ClientContext.getCurrentContext();
            Workspace ws = new Workspace();
            ws.parent = ctx.getPlatform();
            
            URLClassLoader moduleCL = (URLClassLoader) ctx.getClassLoader();
            ClassLoader platformCL = ws.parent.getClass().getClassLoader();
            ClassLoader subLoader = new URLClassLoader(moduleCL.getURLs(), platformCL);
            
            String appLoaderClass = (String) ctx.getAppEnv().get("app.loader");
            AppLoader appLoader = (AppLoader) subLoader.loadClass(appLoaderClass).newInstance();
            
            Map newEnv = new HashMap(ctx.getAppEnv());
            
            if( conf != null ) {
                if( conf.get("env") != null ) {
                    newEnv.put("CLIENT_ENV", conf.get("env"));
                }
                if( conf.get("permissions") != null ) {
                    newEnv.put("CLIENT_PERMISSIONS", conf.get("permissions"));
                }
                if( conf.get("loaderType") != null ) {
                    newEnv.put("LOADER_TYPE", conf.get("loaderType"));
                }
                if( conf.get("showStatusbar") != null ) {
                    ws.showStatusbar = "true".equals(conf.get("showStatusbar")+"");
                }
                if( conf.get("showMenubar") != null ) {
                    ws.showMenubar = "true".equals(conf.get("showMenubar")+"");
                }
                if( conf.get("showToolbar") != null ) {
                    ws.showToolbar = "true".equals(conf.get("showToolbar")+"");
                }
                if( conf.get("titlePrefix") != null ) {
                    ws.workspaceName = conf.get("titlePrefix").toString();
                }
            }
            
            appLoader.load(subLoader, newEnv, ws);
            
            return ws;
            
        } catch(Exception e) {
            throw new RuntimeException("Workspace.create: " + e.getMessage(), e);
        }
    }
    
    
    private String workspaceName;
    private String workspaceId = "workspace-" + new UID() + ":";
    private Platform parent;
    
    private WorkspaceWindow mainWindow;
    
    private boolean showMenubar;
    private boolean showToolbar;
    private boolean showStatusbar;
    
    
    public Workspace() {
        mainWindow = new WorkspaceWindow();
    }
    
    public void show(String title) {
        mainWindow.setMenubarVisible( showMenubar );
        mainWindow.setToolbarVisible( showToolbar );
        
        title = (title!=null)? title : (workspaceName!=null)? workspaceName : workspaceId;
        Map props = new HashMap();
        props.put("title", title);
        props.put("id", workspaceId);
        parent.showWindow(null, mainWindow, props);
    }
    
    public void showStartupWindow(JComponent actionSource, JComponent comp, Map properties) {
        mainWindow.setComponent(comp, WorkspaceWindow.CONTENT);
    }
    
    public void showWindow(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        properties.put("id", workspaceId + id);
        
        String title = (String) properties.get("title");
        properties.put("title", workspaceName + ":" + (title!=null? title : id));
        
        parent.showWindow(actionSource, comp, properties);
    }
    
    public void showPopup(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        properties.put("id", workspaceId + id);
        
        String title = (String) properties.get("title");
        properties.put("title", workspaceName + ":" + (title!=null? title : id));
        
        parent.showPopup(actionSource, comp, properties);
    }
    
    public void showFloatingWindow(JComponent owner, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        properties.put("id", workspaceId + id);
        
        String title = (String) properties.get("title");
        properties.put("title", workspaceName + ":" + (title!=null? title : id));
        
        parent.showFloatingWindow(owner, comp, properties);
    }
    
    public void showError(JComponent actionSource, Exception e) {
        parent.showError(actionSource, e);
    }
    
    public boolean showConfirm(JComponent actionSource, Object message) {
        return parent.showConfirm(actionSource, message);
    }
    
    public void showInfo(JComponent actionSource, Object message) {
        parent.showInfo(actionSource, message);
    }
    
    public void showAlert(JComponent actionSource, Object message) {
        parent.showAlert(actionSource, message);
    }
    
    public Object showInput(JComponent actionSource, Object message) {
        return parent.showInput(actionSource, message);
    }
    
    public MainWindow getMainWindow() {
        return mainWindow;
    }
    
    public boolean isWindowExists(String id) {
        return parent.isWindowExists(workspaceId + id);
    }
    
    public void closeWindow(String id) {
        parent.closeWindow(workspaceId + id);
    }
    
    public void activateWindow(String id) {
        parent.activateWindow(workspaceId + id);
    }
    public void shutdown() {}
    public void logoff() {}
    public void lock() {}
    public void unlock() {}
    
}
