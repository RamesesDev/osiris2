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
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;


public class WorkspaceViewContext extends JPanel implements ViewContext {
    
    private ViewContext viewCtx;
    private Map conf;
    
    
    public WorkspaceViewContext(Map conf, WorkspaceWindow window, Class<? extends JComponent> template) {
        this.viewCtx = window.getViewContext();
        this.conf = conf;
        init(window, template);
    }
    
    public WorkspaceViewContext(Map conf, ViewContext viewCtx, Class<? extends JComponent> template) {
        this.viewCtx = viewCtx;
        this.conf = conf;
        init((Component) viewCtx, template);
    }
    
    private void init(Component comp, Class<? extends JComponent> template) {
        super.setLayout(new BorderLayout());
        
        if( template != null ) {
            try {
                JComponent tpl = template.newInstance();
                bind(tpl);
                tpl.add(comp);
                comp = tpl;
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        
        super.add(comp);
    }
    
    private void bind(JComponent comp) {
        Binding b = new Binding();
        bindRecursive(comp, b);
        b.setBean(conf);
        b.refresh();
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
