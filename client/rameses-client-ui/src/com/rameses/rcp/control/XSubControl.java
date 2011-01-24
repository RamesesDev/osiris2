/*
 * XSubControl.java
 *
 * Created on January 22, 2011, 2:09 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.Opener;
import com.rameses.rcp.control.XSubFormPanel.SubFormContext;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.OpenerProvider;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;


public class XSubControl extends XSubFormPanel implements Validatable {
    
    private AbstractSubControlModel model;
    private boolean handlerAutoLookup = true;
    private boolean readonly;
    private boolean editable;
    private Map properties = new HashMap();
    
    
    public XSubControl() {
        super();
    }
    
    public void refresh() {
        if( model != null ) {
            model.onRefresh();
            
            Map props = model.getProperties();
            props.put("required", isRequired());
            props.put("readonly", readonly);
            props.put("editable", editable);
            props.put("enabled", isEnabled());
        }
        
        super.refresh();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    protected void buildForm() {
        Object obj = null;
        if( handlerAutoLookup ) {
            OpenerProvider provider = ClientContext.getCurrentContext().getOpenerProvider();
            List list = provider.getOpeners(getHandler(), null);
            if( list != null && list.size() > 0 )
                obj = list.get(0);
        } else {
            obj = UIControlUtil.getBeanValue(this, getHandler());
        }
        
        Opener opener = null;
        if ( obj instanceof Opener ) {
            opener = (Opener)obj;
        }
        
        //-- display support
        List<Binding> connectorBindings = bindingConnector.getSubBindings();
        connectorBindings.clear();
        if ( opener == null ) {
            subFormItems.clear();
            removeAll();
            SwingUtilities.updateComponentTreeUI(this);
            return;
        }
        
        if ( currentOpeners.size() > 0 && opener == currentOpeners.get(0) ) {
            SubFormContext sfc = subFormItems.get(0);
            sfc.renderView();
            
            //register new subBindings
            connectorBindings.addAll(getSubBindings());
        } else {
            subFormItems.clear();
            currentOpeners.clear();
            currentOpeners.add( opener );
            
            removeAll();
            SwingUtilities.updateComponentTreeUI(this);
            
            processOpener(opener);
            
            
            //register new subBindings
            connectorBindings.addAll(getSubBindings());
        }
    }
    
    private void processOpener(Opener opener) {
        UIController caller = binding.getController();
        opener.setCaller( caller );
        opener = ControlSupport.initOpener( opener, caller );
        UIController controller = opener.getController();
        
        if ( controller == null )
            throw new RuntimeException("Cannot find controller " + opener.getName());
        
        Object codeBean = controller.getCodeBean();
        if( !(codeBean instanceof AbstractSubControlModel) )
            throw new RuntimeException("Controller bean must be an instanceof " + AbstractSubControlModel.class.getName());
        
        model = (AbstractSubControlModel) codeBean;
        model.init(binding, getName());
        model.getProperties().putAll( properties );
        model.onInit();
                
        UIControllerContext uic = new UIControllerContext( controller );
        if ( !ValueUtil.isEmpty(opener.getOutcome()) ) {
            uic.setCurrentView(opener.getOutcome());
        }
        SubFormContext sfc = new SubFormContext( uic );
        subFormItems.add( sfc );
        
        add( sfc );
    }
    //</editor-fold>
    
    public boolean isHandlerAutoLookup() {
        return handlerAutoLookup;
    }
    
    public void setHandlerAutoLookup(boolean handlerAutoLookup) {
        this.handlerAutoLookup = handlerAutoLookup;
    }
    
    public boolean isRequired() {
        return property.isRequired();
    }
    
    public void setRequired(boolean required) {
        property.setRequired(required);
    }
    
    public void validateInput() {}
    public ActionMessage getActionMessage() { return null; }
    
    public Map getProperties() {
        return properties;
    }
    
    public void setProperties(Map properties) {
        this.properties = properties;
    }
    
    public boolean isReadonly() {
        return readonly;
    }
    
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }
    
    public boolean isEditable() {
        return editable;
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
}
