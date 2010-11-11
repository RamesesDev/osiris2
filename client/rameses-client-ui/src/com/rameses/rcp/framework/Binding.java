
package com.rameses.rcp.framework;

import com.rameses.classutils.ClassDefUtil;
import com.rameses.rcp.annotations.Close;
import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.control.XButton;
import com.rameses.rcp.ui.UIComposite;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.common.MethodResolver;
import com.rameses.platform.interfaces.ViewContext;
import com.rameses.rcp.common.Validator;
import com.rameses.rcp.common.ValidatorEvent;
import com.rameses.rcp.ui.UICompositeFocusable;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    
    //reference to the owner panel(UIViewPanel)
    private UIViewPanel owner;
    
    /**
     * this is used when referencing controller properties
     * such as controller name, id, and title
     */
    private UIController controller;
    
    /**
     * index of all controls in this binding
     * this is used when finding a control by name
     */
    private Map<String, UIControl> controlsIndex = new Hashtable();
    
    /**
     * 1. reference of all controls that can aquire default focus
     *    when the window is shown or during page navigation
     * 2. this reference contains UIInput and UICompositeFocusable only
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
    
    private KeyListener changeLogKeySupport = new ChangeLogKeySupport();
    private List<String> closeMethods;
    private List<Validator> validators = new ArrayList();
    
    /**
     * can be used by UIControls to store values
     * related to this Binding context
     */
    private Map properties = new HashMap();
    
    /**
     * EventManager is used to register any event listener for a specified UIControl name
     */
    private EventManager eventManager = new EventManager();
    
    /**
     * Reference to the ViewContext
     */
    private ViewContext viewContext;
    
    
    public Binding() {}
    
    public Binding(UIViewPanel owner) {
        this.owner = owner;
    }
    
    public UIViewPanel getOwner() {
        return owner;
    }
    
    public void addValidator(Validator validator) {
        if ( validators.contains(validator) )
            validators.add(validator);
    }
    
    public boolean removeValidator(Validator validator) {
        return validators.remove(validator);
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  control binding  ">
    private ControlEventSupport support = new ControlEventSupport();
    
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
        
        //for control event management support
        if ( control instanceof Component ) {
            Component c = (Component) control;
            c.addMouseListener(support);
            c.addKeyListener(support);
        }
    }
    
    public void init() {
        if ( _initialized ) return;
        
        _initialized = true;
        Collections.sort( controls );
        Collections.sort( validatables );
        for( UIControl u : controls ) {
            //index all default focusable controls
            if ( u instanceof UIInput || u instanceof UICompositeFocusable ) {
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
                if ( comp.isDynamic() ) comp.reload();
                
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
        boolean first = !actionMessage.hasMessages();
        for ( Validatable vc: validatables ) {
            Component comp = (Component) vc;
            if ( !comp.isFocusable() || !comp.isEnabled() || !comp.isVisible() ) {
                //do not validate non-focusable, disabled, or hidden fields.
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
    
    public void validateBean(ValidatorEvent evt) {
        for(Validator v: validators) {
            v.validate(evt);
        }
    }
    
    public void formCommit() {
        for ( UIControl u: focusableControls ) {
            if ( u instanceof UIComposite ) {
                UIComposite uc = (UIComposite) u;
                for( UIControl uu: uc.getControls() )
                    doCommit(uu);
                
            } else {
                doCommit(u);
            }
        }
        
        for (BindingListener bl : listeners) {
            bl.formCommit();
        }
    }
    
    private void doCommit(UIControl u) {
        if ( !(u instanceof UIInput) || ValueUtil.isEmpty(u.getName()) ) return;
        
        UIInput ui = (UIInput) u;
        if ( ui.isImmediate() || ui.isReadonly() ) return;
        
        Component c = (Component) ui;
        if ( !c.isEnabled() || !c.isFocusable() || !c.isVisible() ) return;
        
        Object compValue = ui.getValue();
        Object beanValue = UIControlUtil.getBeanValue(ui);
        if ( !ValueUtil.isEqual(compValue, beanValue) ) {
            UIInputUtil.updateBeanValue(ui);
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
            e.printStackTrace();
        }
        
        return true;
    }
    
    public boolean focusFirstInput() {
        //focus first UIInput that is not disabled/readonly
        for (UIControl u: focusableControls ) {
            if ( u instanceof UICompositeFocusable ) {
                UICompositeFocusable uis = (UICompositeFocusable) u;
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
     * fireNavigation can be used to programmatically trigger the navigation handler
     * from the controller's code bean
     */
    public void fireNavigation(Object outcome) {
        fireNavigation(outcome, "parent");
    }
    
    public void fireNavigation(Object outcome, String target) {
        try {
            ClientContext ctx = ClientContext.getCurrentContext();
            NavigationHandler handler = ctx.getNavigationHandler();
            NavigatablePanel navPanel = UIControlUtil.getParentPanel(owner, target);
            if ( handler != null ) {
                handler.navigate(navPanel, null, outcome);
            }
            
        } catch(Exception e) {
            ClientContext.getCurrentContext().getPlatform().showError(owner, e);
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
    
    /**
     * returns the UIControl w/ the specified name
     */
    public UIControl find(String name) {
        if ( name == null ) return null;
        
        return controlsIndex.get(name);
    }
    
    /**
     *
     */
    public void setTitle(String title) {
        if ( viewContext != null && viewContext.getSubWindow() != null )
            viewContext.getSubWindow().setTitle(title);
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
        Method[] cm = cdu.findAnnotatedMethods(bean.getClass(), Close.class);
        for (Method m: cm) {
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
    
    //<editor-fold defaultstate="collapsed" desc="  ControlEventSupport (class)  ">
    private class ControlEventSupport implements MouseListener, KeyListener {
        
        public void mouseClicked(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        
        public void mouseReleased(MouseEvent e) {
            if ( e.getButton() == MouseEvent.BUTTON1 ) {
                ControlEvent evt = createControlEvent(e);
                evt.setEventName(ControlEvent.LEFT_CLICK);
                eventManager.notify( evt.getSource(), evt);
                
            } else if ( e.getButton() == MouseEvent.BUTTON3 ) {
                ControlEvent evt = createControlEvent(e);
                evt.setEventName(ControlEvent.RIGHT_CLICK);
                eventManager.notify( evt.getSource(), evt);
            }
        }
        
        public void keyTyped(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
        
        private ControlEvent createControlEvent(ComponentEvent e) {
            Component c = (Component) e.getSource();
            ControlEvent evt = new ControlEvent();
            evt.setSource( c.getName() );
            evt.setSourceEvent(e);
            return evt;
        }
        
    }
    //</editor-fold>
    
    public ViewContext getViewContext() {
        return viewContext;
    }
    
    public void setViewContext(ViewContext viewContext) {
        this.viewContext = viewContext;
    }
    
}
