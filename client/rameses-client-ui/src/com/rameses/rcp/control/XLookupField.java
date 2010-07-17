package com.rameses.rcp.control;

import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.common.LookupModel;
import com.rameses.rcp.common.LookupSelector;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Windhel
 */

public class XLookupField extends AbstractIconedTextField implements LookupSelector {
    
    private String handler;
    private LookupModel lookupModel;
    private Object selectedValue;
    private UIController lookupController;
    private String expression;
    private boolean transerFocusOnSelect = true;
    
    private XLookupSupport support = new XLookupSupport();
    
    public XLookupField() {
        super("com/rameses/rcp/icons/search.png");
        setOrientation("RIGHT");
        addFocusListener( support );
        addKeyListener( support );
    }
    
    public void actionPerformed(){
        fireLookup();
    }
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        if ( value != null ) {
            selectedValue = value;
            if ( !ValueUtil.isEmpty(expression) ) {
                value = UIControlUtil.evaluateExpr(value, expression);
            }
        }
        
        setValue(value);
    }
    
    public void load() {
        super.load();
        if ( ValueUtil.isEmpty(handler) ) return;
        
        Object o = UIControlUtil.getBeanValue(this, handler);
        if( o instanceof LookupModel ) {
            lookupModel = (LookupModel) o;
            
        } else {
            Opener opener = null;
            //check if instanceof String, then load the opener.
            if( o instanceof Opener ) {
                opener = (Opener)o;
            } else {
                opener = new Opener(handler);
            }
            
            if(opener ==null)
                throw new IllegalStateException("Lookup Handler must reference an Opener object");
            
            ControllerProvider cp = ClientContext.getCurrentContext().getControllerProvider();
            lookupController = cp.getController( opener.getName() );
            if( lookupController == null ) {
                throw new IllegalStateException("Lookup Controller must be valid");
            }
            
            if( !(lookupController.getCodeBean() instanceof LookupModel) )
                throw new IllegalStateException("Lookup Handler code bean must be an instanceof LookupListModel");
            
            lookupController.setTitle( opener.getCaption() );
            lookupController.setId( opener.getId() );
            lookupModel = (LookupModel) lookupController.getCodeBean();
        }
    }
    
    private void fireLookup() {
        try {
            lookupModel.setSelector(this);
            boolean show = lookupModel.show( getText() );
            if( show ) {
                UIController c = lookupController;
                if ( c == null ) return; //should use a default lookup handler
                
                UIControllerPanel uip = new UIControllerPanel(c);
                
                Map props = new HashMap();
                props.put("id", c.getId());
                props.put("title", c.getTitle());
                
                Platform platform = ClientContext.getCurrentContext().getPlatform();
                platform.showPopup(this, uip, props);
            }
        } catch(Exception e) {
            e.printStackTrace();
            MsgBox.err(e);
        }
    }
    
    public void select(Object o) {
        selectedValue = o;
        UIInputUtil.updateBeanValue(this);
        this.refresh();
        support.setDirty(false);
        
        if ( transerFocusOnSelect )
            this.transferFocus();
        else
            this.requestFocus();
    }
    
    public void cancelSelection() {
        Object value = UIControlUtil.getBeanValue(this);
        this.refresh();
        support.setDirty(false);
        this.requestFocus();
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public Object getValue() {
        return selectedValue;
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    public boolean isTranserFocusOnSelect() {
        return transerFocusOnSelect;
    }
    
    public void setTranserFocusOnSelect(boolean transerFocusOnSelect) {
        this.transerFocusOnSelect = transerFocusOnSelect;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  XLookupSupport (class)  ">
    private class XLookupSupport implements FocusListener, KeyListener {
        
        private boolean dirty;
        
        public void focusGained(FocusEvent e) {
            dirty = false;
        }
        
        public void focusLost(FocusEvent e) {
            if ( dirty ) {
                refresh();
                dirty = false;
            }
        }
        
        public void keyTyped(KeyEvent e) {
            dirty = true;
        }
        
        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
        
        public void setDirty(boolean dirty) {
            this.dirty = dirty;
        }
        
    }
    //</editor-fold>
    
}
