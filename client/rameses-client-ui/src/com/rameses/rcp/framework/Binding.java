
package com.rameses.rcp.framework;

import com.rameses.classutils.AnnotationFieldHandler;
import com.rameses.classutils.ClassDefUtil;
import com.rameses.rcp.annotations.Close;
import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.control.XButton;
import com.rameses.rcp.ui.UIComposite;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.UISubControl;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.common.MethodResolver;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    
    /**
     * this is used when referencing controller properties
     * such as controller name, id, and title
     */
    private UIController controller;
    
    /**
     * index of all controls in this binding
     * this is used to speed up in finding a control by name
     */
    private Map<String, UIControl> controlsIndex = new Hashtable();
    
    /**
     * 1. reference of all controls that can aquire default focus
     *    when the window is shown or during page navigation
     * 2. this reference contains UIInput and UISubControl only
     */
    private List<UIControl> focusableControls = new ArrayList();
    
    private List<UIControl> controls = new ArrayList();
    private Map<String, Set<UIControl>> depends = new Hashtable();
    private List<Validatable> validatables = new ArrayList();
    private List<BindingListener> listeners = new ArrayList();
    private ChangeLog changeLog = new ChangeLog();
    private XButton defaultButton;
    private StyleRule[] styleRules;
    
    //flags
    private List<UIControl> _depends = new ArrayList();
    private boolean _initialized = false;
    
    private AnnotationFieldHandler fieldInjector = new BindingAnnotationHandler();
    private KeyListener changeLogKeySupport = new ChangeLogKeySupport();
    private List<String> closeMethods;
    
    /**
     * can be used by UIControls to store values
     * related to this Binding context
     */
    private Map properties = new HashMap();
    
    
    //<editor-fold defaultstate="collapsed" desc="  control binding  ">
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
        if( !ValueUtil.isEmpty(control.getName()) ) {
            controlsIndex.put(control.getName(), control);
        }
    }
    
    
    public void init() {
        if ( _initialized ) return;
        
        _initialized = true;
        Collections.sort( controls );
        Collections.sort( validatables );
        for( UIControl u : controls ) {
            //index all default focusable controls
            if ( u instanceof UIInput || u instanceof UISubControl ) {
                focusableControls.add( u );
            }
            
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  control update/refresh  ">
    public void notifyDepends( UIControl u ) {
        Set<UIControl> refreshed = new HashSet();
        if ( !ValueUtil.isEmpty(u.getName()) && depends.containsKey(u.getName()) ) {
            for( UIControl uu : depends.get(u.getName() ) ) {
                _doRefresh( uu, refreshed );
            }
        }
        refreshed.clear();
        refreshed = null;
        
        for (BindingListener bl : listeners) {
            bl.notifyDepends(u, this);
        }
    }
    
    /**
     *@description
     *  refreshes all UIControls in this binding
     */
    public void refresh() {
        refresh(null);
    }
    
    /**
     *@description
     *  accepts regex expression of filednames
     *  sample usage: refresh("field1|field2|entity.*")
     */
    public void refresh(String regEx) {
        Set<UIControl> refreshed = new HashSet();
        for( UIControl uu : controls ) {
            String name = uu.getName();
            if ( regEx != null && name != null && !name.matches(regEx) ){
                continue;
            }
            
            _doRefresh( uu, refreshed );
        }
        refreshed.clear();
        refreshed = null;
        
        for (BindingListener bl : listeners) {
            bl.refresh(regEx);
        }
    }
    
    private void _doRefresh( UIControl u, Set refreshed ) {
        if( refreshed.add(u) ) {
            if ( u instanceof UIComposite ) {
                UIComposite comp = (UIComposite)u;
                for (UIControl uic: comp.getControls()) {
                    applyStyle(uic);
                }
            } else {
                applyStyle(u);
            }
            
            u.refresh();
            String name = u.getName();
            if( !ValueUtil.isEmpty(name) && depends.containsKey(name) ) {
                for( UIControl uu : depends.get( u.getName() )) {
                    _doRefresh( uu, refreshed );
                }
            }
        }
    }
    
    public final void applyStyle(UIControl u) {
        if ( styleRules == null ) return;
        
        //apply style rules
        for(StyleRule r : styleRules) {
            String pattern = r.getPattern();
            String rule = r.getExpression();
            
            //test expression
            boolean applyStyles = false;
            if ( rule!=null ){
                try {
                    Object o = ClientContext.getCurrentContext().getExpressionResolver().evaluate(getBean(), rule);
                    applyStyles = Boolean.valueOf(o+"");
                } catch (Exception ign){
                    System.out.println("STYLE RULE ERROR: " + ign.getMessage());
                }
            }
            if ( applyStyles ) {
                String name = u.getName();
                if( name == null ) name = "_any_name";
                if( name.matches(pattern) ) {
                    ControlSupport.setStyles( r.getProperties(), (Component) u );
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  utility methods  ">
    public void validate(ActionMessage actionMessage) {
        boolean first = true;
        for ( Validatable vc: validatables ) {
            Component comp = (Component) vc;
            if ( !comp.isFocusable() || !comp.isEnabled() ) {
                //do not validate non-focusable or disabled fields.
                continue;
            }
            
            if ( vc instanceof UIInput ) {
                //do not validate readonly fields
                if ( ((UIInput)vc).isReadonly() ) continue;
            }
            
            vc.validateInput();
            ActionMessage ac = vc.getActionMessage();
            if ( ac.hasMessages() ) {
                if ( first ) {
                    first = false;
                    ((Component) vc).requestFocus();
                }
                actionMessage.addMessage(ac);
            }
        }
        
        for (BindingListener bl: listeners ) {
            bl.validate(actionMessage, this);
        }
    }
    
    public void formCommit() {
        for ( UIControl u: focusableControls ) {
            if ( !(u instanceof UIInput) ) continue;
            if ( ValueUtil.isEmpty(u.getName()) ) continue;
            
            UIInput ui = (UIInput) u;
            if ( ui.isImmediate() ) continue;
            if ( ui.isReadonly() ) continue;
            
            Component c = (Component) ui;
            if ( !c.isEnabled() || !c.isFocusable() || !c.isVisible() ) continue;
            
            Object compValue = ui.getValue();
            Object beanValue = UIControlUtil.getBeanValue(ui);
            if ( !ValueUtil.isEqual(compValue, beanValue) ) {
                UIInputUtil.updateBeanValue(ui);
            }
        }
        
        for (BindingListener bl : listeners) {
            bl.formCommit();
        }
    }
    
    public void update() {
        //clear changeLog
        if ( changeLog != null ) changeLog.clear();
        
        for (BindingListener bl : listeners) {
            bl.update();
        }
    }
    
    public void addBindingListener(BindingListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(BindingListener listener) {
        listeners.remove(listener);
    }
    
    public boolean close() {
        if ( closeMethods == null ) return true;
        
        try {
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            for ( String mname: closeMethods ) {
                Object o = mr.invoke(bean, mname, new Class[]{}, new Object[]{});
                if ( "false".equals(o+"") ) {
                    return false;
                }
            }
            
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
        
        return true;
    }
    
    public boolean focusFirstInput() {
        //focus first UIInput that is not disabled/readonly
        for (UIControl u: focusableControls ) {
            if ( u instanceof UISubControl ) {
                UISubControl uis = (UISubControl) u;
                if ( uis.focusFirstInput() ) return true;
                
            } else if ( u instanceof UIInput ) {
                UIInput ui = (UIInput) u;
                Component comp = (Component) ui;
                if ( !ui.isReadonly() && comp.isEnabled() && comp.isFocusable() ) {
                    comp.requestFocus();
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * fireAction can be used to programmatically invoke a
     * bean action (emulating a UICommand action) that can
     * trigger a navigation process
     */
    public void fireAction(String action) {
        if ( ValueUtil.isEmpty(action) ) return;
        try {
            ClientContext ctx = ClientContext.getCurrentContext();
            Object outcome = null;
            if ( !action.startsWith("_")) {
                MethodResolver mr = ctx.getMethodResolver();
                outcome = mr.invoke(getBean(), action, null, null);
            } else {
                outcome = action;
            }
            
            NavigationHandler handler = ctx.getNavigationHandler();
            UIViewPanel panel = (UIViewPanel) getProperties().get(UIViewPanel.class);
            NavigatablePanel navPanel = UIControlUtil.getParentPanel(panel, null);
            if ( handler != null ) {
                handler.navigate(navPanel, null, outcome);
            }
            
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * focuses a UIControl from a code bean
     * this is helpful when you do the validation from the code and
     * you want to focus a control after displaying an error message
     */
    public void focus(String name) {
        UIControl c = controlsIndex.get(name);
        if ( c != null ) {
            Component comp = (Component) c;
            comp.requestFocus();
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public boolean isInitialized() {
        return _initialized;
    }
    
    public UIController getController() {
        return controller;
    }
    
    public void setController(UIController controller) {
        this.controller = controller;
        if ( bean == null ) {
            setBean( controller.getCodeBean() );
        }
    }
    
    public Object getBean() {
        return bean;
    }
    
    public void setBean(Object bean) {
        this.bean = bean;
        injectAnnotations( bean, bean.getClass() );
        initAnnotatedMethods();
        _load();
    }
    
    public void initAnnotatedMethods() {
        if ( closeMethods != null ) return;
        
        closeMethods = new ArrayList();
        ClassDefUtil cdu = ClassDefUtil.getInstance();
        Method[] marr = cdu.findAnnotatedMethods(bean.getClass(), Close.class);
        for (Method m: marr) {
            closeMethods.add( m.getName() );
        }
    }
    
    public void reinjectAnnotations() {
        injectAnnotations( bean, bean.getClass() );
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
    
    public StyleRule[] getStyleRules() {
        return styleRules;
    }
    
    public void setStyleRules(StyleRule[] styleRules) {
        this.styleRules = styleRules;
    }
    
    public Map getProperties() {
        return properties;
    }
    
    public void setProperties(Map properties) {
        this.properties = properties;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void injectAnnotations( Object o, Class clazz ) {
        if( o == null) return ;
        
        //check for field annotations
        for( Field f: clazz.getDeclaredFields() ) {
            boolean accessible = f.isAccessible();
            if( f.isAnnotationPresent(com.rameses.rcp.annotations.Binding.class)) {
                f.setAccessible(true);
                try {
                    f.set(o, Binding.this );
                } catch(Exception ex) {
                    System.out.println("ERROR injecting @Binding "  + ex.getMessage() );
                }
                f.setAccessible(accessible);
            } else if( f.isAnnotationPresent(com.rameses.rcp.annotations.ChangeLog.class)) {
                f.setAccessible(true);
                
                //check first if the controllers change log is not yet set.
                //The change log used will be the first one found.
                try {
                    com.rameses.rcp.annotations.ChangeLog annot = (com.rameses.rcp.annotations.ChangeLog)f.getAnnotation(com.rameses.rcp.annotations.ChangeLog.class);
                    String[] prefixes = annot.prefix();
                    ChangeLog cl = Binding.this.getChangeLog();
                    if( prefixes!=null) {
                        for(String s: prefixes) {
                            cl.getPrefix().add(s);
                        }
                    }
                    f.set(o, cl );
                } catch(Exception ex) {
                    System.out.println("ERROR injecting @ChangeLog "  + ex.getMessage() );
                }
                f.setAccessible(accessible);
            }
        }
        
        Class superClass = clazz.getSuperclass();
        if( superClass != null ) {
            injectAnnotations( o, superClass );
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  BindingAnnotationHandler (class)  ">
    private class BindingAnnotationHandler implements AnnotationFieldHandler {
        
        public Object getResource(Field f, Annotation a) throws Exception {
            Class type = a.annotationType();
            if ( type == com.rameses.rcp.annotations.Binding.class ) {
                return Binding.this;
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
