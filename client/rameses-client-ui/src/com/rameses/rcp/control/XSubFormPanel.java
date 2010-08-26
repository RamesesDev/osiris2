package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.BindingConnector;
import com.rameses.rcp.framework.ControlSupport;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.ui.UISubControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author jaycverg
 */
public class XSubFormPanel extends JPanel implements UISubControl {
    
    private String handler;
    private String[] depends;
    private int index;
    private Binding binding;
    private BindingConnector bindingConnector = new  BindingConnector();
    private boolean dynamic;
    
    private boolean multiForm;
    private JPanel multiPanel;
    private List<Binding> subBindings = new ArrayList();
    private List<SubFormContext> subFormItems = new ArrayList();
    
    
    public XSubFormPanel() {
        super.setLayout(new BorderLayout());
        if ( Beans.isDesignTime() ) {
            setPreferredSize( new Dimension(40,40) );
            setBackground( Color.decode("#e3e3e3") );
        }
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    public Component add(Component comp) {
        if ( multiForm ) {
            return multiPanel.add( comp );
        } else {
            return super.add( comp );
        }
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
        subFormItems.clear();
        removeAll();
        SwingUtilities.updateComponentTreeUI(this);
        
        Object obj = UIControlUtil.getBeanValue(this, getHandler());
        if ( obj == null ) return;
        
        String errMsg = "XSubFormPanel handler must be an instance of Opener, Opener[], or List<Opener>";
        List<Opener> openers = new ArrayList();
        multiForm = true;
        
        //get handler
        if ( obj instanceof Collection ) {
            for(Object o: (Collection) obj) {
                if ( !(o instanceof Opener) ) {
                    throw new IllegalStateException(errMsg);
                }
                openers.add( (Opener)o );
            }
            
        } else if ( obj.getClass().isArray() ) {
            for(Object o: (Object[]) obj) {
                if ( !(o instanceof Opener) ) {
                    throw new IllegalStateException(errMsg);
                }
                openers.add( (Opener)o );
            }
            
        } else if ( obj instanceof Opener ) {
            openers.add( (Opener)obj );
            multiForm = false;
            
        } else {
            throw new IllegalStateException(errMsg);
        }
        
        if ( openers.size() == 0 ) return;
        
        //check if is a multi form
        if ( multiForm ) {
            multiPanel = new JPanel();
            multiPanel.setLayout( new BoxLayout(multiPanel, BoxLayout.Y_AXIS) );
            super.add(multiPanel, BorderLayout.NORTH);
        }
        
        for (Opener opener : openers) {
            addOpener(opener);
        }
        
        //register subBindings
        List<Binding> connectorBindings = bindingConnector.getSubBindings();
        connectorBindings.clear();
        connectorBindings.addAll(getSubBindings());
    }
    
    private void addOpener(Opener opener) {
        UIController caller = binding.getController();
        opener.setCaller( caller );
        opener = ControlSupport.initOpener( opener, caller );
        UIController controller = opener.getController();
        
        if ( controller == null )
            throw new IllegalStateException("Cannot find controller " + opener.getName());
        
        UIControllerContext uic = new UIControllerContext( controller );
        if ( !ValueUtil.isEmpty(opener.getOutcome()) ) {
            uic.setCurrentView(opener.getOutcome());
        }
        SubFormContext sfc = new SubFormContext( uic );
        subFormItems.add( sfc );
        add( sfc );
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public boolean focusFirstInput() {
        for( Binding b: getSubBindings() ) {
            if ( b.focusFirstInput() ) return true;
        }
        return false;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
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
    
    public List<Binding> getSubBindings() {
        List<Binding> list = new ArrayList();
        
        for (SubFormContext sfc: subFormItems) {
            list.add( sfc.getCurrentController().getCurrentView().getBinding() );
        }
        
        return list;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  SubFormContext (class)  ">
    private class SubFormContext extends UIControllerPanel {
        
        SubFormContext(UIControllerContext controller) {
            super(controller);
        }
        
        public void renderView() {
            super.renderView();
            
            List<Binding> connectorBindings = bindingConnector.getSubBindings();
            connectorBindings.clear();
            connectorBindings.addAll(getSubBindings());
        }
        
    }
    //</editor-fold>
    
}
