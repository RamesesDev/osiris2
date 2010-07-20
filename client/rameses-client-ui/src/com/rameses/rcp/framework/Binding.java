
package com.rameses.rcp.framework;

import com.rameses.classutils.AnnotationFieldHandler;
import com.rameses.classutils.ClassDefUtil;
import com.rameses.rcp.control.XButton;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.util.ValueUtil;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author jaycverg
 */
public class Binding {
    
    private Object bean;
    private List<UIControl> controls = new ArrayList();
    private Map<String, Set<UIControl>> depends = new Hashtable();
    private List<Validatable> validatables = new ArrayList();
    private List<BindingListener> listeners = new ArrayList();
    private ChangeLog changeLog = new ChangeLog();
    private XButton defaultButton;
    
    private List<UIControl> _depends = new ArrayList();
    private boolean _initialized = false;
    
    private AnnotationFieldHandler fieldInjector = new BindingAnnotationHandler();
    private KeyListener changeLogKeySupport = new ChangeLogKeySupport();
    
    
    public void register( UIControl control ) {
        controls.add( control );
        if( control.getDepends() != null ) {
            _depends.add( control );
        }
        if( control instanceof Validatable ) {
            validatables.add( (Validatable)control );
        }
        if( control instanceof XButton && defaultButton == null ) {
            XButton btn = (XButton) control;
            if ( btn.isDefaultCommand() )
                defaultButton = btn;
        }
    }
    
    
    public void init() {
        if ( _initialized ) return;
        
        _initialized = true;
        Collections.sort( controls );
        Collections.sort( validatables );
        for( UIControl u : controls ) {
            String n = u.getName();
            if( n==null || n.trim().length() == 0 ) continue;
            for( UIControl c: _depends ) {
                if( u == c ) continue;
                for( String s: c.getDepends() ) {
                    if(n.matches(s)) {
                        if(! depends.containsKey(n)) depends.put(n, new HashSet<UIControl>());
                        depends.get(n).add( c );
                    }
                }
            }
            
        }
    }
    
    public void notifyDepends( UIControl u ) {
        Set<UIControl> refreshed = new HashSet();
        if ( !ValueUtil.isEmpty(u.getName()) && depends.containsKey(u.getName()) ) {
            for( UIControl uu : depends.get(u.getName() ) ) {
                _refresh( uu, refreshed );
            }
        }
        refreshed.clear();
        refreshed = null;
        
        for (BindingListener bl : listeners) {
            bl.notifyDepends(u, this);
        }
    }
    
    public void refresh() {
        Set<UIControl> refreshed = new HashSet();
        for( UIControl uu : controls ) {
            _refresh( uu, refreshed );
        }
        refreshed.clear();
        refreshed = null;
    }
    
    private void _refresh( UIControl u, Set refreshed ) {
        if( refreshed.add(u) ) {
            u.refresh();
            String name = u.getName();
            if( !ValueUtil.isEmpty(name) && depends.containsKey(name) ) {
                for( UIControl uu : depends.get( u.getName() )) {
                    _refresh( uu, refreshed );
                }
            }
        }
    }
    
    public void validate(ActionMessage actionMessage) {
        for ( Validatable vc: validatables ) {
            vc.validateInput();
            ActionMessage ac = vc.getActionMessage();
            if ( ac.hasMessages() ) actionMessage.addMessage(ac);
        }
        
        for (BindingListener bl: listeners ) {
            bl.validate(actionMessage, this);
        }
    }
    
    public void update() {
        //clear changeLog
        if ( changeLog != null ) changeLog.clear();
    }
    
    public void addBindingListener(BindingListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(BindingListener listener) {
        listeners.remove(listener);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public boolean isInitialized() {
        return _initialized;
    }
    
    public Object getBean() {
        return bean;
    }
    
    public void setBean(Object bean) {
        this.bean = bean;
        ClassDefUtil.getInstance().injectFields(bean, fieldInjector);
        _load();
    }
    
    private void _load() {
        for ( UIControl c: controls ) {
            c.load();
        }
    }
    
    public ChangeLog getChangeLog() {
        return changeLog;
    }
    
    public void setChangeLog(ChangeLog changeLog) {
        this.changeLog = changeLog;
    }
    
    public XButton getDefaultButton() {
        return defaultButton;
    }
    
    public void setDefaultButton(XButton defaultButton) {
        this.defaultButton = defaultButton;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  BindingAnnotationHandler (class)  ">
    private class BindingAnnotationHandler implements AnnotationFieldHandler {
        
        public Object getResource(Field f, Annotation a) throws Exception {
            Class type = a.annotationType();
            if ( type == com.rameses.common.annotations.Binding.class ) {
                return Binding.this;
            } else if (type == com.rameses.common.annotations.ChangeLog.class ) {
                return Binding.this.getChangeLog();
            }
            return null;
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  ChangeLogKeySupport (class)  ">
    private class ChangeLogKeySupport implements KeyListener {
        
        public void keyTyped(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {}
        
        public void keyReleased(KeyEvent e) {
            if ( e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z ) {
                if ( changeLog.hasChanges() ) {
                    changeLog.undo();
                    refresh();
                }
            }
        }
        
    }
    //</editor-fold>
    
}
