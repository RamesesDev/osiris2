package com.rameses.rcp.framework;

import com.rameses.rcp.annotations.Controller;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author jaycverg
 */
public abstract class ControllerProvider 
{
    private Map<Class,Field> classIndex = new Hashtable();
    
    public UIController getController(String name, UIController caller) {
        UIController controller = provide(name, caller);
        Object bean = controller.getCodeBean();
        injectController( bean, bean.getClass(), controller );
        
        return controller;
    }
    
    protected abstract UIController provide(String name, UIController caller);
    
    private void injectController( Object o, Class clazz, UIController u ) {
        if( o == null) return;
        
        if( classIndex.containsKey(clazz) ) {
            Field f = classIndex.get(clazz);
            if( f != null ) {
                setValue(f, o, u);
            }
        }
        else {
            for( Field f: clazz.getDeclaredFields() ) {
                //inject Controller
                if( f.isAnnotationPresent( Controller.class )) {
                    setValue( f, o, u );
                    classIndex.put(clazz, f);
                    return;
                }
            }
            if( clazz.getSuperclass() != null ) {
                injectController( o, clazz.getSuperclass(), u );
            }
        }
    }
    
    private void setValue(Field f, Object owner, Object value) {
        boolean accessible = f.isAccessible();
        f.setAccessible(true);
        try {
            f.set(owner, value);
        } catch(Exception ex) {
            System.out.println("ERROR injecting @Controller "  + ex.getMessage() );
        }
        f.setAccessible(accessible);
    }
    
}
