package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.BindingConnector;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControlSupport;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIViewPanel;
import com.rameses.rcp.ui.UISubControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author jaycverg
 */
public class XSubFormPanel extends JPanel implements UISubControl, NavigatablePanel {
    
    private String handler;
    private String[] depends;
    private int index;
    private Binding binding;
    private BindingConnector bindingConnector = new  BindingConnector();
    private Stack<UIController> controllers = new Stack();
    private boolean dynamic;
    
    private List<Binding> subBindings = new ArrayList();
    
    public XSubFormPanel() {
        super.setLayout(new BorderLayout());
        if ( Beans.isDesignTime() ) {
            setPreferredSize( new Dimension(40,40) );
            setBackground( Color.decode("#e3e3e3") );
        }
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    public Dimension getPreferredSize() {
        if ( getComponentCount() > 0 ) {
            return getComponent(0).getPreferredSize();
        } else {
            return super.getPreferredSize();
        }
    }
    
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
        if ( dynamic ) {
            buildForm();
        }
    }
    
    public void load() {
        if ( !dynamic ) {
            buildForm();
        }
    }
    
    private void buildForm() {
        controllers.clear();
        Object obj = UIControlUtil.getBeanValue(this, getHandler());
        if ( obj == null ) {
            removeAll();
            SwingUtilities.updateComponentTreeUI(this);
            return;
        }
        
        if ( !(obj instanceof Opener) )
            throw new IllegalStateException("handler should be an instanceof " + Opener.class.getName());
        
        Opener opener = (Opener) obj;
        opener = ControlSupport.initOpener( opener, binding.getController() );
        String opnrOutcome = opener.getOutcome();
        
        ControllerProvider provider = ClientContext.getCurrentContext().getControllerProvider();
        UIController controller = provider.getController(opener.getName());
        if ( controller == null )
            throw new IllegalStateException("Cannot find controller " + opener.getName());
        
        controller.setName( opener.getName() );
        controller.setId( opener.getId() );
        Object o = controller.init( opener.getParams(), opener.getAction() );
        
        if ( o != null && o instanceof String ) {
            opnrOutcome = (String) o;
        }
        
        if ( !ValueUtil.isEmpty(opnrOutcome) ) {
            controller.setCurrentView( opnrOutcome );
        }
        
        controllers.push(controller);
        renderView();
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public Stack<UIController> getControllers() {
        return controllers;
    }
    
    public void setControllers(Stack controllers){}
    
    public void renderView() {
        UIController controller = controllers.peek();
        UIViewPanel viewPanel = controller.getCurrentView();
        Binding subBinding = viewPanel.getBinding();
        subBinding.setBean(controller.getCodeBean());
        List<Binding> connectorBindings = bindingConnector.getSubBindings();
        connectorBindings.clear();
        connectorBindings.add(subBinding);
        removeAll();
        add(viewPanel);
        viewPanel.refresh();
        SwingUtilities.updateComponentTreeUI(this);
        
        subBindings.clear();
        subBindings.add( subBinding );
    }
    
    public List<Binding> getSubBindings() {
        return subBindings;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    
}
