package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.BindingConnector;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIViewPanel;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.List;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author jaycverg
 */
public class XSubFormPanel extends JPanel implements UIControl, NavigatablePanel {
    
    private String handler;
    private String[] depends;
    private int index;
    private Binding binding;
    private BindingConnector bindingConnector = new  BindingConnector();
    private Stack<UIController> controllers = new Stack();
    
    public XSubFormPanel() {
        super.setLayout(new BorderLayout());
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
        bindingConnector.setParentBinding(binding);
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public void refresh() {
    }
    
    public void load() {
        Object obj = UIControlUtil.getBeanValue(this, getHandler());
        if ( obj == null ) return;
        
        if ( !(obj instanceof Opener) )
            throw new IllegalStateException("handler should be an instanceof " + Opener.class.getName());
        
        Opener opener = (Opener) obj;
        ControllerProvider provider = ClientContext.getCurrentContext().getControllerProvider();
        UIController controller = provider.getController(opener.getName());
        controllers.push(controller);
        renderView();
    }
    
    public int compareTo(Object o) {
        if ( o == null || !(o instanceof UIControl) ) return 0;
        return this.index - ((UIControl) o).getIndex();
    }
    
    public Stack<UIController> getControllers() {
        return controllers;
    }
    
    public void renderView() {
        UIController controller = controllers.peek();
        UIViewPanel viewPanel = controller.getCurrentView();
        Binding subBinding = viewPanel.getBinding();
        subBinding.setBean(controller.getCodeBean());
        List<Binding> subBindingList = bindingConnector.getSubBindings();
        subBindingList.clear();
        subBindingList.add(subBinding);
        removeAll();
        add(viewPanel);
        SwingUtilities.updateComponentTreeUI(this);
        viewPanel.refresh();
    }
    
}
