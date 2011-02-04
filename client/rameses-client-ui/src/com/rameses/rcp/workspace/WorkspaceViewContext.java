/*
 * WorkspaceViewContext.java
 *
 * Created on February 3, 2011, 4:16 PM
 * @author jaycverg
 */

package com.rameses.rcp.workspace;

import com.rameses.platform.interfaces.SubWindow;
import com.rameses.platform.interfaces.ViewContext;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.ControlContainer;
import com.rameses.rcp.ui.UIControl;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;


public class WorkspaceViewContext extends JPanel implements ViewContext {
    
    private ViewContext viewCtx;
    private Workspace workspace;
    private String title;
    
    
    public WorkspaceViewContext(String title, Workspace workspace) {
        this.workspace = workspace;
        this.viewCtx = ((WorkspaceWindow)workspace.getMainWindow()).getViewContext();
        this.title = title;
        init((Component)workspace.getMainWindow());
    }
    
    public WorkspaceViewContext(String title, Workspace workspace, ViewContext viewCtx) {
        this.workspace = workspace;
        this.viewCtx = viewCtx;
        this.title = title;
        init((Component) viewCtx);
    }
    
    private void init(Component comp) {
        super.setLayout(new BorderLayout());
        
        Class<? extends JComponent> template = workspace.getTemplate();
        String tplTitle = workspace.getTemplateTitle();
        if( template != null ) {
            try {
                JComponent tpl = template.newInstance();
                if( tplTitle != null && tpl instanceof WorkspaceDefaultTpl ) {
                    ((WorkspaceDefaultTpl) tpl).setTitle(tplTitle);
                }
                
                Binding b = new Binding();
                bindRecursive(tpl, b);
                
                Map bean = new HashMap();
                bean.put("conf", workspace.getConf());
                bean.put("title", title);
                b.setBean(bean);
                b.refresh();
                
                tpl.add(comp);
                comp = tpl;
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        
        super.add(comp);
    }
    
    private void bindRecursive(Container comp, Binding binding) {
        for( Component c: comp.getComponents()) {
            if( c instanceof UIControl ) {
                UIControl uic = (UIControl)c;
                uic.setBinding(binding);
                binding.register(uic);
                
                if( c instanceof ControlContainer && ((ControlContainer) c).isHasNonDynamicContents() && c instanceof Container )
                    bindRecursive( (Container)c, binding );
                
            } else if( c instanceof Container ) {
                bindRecursive( (Container)c, binding );
            }
        }
    }
    
    public void setLayout(LayoutManager mgr){}
    
    public boolean close() {
        if( viewCtx != null ) return viewCtx.close();
        
        return true;
    }
    
    public void display() {
        if( viewCtx != null ) viewCtx.display();
    }
    
    public void setSubWindow(SubWindow subWindow) {
        if( viewCtx != null ) viewCtx.setSubWindow(subWindow);
    }
    
    public SubWindow getSubWindow() {
        if( viewCtx != null ) return viewCtx.getSubWindow();
        
        return null;
    }
    
}
