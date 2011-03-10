package com.rameses.rcp.workspace;

import com.rameses.platform.interfaces.AppLoader;
import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.Platform;
import com.rameses.platform.interfaces.SubPlatform;
import com.rameses.platform.interfaces.ViewContext;
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
public class Workspace implements SubPlatform {
    
    public static final Workspace create(Map conf) {
        ClientContext ctx = ClientContext.getCurrentContext();
        Workspace ws = new Workspace();
        Platform parent = ctx.getPlatform();
        
        if( parent instanceof SubPlatform ) {
            parent = ((SubPlatform) parent).getParent();
        }
        
        ws.parent = parent;
        
        Map newEnv = new HashMap(ctx.getAppEnv());
        if( conf != null ) {
            ws.conf = conf;
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
            if( conf.get("pageTemplate") != null ) {
                ws.setTemplate( conf.get("pageTemplate") );
            }
            if( conf.get("templateTitle") != null ) {
                ws.templateTitle = conf.get("templateTitle").toString();
            }
            if( conf.get("params") != null ) {
                newEnv.put("PROPERTIES", conf.get("params"));
            }
        }
        
        ws.virtualEnv = newEnv;
        return ws;
    }
    
    
    private String workspaceName;
    private String workspaceId = "workspace-" + new UID() + ":";
    private Platform parent;
    private Map conf = new HashMap();
    
    private WorkspaceWindow mainWindow;
    private Class<? extends JComponent> pageTemplate = WorkspaceDefaultTpl.class;
    private String templateTitle;
    
    private boolean showMenubar;
    private boolean showToolbar;
    private boolean showStatusbar;
    
    private Map virtualEnv = new HashMap();
    
    //internal flag
    private boolean loaded;
    
    
    public Workspace() {
        mainWindow = new WorkspaceWindow();
    }
    
    public void show(String title) {
        load();
        
        mainWindow.setMenubarVisible( showMenubar );
        mainWindow.setToolbarVisible( showToolbar );
        
        title = (title!=null)? title : (workspaceName!=null)? workspaceName : workspaceId;
        Map props = new HashMap();
        props.put("title", title);
        props.put("id", workspaceId);
        parent.showWindow(null, new WorkspaceViewContext(title, this), props);
    }
    
    private void load() {
        if( loaded ) return;
        
        try {
            ClientContext ctx = ClientContext.getCurrentContext();
            URLClassLoader moduleCL = (URLClassLoader) ctx.getClassLoader();
            ClassLoader subLoader = new URLClassLoader(moduleCL.getURLs(), moduleCL.getParent());
            
            String appLoaderClass = (String) ctx.getAppEnv().get("app.loader");
            AppLoader appLoader = (AppLoader) subLoader.loadClass(appLoaderClass).newInstance();
            appLoader.load(subLoader, virtualEnv, this);
            
            loaded = true;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    void setTemplate(Object tpl) {
        try {
            if( tpl == null ); //do nothing
            else if( tpl instanceof String ) {
                pageTemplate = (Class<? extends JComponent>) getClass().getClassLoader().loadClass( tpl.toString() );
            } else if( tpl instanceof Class ) {
                pageTemplate = (Class<? extends JComponent>) tpl;
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    Class<? extends JComponent> getTemplate() {
        return pageTemplate;
    }
    
    Map getConf() {
        return conf;
    }
    
    String getTemplateTitle() {
        return templateTitle;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Platform properties/methods  ">
    public void showStartupWindow(JComponent actionSource, JComponent comp, Map properties) {
        mainWindow.setComponent(comp, WorkspaceWindow.CONTENT);
    }
    
    public void showWindow(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        properties.put("id", workspaceId + id);
        
        String title = (String) properties.get("title");
        properties.put("title", (workspaceName!=null? workspaceName + ":" : "") + (title!=null? title : id));
        
        if( comp instanceof ViewContext )
            comp = new WorkspaceViewContext(title, this, (ViewContext) comp);
        
        parent.showWindow(actionSource, comp, properties);
    }
    
    public void showPopup(JComponent actionSource, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        properties.put("id", workspaceId + id);
        
        String title = (String) properties.get("title");
        properties.put("title", (workspaceName!=null? workspaceName + ":" : "") + (title!=null? title : id));
        
        if( comp instanceof ViewContext )
            comp = new WorkspaceViewContext(title, this, (ViewContext) comp);
        
        parent.showPopup(actionSource, comp, properties);
    }
    
    public void showFloatingWindow(JComponent owner, JComponent comp, Map properties) {
        String id = (String) properties.get("id");
        properties.put("id", workspaceId + id);
        
        String title = (String) properties.get("title");
        properties.put("title", (workspaceName!=null? workspaceName + ":" : "") + (title!=null? title : id));
        
        if( comp instanceof ViewContext )
            comp = new WorkspaceViewContext(title, this, (ViewContext) comp);
        
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
    //</editor-fold>
    
    public Platform getParent() {
        return parent;
    }
    
    public String getId() {
        return workspaceId;
    }
    
    public void setId(String workspaceId) {
        this.workspaceId = workspaceId;
    }
    
    public String getWorkspaceName() {
        return workspaceName;
    }
    
    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }
    
}
