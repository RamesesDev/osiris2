package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.ui.BindingConnector;
import com.rameses.rcp.ui.ControlProperty;
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
public class XSubFormPanel extends JPanel implements UISubControl, ActiveControl {
    
    private String handler;
    private String[] depends;
    private int index;
    private Binding binding;
    private BindingConnector bindingConnector = new  BindingConnector(this);
    private boolean dynamic;
    
    private boolean multiForm;
    private JPanel multiPanel;
    private List<Binding> subBindings = new ArrayList();
    private List<SubFormContext> subFormItems = new ArrayList();
    
    private List<Opener> openers;
    
    private ControlProperty property = new ControlProperty();
    
    
    public XSubFormPanel() {
        super.setLayout(new BorderLayout());
        setOpaque(false);
        if ( Beans.isDesignTime() ) {
            setPreferredSize( new Dimension(40,40) );
            setBackground( Color.decode("#e3e3e3") );
        }
    }
    
    public XSubFormPanel(Opener o) {
        this();
        getOpeners().add(o);
        multiForm = false;
    }
    
    public XSubFormPanel(List<Opener> o) {
        this();
        this.openers = o;
        multiForm = true;
    }
    
    public List<Opener> getOpeners() {
        if ( openers == null ) openers = new ArrayList();
        return openers;
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
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void buildForm() {
        List<Binding> connectorBindings = bindingConnector.getSubBindings();
        connectorBindings.clear();
        subFormItems.clear();
        removeAll();
        SwingUtilities.updateComponentTreeUI(this);
        
        Object obj = null;
        
        //this is usually set by XTabbedPane or
        //other controls that used XSubForm internally
        if ( getOpeners().size() > 0 ) {
            obj = getOpeners();
        } else {
            obj = UIControlUtil.getBeanValue(this, getHandler());
            multiForm = true; //reset, check based on passed value
        }
        if ( obj == null ) return;
        
        List<Opener> openers = new ArrayList();
        
        if ( obj instanceof Collection ) {
            for(Object o: (Collection) obj) {
                openers.add( (Opener)o );
            }
            
        } else if ( obj.getClass().isArray() ) {
            for(Object o: (Object[]) obj) {
                openers.add( (Opener)o );
            }
            
        } else if ( obj instanceof Opener ) {
            openers.add( (Opener)obj );
            multiForm = false;
            
        } else {
            throw new IllegalStateException("XSubFormPanel handler must be an instance of Opener, Opener[], or List<Opener>");
        }
        
        if ( openers.size() == 0 ) return;
        
        //check if is a multi form
        if ( multiForm ) {
            multiPanel = new JPanel();
            multiPanel.setOpaque(false);
            multiPanel.setLayout( new BoxLayout(multiPanel, BoxLayout.Y_AXIS) );
            super.add(multiPanel, BorderLayout.NORTH);
        }
        
        for (Opener opener : openers) {
            addOpener(opener);
        }
        
        //register new subBindings
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
    //</editor-fold>
    
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
    
    public String getCaption() {
        if ( property.getCaption() == null ) {
            if ( !ValueUtil.isEmpty(getName()))
                return getName();
            else
                return handler;
        }
        return property.getCaption();
    }
    
    public void setCaption(String caption) {
        property.setCaption(caption);
    }
    
    public char getCaptionMnemonic() {
        return property.getCaptionMnemonic();
    }
    
    public void setCaptionMnemonic(char c) {
        property.setCaptionMnemonic(c);
    }
    
    public int getCaptionWidth() {
        return property.getCaptionWidth();
    }
    
    public void setCaptionWidth(int width) {
        property.setCaptionWidth(width);
    }
    
    public boolean isShowCaption() {
        return property.isShowCaption();
    }
    
    public void setShowCaption(boolean showCaption) {
        property.setShowCaption(showCaption);
    }
    
    public ControlProperty getControlProperty() {
        return property;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  SubFormContext (class)  ">
    private class SubFormContext extends UIControllerPanel {
        
        SubFormContext(UIControllerContext controller) {
            super(controller);
            setOpaque(false);
            setName(XSubFormPanel.this.getName());
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
